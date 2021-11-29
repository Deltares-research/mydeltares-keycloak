package scripting;

import nl.deltares.keycloak.utils.KeycloakUtilsImpl;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class DisabledUsers {

    /**
     * Retrieve disapbled users from Keycloak:
     * <p>
     * keycloak properties example:
     * <p>
     * keycloak.baseurl=http://keycloak.local.nl:8080/auth/realms/liferay-portal/
     * keycloak.baseapiurl=http://keycloak.local.nl:8080/auth/admin/realms/liferay-portal/
     * keycloak.clientid= client id
     * keycloak.clientsecret= client secret
     * exportDir= directory to export
     *
     * @param args
     */
    public static void main(String[] args) {
        assert args.length > 0;

        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        Properties properties = loadProperties(args[0]);
        if (properties == null) return;
        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        File exportDir = new File(properties.getProperty("exportDir"));

        if (!exportDir.exists() && !exportDir.mkdirs()) {
            throw new RuntimeException(String.format("failed to create exportDir %s", exportDir.getAbsolutePath()));
        }

        File exportFile = new File(exportDir, "exportedDisabledUsers.csv");
        if (exportFile.exists())
            exportFile.renameTo(new File(exportDir, System.currentTimeMillis() + exportFile.getName()));
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))) {
            Long after =  format.parse("20211101000000").getTime();
            keycloakUtils.exportDisabledUsers(bw, after, null);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
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
