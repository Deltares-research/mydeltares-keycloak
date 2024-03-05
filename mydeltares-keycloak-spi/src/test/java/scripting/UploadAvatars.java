package scripting;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;

import java.io.*;
import java.util.Properties;

public class UploadAvatars {

    /**
     * Uploads Avatar images to the keycloak accounts database. Expected input properties file:
     * <p>
     * keycloak properties example:
     * <p>
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

        Properties properties = loadProperties("admin-keycloak.properties");
        if (properties == null) return;
        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        final File avatarFile = new File("avatar.jpg");
        try {
            keycloakUtils.uploadTestUserAvatar("cd9adcc2-40dc-465d-8c7a-4756481e9400", avatarFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static File getPortraitFile(File userPortraitsDir, String portraitId) {

        FilenameFilter filter = (dir, name) -> name.startsWith(portraitId + ".");

        File[] portraitDir = userPortraitsDir.listFiles(filter);
        assert portraitDir != null && portraitDir.length == 1;
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
