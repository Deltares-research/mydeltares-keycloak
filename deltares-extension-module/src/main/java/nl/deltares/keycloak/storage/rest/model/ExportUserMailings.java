package nl.deltares.keycloak.storage.rest.model;

import nl.deltares.keycloak.storage.jpa.Mailing;
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

    private static final Logger logger = Logger.getLogger(ExportUserMailings2.class);
    private final String[] headers = new String[]{"firstName", "lastName", "email", "salutation", "organization", "country"};
    private final String[] values;
    private final TypedQuery<UserEntity> query;
    private final Mailing mailing;

    private String separator = ";";
    private int totalCount = 0;
    private int rsCount = 0;
    private List<UserEntity> resultSets = null;

    public ExportUserMailings(RealmModel realmModel, KeycloakSession session, Mailing mailing) {
        this.mailing = mailing;
        this.values = new String[headers.length];
        EntityManager entityManager = getEntityManager(session);
        query = entityManager.createQuery("SELECT u " +
                "FROM UserEntity AS u " +
                "INNER JOIN UserMailing AS m ON u.id = m.userId " +
                " WHERE u.realmId=:realmId AND m.mailingId=:mailingId", UserEntity.class);
        this.query.setParameter("realmId", realmModel.getId());
        this.query.setParameter("mailingId", mailing.getId());
    }

    public void setMaxResults(int maxResults) {
        if (maxResults < 1) throw new IllegalArgumentException("MaxResults must be larger and 0! " + maxResults);
        query.setMaxResults(maxResults);
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String getHeader() {
        return String.join(separator, headers);
    }

    @Override
    public boolean hasHeader() {
        return true;
    }

    @Override
    public boolean hasNextLine() {

        //Not initialized yet
        if (resultSets == null){
            logger.info("Start downloading user mailings for " + mailing.getName());
            resultSets = query.getResultList();
        }
        //Check if all list items have been processed
        if (rsCount < resultSets.size()) return true;

        //Reached end of list get more elements
        logger.info(String.format("%d user mailings downloaded", totalCount));
        query.setFirstResult(totalCount);
        rsCount = 0;
        resultSets = query.getResultList();
        boolean hasNext = resultSets.size() > 0;
        if (!hasNext){
            logger.info(String.format("Finished downloading %d user mailings for %s", totalCount, mailing.getName()));
        }
        return hasNext;
    }

    public String nextLine() {

        int curr = rsCount;
        rsCount++;
        totalCount++;
        UserEntity user = resultSets.get(curr);
        Arrays.fill(values, "");
        values[0] = user.getFirstName();
        values[1] = user.getLastName();
        values[2] = user.getEmail();

        readAttributes(values, user.getAttributes());

        return String.join(separator, values);
    }

    private void readAttributes(String[] values, Collection<UserAttributeEntity> attributes) {

        for (UserAttributeEntity attribute : attributes) {
            String name = attribute.getName();
            if ("academicTitle".equals(name)) {
                values[3] = attribute.getValue(); //salutation
            } else if ("org_name".equals(name)) {
                values[4] = attribute.getValue(); //org name
            } else if ("org_country".equals(name)) {
                values[5] = attribute.getValue(); //org country
            }

        }
    }

    @Override
    public int totalExportedCount() {
        return totalCount;
    }
}
