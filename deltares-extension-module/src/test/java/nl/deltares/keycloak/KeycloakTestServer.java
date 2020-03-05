package nl.deltares.keycloak;

import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.keycloak.testsuite.KeycloakServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.keycloak.policy.BlacklistPasswordPolicyProviderFactory.JBOSS_SERVER_DATA_DIR;

public class KeycloakTestServer {


    private static KeycloakUtilsImpl adminUtils;
    private static KeycloakUtilsImpl viewerUtils;
    private static KeycloakUtilsImpl userUtils;
    private static KeycloakServer keycloakServer;
    private static boolean running;

    static void startKeycloak() throws Throwable {

        URL standaloneResource = KeycloakTestServer.class.getClassLoader().getResource("standalone.zip");
        Assert.assertNotNull(standaloneResource);
        File standaloneZip = new File(standaloneResource.getFile());
        Assert.assertTrue(standaloneZip.exists());
        File standaloneDir = new File(standaloneZip.getParent(), "keycloak/standalone");
        if (standaloneDir.exists()){
            FileUtils.deleteDirectory(standaloneDir);
        }
        unzipArchive(standaloneZip, standaloneDir);

        KeycloakServer.KeycloakServerConfig serverConfig = new KeycloakServer.KeycloakServerConfig();
        serverConfig.setPort(8080);
        serverConfig.setHost("localhost");
        serverConfig.setResourcesHome(standaloneDir.getAbsolutePath());

        String dataDir = new File(standaloneDir, "data").getAbsolutePath();
        dataDir = dataDir.replaceAll("\\\\", "/");
        System.setProperty(JBOSS_SERVER_DATA_DIR, dataDir);
        KeycloakServer.configureDataDirectory();

        URL themesResource = KeycloakTestServer.class.getClassLoader().getResource("themes.zip");
        Assert.assertNotNull(themesResource);
        File themesZip = new File(themesResource.getFile());
        Assert.assertTrue(themesZip.exists());
        File themesDir = new File(themesZip.getParent(), "keycloak/themes");
        if (themesDir.exists()){
            FileUtils.deleteDirectory(themesDir);
        }
        unzipArchive(themesZip, themesDir);
        System.setProperty("keycloak.theme.dir", themesDir.getAbsolutePath());

        URL serverConfigResource = KeycloakTestServer.class.getClassLoader().getResource("META-INF/keycloak-server.json");
        Assert.assertNotNull(serverConfigResource);
        searchAndReplace(new File(serverConfigResource.getFile()), "@JBOSS_SERVER_DATA_DIR@", dataDir);

        keycloakServer = new KeycloakServer(serverConfig);
        Thread runServer = new Thread(() -> {
            try {
                keycloakServer.start();
            } catch (Throwable throwable) {
                keycloakServer = null;
                throw new RuntimeException(throwable);
            }
        });
        runServer.start();

        //wait for server to start
        RequestConfig config = RequestConfig.custom().setSocketTimeout(1000).build();
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpGet request = new HttpGet("http://localhost:8080/auth/");
            int count = 0;
            while (count < 30) {
                count++;
                CloseableHttpResponse response;
                try {
                    response = client.execute(request);
                } catch (IOException e) {
                    continue;
                }
                if (response.getStatusLine().getStatusCode() < 299) {
                    running = true;
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.print("interrupted");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void searchAndReplace(File searchFile, String searchString, String replaceString) throws IOException {
        Path path = searchFile.toPath();
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        content = content.replaceAll(searchString, replaceString);
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));
    }

    static void stopKeycloak()  {

        if (keycloakServer != null) {
            running = false;
            keycloakServer.stop();
            keycloakServer = null;
        }

    }

    public static boolean isRunning(){
        return keycloakServer != null && running;
    }

    private static void unzipArchive(File zipFile, File destDir) throws IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                if (zipEntry.isDirectory()){
                    FileUtils.forceMkdir(new File(destDir, zipEntry.getName()));
                } else {
                    File newFile = newFile(destDir, zipEntry);
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }

    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }


    public static KeycloakUtilsImpl getAdminKeycloakUtils(){

        if (adminUtils != null) return adminUtils;
        adminUtils = loadPropertiesFile("admin-keycloak.properties");
        return adminUtils;
    }

    public static KeycloakUtilsImpl getViewerKeycloakUtils(){

        if (viewerUtils != null) return viewerUtils;
        viewerUtils = loadPropertiesFile("viewer-keycloak.properties");
        return viewerUtils;
    }

    public static KeycloakUtilsImpl getUserKeycloakUtils(){

        if (userUtils != null) return userUtils;
        userUtils = loadPropertiesFile("user-keycloak.properties");
        return userUtils;
    }


    private static KeycloakUtilsImpl loadPropertiesFile(String fileName) {
        URL adminResource = KeycloakTestServer.class.getClassLoader().getResource("keycloak/standalone/configuration/" + fileName);
        Assert.assertNotNull(adminResource);

        File adminProperties = new File(adminResource.getFile());
        Assert.assertTrue(adminProperties.exists());
        return new KeycloakUtilsImpl(adminProperties);
    }
}
