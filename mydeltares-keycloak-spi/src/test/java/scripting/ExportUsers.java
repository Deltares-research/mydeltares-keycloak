package scripting;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ExportUsers {

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

        try {
            System.out.println("User count= " +keycloakUtils.countUsers());
        } catch (IOException ignored) {


        }

        File exportDir = new File(properties.getProperty("exportDir"));

        if (!exportDir.exists() && !exportDir.mkdirs()){
            throw new RuntimeException(String.format("failed to create exportDir %s", exportDir.getAbsolutePath()));
        }

        File exportFile = new File(exportDir, "exportedUsers.csv");
        if (exportFile.exists()) exportFile.renameTo(new File(exportDir, System.currentTimeMillis() + exportFile.getName()));
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile)))) {
            writeResults(bw, "id", "username", "email", "verified", "federationid", "terms", "lastlogin"); // write header

            int nextStartIndex = 9875;
            while (nextStartIndex > -1) {
                String userJson = keycloakUtils.getUsersAdminApi(nextStartIndex, 25, null);

                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> map = mapper.readValue(userJson, mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                for (Map<String, Object> userMap : map) {
                    nextStartIndex++;
                    Object federationLink = userMap.get("federationLink");

                    Map attributes = (Map) userMap.get("attributes");
                    boolean terms = false;
                    String lastLogin = "";
                    if (attributes != null && !attributes.isEmpty()){
                        terms = attributes.get("terms_and_conditions") != null;
                        List list = (List) attributes.get("login.recent-login-date");
                        lastLogin = list != null ? (String)list.get(0) : "";
                    }

                    writeResults(bw,
                            (String)userMap.get("id"),
                            (String)userMap.get("username"),
                            (String)userMap.get("email"),
                            String.valueOf(userMap.get("emailVerified")),
                            federationLink == null ? "" : (String)federationLink,
                            String.valueOf(terms),
                            lastLogin);
                }
                if (map.isEmpty()){
                    break;
                }
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
        bw.write(String.format(format.toString(), (Object) args));
        bw.newLine();
        bw.flush();
    }
}
