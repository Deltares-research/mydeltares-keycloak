package nl.deltares.keycloak.storage.rest.model;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.jpa.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

public class ExportInvalidUser implements ExportCsvContent {

    private static final Logger logger = Logger.getLogger(ExportInvalidUser.class);
    private final String[] headers = new String[]{"keycloakId", "email", "enabled", "emailVerified"};
    private final String[] values;
    private final RealmModel realm;
    private final KeycloakSession session;
    private TypedQuery<UserEntity> query;

    private int totalCount = 0;
    private int rsCount = 0;
    private List<UserEntity> resultSets = null; // UserEntity, UserMailing
    private int maxResults = 500;
    private int iterationCount = 0;
    private KeycloakSession localSession;

    public ExportInvalidUser(RealmModel realmModel, KeycloakSession session) {
        this.values = new String[headers.length];
        this.realm = realmModel;
        this.session = session;
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

        query = entityManager.createQuery("SELECT u " +
                "FROM UserEntity AS u " +
                " WHERE u.realmId=:realmId AND (u.enabled=false OR u.emailVerified=false)", UserEntity.class);
        query.setParameter("realmId", realm.getId());
        query.setMaxResults(maxResults);
    }

    @Override
    public String getId() {
        return "exportInvalidUsers";
    }

    @Override
    public String getName() {
        return "exportInvalidUsers";
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
        if (resultSets == null) {
            logger.info("Start downloading users for search: " + getName());
            resultSets = query.getResultList();
        }

        //Loop through RS looking for next matching value
        rsCount = getNextValidRow();

        //Check if all list items have been processed
        if (rsCount < resultSets.size()) return true;

        //Reached end of list get more elements starting from end of last search
        logger.info(String.format("%d invalid users downloaded", totalCount));
        iterationCount++;
        query.setFirstResult(maxResults * iterationCount);
        rsCount = 0;
        resultSets = query.getResultList();

        rsCount = getNextValidRow();
        //Check if all list items have been processed
        if (rsCount < resultSets.size()) return true;
        logger.info(String.format("Finished downloading %d users for search '%s'", totalCount, getName()));
        close();
        return false;
    }

    private boolean isSystemEmail(String email) {
        return email == null || email.endsWith("@liferay.com") || email.endsWith("@placeholder.org");
    }

    private int getNextValidRow() {

        Arrays.fill(values, "");
        for (int i = rsCount; i < resultSets.size(); i++) {
            final UserEntity userEntity = resultSets.get(i);
            if (!isSystemEmail(userEntity.getEmail())) {
                Arrays.fill(values, "");
                values[0] = userEntity.getId();
                values[1] = userEntity.getEmail();
                values[2] = Boolean.toString(userEntity.isEnabled());
                values[3] = Boolean.toString(userEntity.isEmailVerified());
                return i;
            }
        }
        return resultSets.size();
    }

    @Override
    public String[] nextRow() {
        if (query == null) {
            throw new IllegalStateException("First call 'hasNextRow'!");
        }
        rsCount++;
        totalCount++;
        return values;
    }

    @Override
    public int totalExportedCount() {
        return totalCount;
    }
}
