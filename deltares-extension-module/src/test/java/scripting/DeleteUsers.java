package scripting;

import java.io.*;
import java.util.Properties;

public class DeleteUsers {

    /**
     * Deletes users based on their email address. Expected input properties file:
     *
     * keycloak properties example:
     *
     * keycloak.baseurl=http://keycloak.local.nl:8080/auth/realms/liferay-portal/
     * keycloak.baseapiurl=http://keycloak.local.nl:8080/auth/admin/realms/liferay-portal/
     * keycloak.clientid= client id
     * keycloak.clientsecret= client secret
     * emails= csv file with list of email addresses to delete
     *
     * @param args
     */
    public static void main(String[] args) {
        assert args.length > 0;

        Properties properties = loadProperties(args[0]);
        if (properties == null) return;
        KeycloakUtilsImpl keycloakUtils = new KeycloakUtilsImpl(properties);

        File emailsFile = new File(properties.getProperty("emails"));
        File failedEmails = new File(emailsFile.getParent(), "failedEmails.csv");

        if (failedEmails.exists()) failedEmails.renameTo(new File(emailsFile.getParent(), System.currentTimeMillis() + '_' +emailsFile.getName()));
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(failedEmails)))) {
            writeResults(bw, "email", "result", "message"); // write header
            try (BufferedReader reader = new BufferedReader(new FileReader(emailsFile))) {
                String line = reader.readLine();
                while (line != null) {
                    String email = line.trim();
                    String userId = null;
                    try {
                        userId = keycloakUtils.getUserId(email);
                        if (userId == null) {
                            writeResults(bw, email, "missing");
                        }
                    } catch (Exception e) {
                        writeResults(bw, email, "error");
                    }

                    try {
                        if (userId != null) {
                            keycloakUtils.deleteUser(userId);
                            writeResults(bw, email, "success");
                        }
                    } catch (IOException e) {
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
