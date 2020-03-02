package scripting;

import nl.deltares.keycloak.utils.KeycloakUtilsImpl;

import java.io.*;
import java.util.Properties;

public class ImportUserMailings {

    /**
     * Set the user mailings by uploading csv. Expected input properties file:
     *
     * keycloak properties example:
     *
     * keycloak.baseurl=http://keycloak.local.nl:8080/auth/realms/liferay-portal/
     * keycloak.baseapiurl=http://keycloak.local.nl:8080/auth/admin/realms/liferay-portal/
     * keycloak.clientid= client id
     * keycloak.clientsecret= client secret
     * emails= csv file with list of email addresses to delete
     * user.mailings.file = csv file with userids
     *
     * @param args
     */
    public static void main(String[] args) {
        assert args.length > 0;

        Properties properties = loadProperties(args[0]);
        if (properties == null) return;

        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        File csvFile = new File(properties.getProperty("import.user.mailings.file"));
        String mailingId = properties.getProperty("import.mailingId");

        try (FileReader reader = new FileReader(csvFile)){
            keycloakUtils.setMailingForUserIdsAdminApi(mailingId, reader);
        } catch (IOException e) {
            System.out.println("Failed to import user mailings for mailing: " + mailingId + ": " + e.getMessage());
        }
    }

    private static Properties loadProperties(String arg) {
        try (InputStream input = new FileInputStream(arg)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
