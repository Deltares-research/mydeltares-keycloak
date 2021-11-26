package scripting;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DisabledUsers {

    /**
     * Retrieve disapbled users from Keycloak:
     *
     * keycloak properties example:
     *
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

        Properties properties = loadProperties(args[0]);
        if (properties == null) return;
        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        try {
            final List<UserRepresentation> disabledUsers = keycloakUtils.getDisabledUsers(10,10, false);
            for (UserRepresentation user : disabledUsers) {
                System.out.println(String.format("email: %s, id: %s, enabled:%s", user.getEmail(), user.getId(), user.isEnabled()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getIdpLink(String id, KeycloakUtilsImpl keycloakUtils) throws IOException {

        String userJson = keycloakUtils.getUserAsJson(id);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> user = mapper.readValue(userJson, mapper.getTypeFactory().constructType(Map.class));
        List federatedIdentities = (List) user.get("federatedIdentities");
        if (federatedIdentities.size() > 0){
            return (String) ((Map)federatedIdentities.get(0)).get("identityProvider");
        }
        return null;
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
