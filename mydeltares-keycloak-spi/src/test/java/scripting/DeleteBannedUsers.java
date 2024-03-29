package scripting;

import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class DeleteBannedUsers {

    /**
     * Deletes users based on their screenname or email address. Expected input is array of search strings.
     *
     * @param args
     */
    public static void main(String[] args) {
        assert args.length > 0;

        Properties properties = loadProperties(args[0]);
        if (properties == null) return;
        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        String[] bannedUsers = {};

        for (String screenName : bannedUsers) {
            try {
                List<UserRepresentation> userRepresentation = keycloakUtils.searchUser(screenName);
                if (userRepresentation == null || userRepresentation.size() == 0) {
                    System.out.println("Could not find user for " + screenName);
                } else if (userRepresentation.size() > 1){
                    System.out.println("Multiple results found for " + screenName);
                    for (UserRepresentation user : userRepresentation) {
                        if (!user.getUsername().equals(screenName)) continue;
                        keycloakUtils.deleteUser(user.getId());
                        System.out.println("deleted: " + screenName);
                    }
                } else {
                    UserRepresentation user = userRepresentation.get(0);
//                    user.setEnabled(false);
                    keycloakUtils.deleteUser(user.getId());
                    System.out.println("deleted: " + screenName);
                }

            } catch (Exception e) {
                System.out.println("error for " + screenName + ": " + e.getMessage());
            }
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
        bw.write(String.format(format.toString(), (Object[]) args));
        bw.newLine();
        bw.flush();
    }
}
