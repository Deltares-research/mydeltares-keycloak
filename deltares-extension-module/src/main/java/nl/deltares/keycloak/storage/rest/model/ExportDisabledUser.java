package nl.deltares.keycloak.storage.rest.model;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.jpa.entities.UserAttributeEntity;
import org.keycloak.models.jpa.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

public class ExportDisabledUser implements ExportCsvContent {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Logger logger = Logger.getLogger(ExportDisabledUser.class);
    private final String[] headers = new String[]{"email", "disabledTime"};
    private final String[] values;
    private final RealmModel realm;
    private final KeycloakSession session;
    private final long disabledAfterMillis;
    private final long disabledBeforeMillis;
    private TypedQuery<UserEntity> query;

    private int totalCount = 0;
    private int rsCount = 0;
    private List<UserEntity> resultSets = null; // UserEntity, UserMailing
    private int maxResults = 500;
    private KeycloakSession localSession;

    public ExportDisabledUser(RealmModel realmModel, KeycloakSession session, Long disabledAfterMillis, Long disabledBeforeMillis) {
        this.values = new String[headers.length];
        this.realm = realmModel;
        this.session = session;
        this.disabledAfterMillis = disabledAfterMillis == null ? Long.MIN_VALUE : disabledAfterMillis;
        this.disabledBeforeMillis = disabledBeforeMillis == null ? Long.MAX_VALUE: disabledBeforeMillis;
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
                " WHERE u.realmId=:realmId AND u.enabled=false", UserEntity.class);
        query.setParameter("realmId", realm.getId());
        query.setMaxResults(maxResults);
    }

    @Override
    public String getId() {
        return "exportDisabledUsers";
    }

    @Override
    public String getName() {
        return "exportDisabledUsers";
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
        }
        //Loop through RS looking for next matching value
        rsCount = getNextValidRow();

        //Check if all list items have been processed
        if (rsCount < resultSets.size()) return true;

        //Reached end of list get more elements
        logger.info(String.format("%d disabled users downloaded", totalCount));
        query.setFirstResult(totalCount);
        rsCount = 0;
        resultSets = query.getResultList();
        boolean hasNext = resultSets.size() > 0;
        if (!hasNext){
            logger.info(String.format("Finished downloading %d user attributes for search '%s'", totalCount, getName()));
            close();
        }
        return hasNext;
    }

    private int getNextValidRow() {

        Arrays.fill(values, "");
        for (int i = rsCount; i < resultSets.size(); i++) {
            final UserEntity userEntity = resultSets.get(i);
            String disabledTime = null;
            final Collection<UserAttributeEntity> attributes = userEntity.getAttributes();
            for (UserAttributeEntity attribute : attributes) {
                if (attribute.getName().equals("disabledTime")){
                    disabledTime = attribute.getValue();
                    break;
                }
            }
            //No attribute disabledTime
            if (disabledTime == null){
                values[0] = userEntity.getEmail();
                values[1] = "";
                return i;
            } else {
                try {
                    final long millis = dateFormat.parse(disabledTime).getTime();
                    if (millis > disabledAfterMillis && millis < disabledBeforeMillis){
                        values[0] = userEntity.getEmail();
                        values[1] = disabledTime;
                        return i;
                    }
                } catch (ParseException e) {
                    logger.warn(String.format("Invalid disabledTime %s for user %s", disabledTime, userEntity.getEmail()));
                }
            }

        }
        return resultSets.size();
    }


    @Override
    public String[] nextRow() {
        rsCount++;
        totalCount++;
        return values;
    }

    @Override
    public int totalExportedCount() {
        return totalCount;
    }
}
