package nl.deltares.keycloak.storage.rest.model;

import nl.deltares.keycloak.storage.jpa.UserMailing;
import org.jboss.logging.Logger;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;

import javax.persistence.TypedQuery;
import java.util.List;

public class ExportUserMailings implements ExportCsvContent {

    private static final Logger logger = Logger.getLogger(ExportUserMailings.class);
    private final String[] headers = new String[]{"firstName", "lastName", "email", "salutation", "organization", "country"};
    private final String[] values;
    private final UserProvider userProvider;
    private final RealmModel realm;
    private final TypedQuery<UserMailing> query;
    private final String mailingName;

    private String separator = ";";
    private int totalCount = 0;
    private int rsCount = 0;
    private int consecutiveErrorsCount = 0;
    private List<UserMailing> userMailings = null;
    private List<UserModel> allUsers = null;

    public ExportUserMailings(UserProvider userProvider, RealmModel realmModel, TypedQuery<UserMailing> query, String name) {
        this.userProvider = userProvider;
        this.realm = realmModel;
        this.query = query;
        this.query.setMaxResults(100);
        this.query.setFirstResult(0);
        this.mailingName = name;
        values = new String[headers.length];
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
        if (userMailings == null){
            logger.info("Start downloading user mailings for " + mailingName);
            userMailings = query.getResultList();
            if (userMailings.size() > 0){
                allUsers = userProvider.getUsers(realm, false);
            }
        }
        //Check if all list items have been processed
        if (rsCount < userMailings.size()) return true;

        //Reached end of list get more elements
        logger.info(String.format("%d user mailings downloaded", totalCount));
        query.setFirstResult(totalCount);
        rsCount = 0;
        userMailings = query.getResultList();
        boolean hasNext = userMailings.size() > 0;
        if (!hasNext){
            logger.info(String.format("Finished downloading %d user mailings for %s", totalCount, mailingName));
        }
        return hasNext;
    }

    @Override
    public String nextLine() {

        int curr = rsCount;
        rsCount++;
        totalCount++;
        UserMailing userMailing = userMailings.get(curr);

        UserModel user = null;
        try {
            for (UserModel userModel : allUsers) {
                if (userModel.getId().equals(userMailing.getId())) {
                    user = userModel;
                    break;
                }
            }

//            user[0] = userProvider.getUserById(userMailing.getUserId(), realm);
            if (user == null) {
                return null;
            }
        } catch (Exception e){
            consecutiveErrorsCount++;
            if (consecutiveErrorsCount > 100){
                throw e; //if there is a real problem then do not continue.
            }
            logger.warn(String.format("Error getUserById for id %s: %s", userMailing.getUserId(), e.getMessage()));
            return null;
        }
        consecutiveErrorsCount=0;

        values[0] = user.getFirstName();
        values[1] = user.getLastName();
        values[2] = user.getEmail();

        List<String> salutation = user.getAttribute("academicTitle");
        values[3] = salutation.size() > 0 ? salutation.get(0) : "";

        List<String> organization = user.getAttribute("org_name");
        values[4] = organization.size() > 0 ? organization.get(0) : "";

        List<String> country = user.getAttribute("org_country");
        values[5] = country.size() > 0 ? country.get(0) : "";

        return String.join(separator, values);
    }

}
