package nl.deltares.keycloak;

import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class KeycloakTestServer {

    private static KeycloakUtilsImpl adminUtils;
    private static KeycloakUtilsImpl viewerUtils;
    private static KeycloakUtilsImpl userUtils;
    private static String containerId;
    private static GenericContainer dockerClient;
    private static boolean running;

    /**
     * Use main method to start keycloak server manually. Server can be stopped by manually updating the 'keepGoing'
     * variable.
     * @param args
     * @throws Throwable
     */
     public static void main(String[] args) throws Throwable {

         Assert.assertTrue("Usage: <path to docker root dir>",args != null && args.length ==1);

         File testResources = new File(args[0]);
         File keycloakTmpDir = Files.createTempDirectory("keycloak").toFile();

         File dataDir = new File(keycloakTmpDir, "standalone/data/");
         if (!dataDir.exists()) Files.createDirectories(dataDir.toPath());
         File deploymentDir = new File(keycloakTmpDir, "standalone/deployments/");
         if (!deploymentDir.exists()) Files.createDirectories(deploymentDir.toPath());

         Files.copy(new File(testResources, "testdata/keycloak.mv.db").toPath(), new File(dataDir, "keycloak.mv.db").toPath());
         Files.copy(new File(testResources, "testdata/admin-keycloak.properties").toPath(), new File(dataDir, "admin-keycloak.properties").toPath());
         Files.copy(new File(testResources, "testdata/user-keycloak.properties").toPath(), new File(dataDir, "user-keycloak.properties").toPath());
         Files.copy(new File(testResources, "testdata/viewer-keycloak.properties").toPath(), new File(dataDir, "viewer-keycloak.properties").toPath());
         Files.copy(new File(testResources, "testdata/deltares-extension-bundle-1.0.ear").toPath(),
                 new File(deploymentDir, "deltares-extension-bundle-1.0.ear").toPath());


         KeycloakTestServer.startKeycloak(deploymentDir.getPath(), dataDir.getPath());

         try {
             boolean keepGoing = true;
             //noinspection ConstantConditions
             while (keepGoing) {
                 Thread.sleep(5000);
             }
         } finally {
             stopKeycloak();
         }


     }

    static void startKeycloak(String deploymentDir, String resourcesDir)  {

        dockerClient =  new GenericContainer(DockerImageName.parse("quay.io/keycloak/keycloak:6.0.1"))
                .withExposedPorts(8080, 8787)
                .withEnv("DB_VENDOR", "h2")
                .withEnv("DEBUG", "true")
                .withEnv("DEBUG_PORT", "8787")
                .withEnv("HOSTNAME", "keycloak")
                .withFileSystemBind(deploymentDir, "/opt/jboss/keycloak/standalone/deployments", BindMode.READ_WRITE)
                .withFileSystemBind(resourcesDir, "/opt/jboss/keycloak/standalone/data", BindMode.READ_WRITE)
                .withCommand("-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=12345",
                        "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.authenticate=false",
                        "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.rmi.port=12345",
                        "-Djava.rmi.server.hostname=$HOSTNAME","-Dcache.export=false");

        dockerClient.start();
        String address = dockerClient.getHost();
        Integer port = dockerClient.getFirstMappedPort();

        //wait for server to start
        RequestConfig config = RequestConfig.custom().setSocketTimeout(1000).build();
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpGet request = new HttpGet(String.format("http://%s:%d/auth/", address, port));
            int count = 0;
            while (count < 10000) {
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
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.print("interrupted");
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (!running){
            stopKeycloak();
            throw new RuntimeException("Keycloak has not started!");
        } else {

        }

    }

    static void stopKeycloak() {

        if (dockerClient != null) {

            readLogs();

            // Kill container
            try {
                dockerClient.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Remove container
            try {
                dockerClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dockerClient = null;
        }

    }

    private static void readLogs() {

        try {
            System.out.println("***** Reading docker logs *****");
            String logs = dockerClient.getLogs();
            System.out.print(logs);
            System.out.println("***** Finished reading docker logs *****");
        } catch (Exception e) {
            System.out.println("Error reading docker log: " + e.getMessage());
        }

    }

    public static boolean isRunning(){
        return running;
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
        URL adminResource = KeycloakTestServer.class.getClassLoader().getResource("docker/testdata/" + fileName);
        Assert.assertNotNull(adminResource);

        File adminProperties = new File(adminResource.getFile());
        Assert.assertTrue(adminProperties.exists());

        updateAddress(adminProperties);

        return new KeycloakUtilsImpl(adminProperties);
    }

    private static void updateAddress(File adminProperties) {
        String address = dockerClient.getHost();
        Integer port = dockerClient.getFirstMappedPort();

        Path path = adminProperties.toPath();
        Charset charset = StandardCharsets.UTF_8;

        try {
            String content = new String(Files.readAllBytes(path), charset);
            content = content.replaceAll("localhost", address);
            content = content.replaceAll(":\\d+/auth", ":" + port + "/auth");
            Files.write(path, content.getBytes(charset));
        } catch (IOException e) {
            //
        }
    }
}
