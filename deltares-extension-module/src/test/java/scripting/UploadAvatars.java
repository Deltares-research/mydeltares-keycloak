package scripting;

import java.io.*;
import java.util.Properties;

public class UploadAvatars {

    /**
     * Uploads Avatar images to the keycloak accounts database. Expected input properties file:
     *
     * keycloak properties example:
     *
     * keycloak.baseurl=http://keycloak.local.nl:8080/auth/realms/liferay-portal/
     * keycloak.baseapiurl=http://keycloak.local.nl:8080/auth/admin/realms/liferay-portal/
     * keycloak.clientid= client id
     * keycloak.clientsecret= client secret
     * avatar.dir= directory containing image files
     * avatar.ids= file containing mapping email address and image id
     *
     * @param args
     */
    public static void main(String[] args) {
        assert args.length > 0;

        Properties properties = loadProperties(args[0]);
        if (properties == null) return;
        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        File userPortraitsDir = new File(properties.getProperty("avatar.dir").trim());
        if (!userPortraitsDir.exists()){
            throw new RuntimeException("Portraits dir does not exist: " + userPortraitsDir.getAbsolutePath());
        }

        File userPortraitIdsFile = new File(properties.getProperty("avatar.ids"));
        File failedEmails = new File(userPortraitIdsFile.getParent(), "failedEmails.csv");
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(failedEmails)))) {
            writeFailed(bw, "email", "portraitId"); // write header
            try (BufferedReader reader = new BufferedReader(new FileReader(userPortraitIdsFile))) {
                String line = reader.readLine(); //skip header
                line = reader.readLine();
                while (line != null) {
                    String[] values = line.split(";");
                    String email = values[0];
                    String portraitId = values[1];

                    String userId = null;
                    try {
                        userId = keycloakUtils.getUserId(email);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if (userId == null) writeFailed(bw, email, portraitId);
                    else {
                        try {
                            File portraitFile = getPortraitFile(userPortraitsDir, portraitId);
                            keycloakUtils.uploadUserAvatar(userId, portraitFile);
                        } catch (Exception e) {
                            writeFailed(bw, email, portraitId);
                        }
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

    private static File getPortraitFile(File userPortraitsDir, String portraitId) {

        FilenameFilter filter = (dir, name) -> name.startsWith(portraitId + ".");

        File[] portraitDir = userPortraitsDir.listFiles(filter);
        assert portraitDir != null &&  portraitDir.length == 1;
        File[] portraitFile = portraitDir[0].listFiles();
        assert portraitFile != null && portraitFile.length == 1;
        return portraitFile[0];

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

    private static void writeFailed(BufferedWriter bw, String email, String portraitId) throws IOException {

        bw.write(String.format("%s;%s", email, portraitId));
        bw.newLine();
    }
}
