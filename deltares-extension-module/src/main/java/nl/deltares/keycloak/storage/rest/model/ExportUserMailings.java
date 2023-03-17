package nl.deltares.keycloak.storage.rest.model;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.rest.ResourceUtils;
import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.jpa.entities.UserAttributeEntity;
import org.keycloak.models.jpa.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

public class ExportUserMailings implements ExportCsvContent {

    private static final Logger logger = Logger.getLogger(ExportUserMailings.class);
    private final String[] headers = new String[]{"firstName", "lastName", "email", "salutation", "organization", "country", "language", "delivery", "unsubscribe"};
    private final String[] values;
    private final RealmModel realm;
    private final KeycloakSession session;
    private final String unsubscribeApiPath;
    private TypedQuery<Object[]> query;
    private final Mailing mailing;

    private int totalCount = 0;
    private long processedCount = 0;
    private int rsCount = 0;
    private List<Object[]> resultSets = null; // UserEntity, UserMailing
    private int maxResults = 500;
    private int iterationCount = 0;
    private KeycloakSession localSession;

    public ExportUserMailings(RealmModel realmModel, KeycloakSession session, Mailing mailing) {
        this.mailing = mailing;
        this.values = new String[headers.length];
        this.realm = realmModel;
        this.session = session;
        this.unsubscribeApiPath = session.getContext().getUri().getBaseUriBuilder().segment(
                "realms",realmModel.getName(), "user-mailings", "unsubscribe", mailing.getId()).build().toASCIIString();
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        if (maxResults < 1) throw new IllegalArgumentException("MaxResults must be larger and 0! " + maxResults);
    }

    @Override
    public String[] getHeaders() {
        return headers;
    }

    @Override
    public boolean hasHeader() {
        return true;
    }

    private void initialize() {

        localSession = session.getKeycloakSessionFactory().create();
        EntityManager entityManager = getEntityManager(localSession);
        query = entityManager.createQuery("SELECT u, m " +
                "FROM UserEntity AS u " +
                "INNER JOIN UserMailing AS m ON u.id = m.userId " +
                " WHERE u.realmId=:realmId AND m.mailingId=:mailingId", Object[].class);
        query.setParameter("realmId", realm.getId());
        query.setParameter("mailingId", mailing.getId());
        query.setMaxResults(maxResults);
    }

    @Override
    public String getId() {
        return mailing.getId();
    }

    @Override
    public String getName() {
        return mailing.getName();
    }

    @Override
    public void close() {
        if (localSession != null) {
            localSession.close();
            localSession = null;
        }
    }

    @Override
    public boolean hasNextRow() {

        if (query == null) initialize();

        //Not initialized yet
        if (resultSets == null){
            logger.info("Start downloading user mailings for " + mailing.getName());
            resultSets = query.getResultList();
            totalCount = resultSets.size();
        }
        //Check if all list items have been processed
        if (rsCount < resultSets.size()) return true;

        //Reached end of list get more elements
        logger.info(String.format("%d user mailings downloaded", totalCount));
        iterationCount++;
        query.setFirstResult(maxResults * iterationCount);
        rsCount = 0;
        resultSets = query.getResultList();
        totalCount += resultSets.size();
        boolean hasNext = resultSets.size() > 0;
        if (!hasNext){
            logger.info(String.format("Finished downloading %d user mailings for %s", totalCount, mailing.getName()));
            close();
        }
        return hasNext;
    }

    public String[] nextRow() {

        if (query == null){
            throw new IllegalStateException("First call 'hasNextRow'!");
        }
        int curr = rsCount;
        rsCount++;
        processedCount++;
        Object[] results  = resultSets.get(curr);
        if (results.length != 2) {
            throw new IllegalStateException("Expected resultSet to have two arguments; UserEntity and UserMailing! Found " + results.length);
        }
        UserEntity user = (UserEntity) results[0];
        UserMailing userMailing = (UserMailing) results[1];
        Arrays.fill(values, "");
        values[0] = user.getFirstName();
        values[1] = user.getLastName();
        values[2] = user.getEmail();

        readAttributes(values, user.getAttributes());

        values[6] = userMailing.getLanguage();
        values[7] = Mailing.deliveries.get(userMailing.getDelivery());
        values[8] = getUnsubscribeUrl(user);
        return values;
    }

    private String getUnsubscribeUrl(UserEntity user) {
        return unsubscribeApiPath + '/' + ResourceUtils.encrypt(user.getId(), mailing.getId());
    }

    private void readAttributes(String[] values, Collection<UserAttributeEntity> attributes) {

        for (UserAttributeEntity attribute : attributes) {
            String name = attribute.getName();
            if ("academictitle".equalsIgnoreCase(name)) {
                values[3] = attribute.getValue(); //salutation
            } else if ("org_name".equalsIgnoreCase(name)) {
                values[4] = attribute.getValue(); //org name
            } else if ("org_country".equalsIgnoreCase(name)) {
                values[5] = attribute.getValue(); //org country
            }

        }
    }

    @Override
    public int percentProcessed() {
        return (int)(100 * ((float)processedCount/totalCount));
    }
}
