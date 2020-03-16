package scripting;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ExportDeltaresUsers {

    /**
     * Export users and related attributes. Expected input properties file:
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

        File exportDir = new File(properties.getProperty("exportDir"));

        if (!exportDir.exists() && !exportDir.mkdirs()){
            throw new RuntimeException(String.format("failed to create exportDir %s", exportDir.getAbsolutePath()));
        }

        File exportFile = new File(exportDir, "exportedDeltaresUsers.csv");
        if (exportFile.exists()) exportFile.renameTo(new File(exportDir, System.currentTimeMillis() + exportFile.getName()));
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))) {
            writeResults(bw, "id", "username", "email", "verified", "federationid", "terms", "lastlogin", "idplink"); // write header

            int nextStartIndex = 0;
            while (nextStartIndex > -1) {
                String userJson = keycloakUtils.getUsersAdminApi(nextStartIndex, 100, "@deltares.nl");

                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> map = mapper.readValue(userJson, mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                for (Map<String, Object> userMap : map) {

                    Object federationLink = userMap.get("federationLink");

                    Map attributes = (Map) userMap.get("attributes");
                    boolean terms = false;
                    String lastLogin = "";
                    if (attributes != null && attributes.size() > 0){
                        terms = attributes.get("terms_and_conditions") != null;
                        List list = (List) attributes.get("login.recent-login-date");
                        lastLogin = list != null ? (String)list.get(0) : "";
                    }

                    String id = (String) userMap.get("id");
                    writeResults(bw,
                            id,
                            (String)userMap.get("username"),
                            (String) userMap.get("email"),
                            String.valueOf(userMap.get("emailVerified")),
                            federationLink == null ? "" : (String)federationLink,
                            String.valueOf(terms),
                            lastLogin,
                            getIdpLink(id, keycloakUtils));
                }
                if (map.size() == 0){
                    break;
                } else {
                    nextStartIndex = nextStartIndex + 100;
                }
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
