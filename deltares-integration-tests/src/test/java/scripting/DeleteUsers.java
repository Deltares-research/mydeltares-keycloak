package scripting;

import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.*;
import java.util.Properties;

public class DeleteUsers {

    /**
     * Deletes users based on their userId. Expected input properties file:
     *
     * keycloak properties example:
     *
     * keycloak.baseurl=http://keycloak.local.nl:8080/auth/realms/liferay-portal/
     * keycloak.baseapiurl=http://keycloak.local.nl:8080/auth/admin/realms/liferay-portal/
     * keycloak.clientid= client id
     * keycloak.clientsecret= client secret
     * users= csv file with list of userids to delete
     *
     * @param args
     */
    public static void main(String[] args) {
        assert args.length > 0;

        Properties properties = loadProperties(args[0]);
        if (properties == null) return;
        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        File usersFile = new File(properties.getProperty("users"));
        File failed = new File(usersFile.getParent(), "deletion-results.csv");

        if (failed.exists()) failed.renameTo(new File(usersFile.getParent(), System.currentTimeMillis() + '_' +usersFile.getName()));
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(failed)))) {
            writeResults(bw, "id", "result", "message"); // write header
            try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
                String line = reader.readLine();
                while (line != null) {
                    String email = line.trim();
                    final UserRepresentation userRep;
                    try {
                         userRep = keycloakUtils.getUserByEmail(email);
                         if (userRep != null){
                             keycloakUtils.deleteUser(userRep.getId());
                             writeResults(bw, email, "success");
                         } else {
                             writeResults(bw, email, "user not found");
                         }
                    } catch (IOException e){
                        System.out.println(e.getMessage());
                        writeResults(bw, email, "error");
                    }
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
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

    private static void writeResults(BufferedWriter bw, String... args) throws IOException {

        StringBuilder format = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            format.append("%s;");
        }
        bw.write(String.format(format.toString(), args));
        bw.newLine();
        bw.flush();
    }
}
