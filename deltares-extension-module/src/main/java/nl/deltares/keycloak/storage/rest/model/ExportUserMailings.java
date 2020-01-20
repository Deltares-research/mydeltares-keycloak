package nl.deltares.keycloak.storage.rest.model;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;

import javax.persistence.TypedQuery;
import java.util.List;

public class ExportUserMailings implements ExportCsvContent {

    private final String[] headers = new String[]{"mailing", "firstName", "lastName", "email", "salutation", "organization", "country"};
    private final String[] values;
    private final Mailing mailing;
    private final UserProvider userProvider;
    private final RealmModel realm;
    private final TypedQuery<UserMailing> query;

    private String emptyLine = null;
    private String separator = ";";
    private int totalCount = 0;
    private int rsCount = 0;
    private List<UserMailing> userMailings = null;

    public ExportUserMailings(UserProvider userProvider, RealmModel realmModel, TypedQuery<UserMailing> query, Mailing mailing) {
        this.userProvider = userProvider;
        this.realm = realmModel;
        this.query = query;
        this.query.setMaxResults(100);
        this.query.setFirstResult(0);
        this.mailing = mailing;
        values = new String[headers.length];
        values[0] = mailing.getName();
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
            userMailings = query.getResultList();
        }
        //Check if all list items have been processed
        if (rsCount < userMailings.size()) return true;

        //Reached end of list get more elements
        query.setFirstResult(totalCount);
        rsCount = 0;
        userMailings = query.getResultList();
        return userMailings.size() > 0;
    }

    @Override
    public String nextLine() {

        int curr = rsCount;
        rsCount++;
        totalCount++;
        UserMailing userMailing = userMailings.get(curr);

        UserModel user = userProvider.getUserById(userMailing.getUserId(), realm);
        if (user == null) return emptyLine();

        values[1] = user.getFirstName();
        values[2] = user.getLastName();
        values[3] = user.getEmail();

        List<String> salutation = user.getAttribute("academicTitle");
        values[4] = salutation.size() > 0 ? salutation.get(0) : "";

        List<String> organization = user.getAttribute("org_name");
        values[5] = organization.size() > 0 ? organization.get(0) : "";

        List<String> country = user.getAttribute("org_country");
        values[6] = country.size() > 0 ? country.get(0) : "";

        return String.join(separator, values);
    }

    private String emptyLine() {
        if (emptyLine == null){
            emptyLine = String.join(separator, new String[]{mailing.getName(), "", "", "", "", "", ""});
        }
        return emptyLine;
    }
}
