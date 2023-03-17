package nl.deltares.keycloak.storage.rest.model;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.jpa.entities.UserAttributeEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

public class ExportUserAttributes implements ExportCsvContent {

    private static final Logger logger = Logger.getLogger(ExportUserAttributes.class);
    private final String[] headers = new String[]{"attribute", "value", "email", "id"};
    private final String[] values;
    private final RealmModel realm;
    private final KeycloakSession session;
    private final String search;
    private TypedQuery<UserAttributeEntity> query;

    private long totalCount = 0;
    private long processedCount = 0;
    private int rsCount = 0;
    private List<UserAttributeEntity> resultSets = null; // UserEntity, UserMailing
    private int maxResults = 500;
    private int iterationCount = 0;
    private KeycloakSession localSession;

    public ExportUserAttributes(RealmModel realmModel, KeycloakSession session, String search) {
        this.values = new String[headers.length];
        this.realm = realmModel;
        this.session = session;
        this.search = search.toLowerCase();
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

        query = entityManager.createQuery("SELECT ua " +
                "FROM UserAttributeEntity AS ua " +
                " WHERE ua.user.realmId=:realmId AND lower(ua.name) like: attribute", UserAttributeEntity.class);
        query.setParameter("realmId", realm.getId());
        query.setParameter("attribute", '%' + search + '%');
        query.setMaxResults(maxResults);
    }

    @Override
    public String getId() {
        return search;
    }

    @Override
    public String getName() {
        return search;
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
            logger.info("Start downloading user attributes for search: " + getName());
            resultSets = query.getResultList();
            totalCount = resultSets.size();
        }
        //Check if all list items have been processed
        if (rsCount < resultSets.size()) return true;

        //Reached end of list get more elements
        logger.info(String.format("%d user attributes downloaded", totalCount));
        iterationCount++;
        query.setFirstResult(maxResults * iterationCount);
        rsCount = 0;
        resultSets = query.getResultList();
        totalCount += resultSets.size();
        boolean hasNext = resultSets.size() > 0;
        if (!hasNext){
            logger.info(String.format("Finished downloading %d user attributes for search '%s'", totalCount, getName()));
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
        UserAttributeEntity userAttributes  = resultSets.get(curr);
        Arrays.fill(values, "");
        values[0] = userAttributes.getName();
        values[1] = userAttributes.getValue();
        values[2] = userAttributes.getUser().getEmail();
        values[3] = userAttributes.getUser().getId();
        return values;
    }


    @Override
    public int percentProcessed() {
        return (int) (processedCount / totalCount);
    }
}
