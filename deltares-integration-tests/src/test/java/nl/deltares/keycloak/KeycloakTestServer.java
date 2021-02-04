package nl.deltares.keycloak;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KeycloakTestServer {


    private static KeycloakUtilsImpl adminUtils;
    private static KeycloakUtilsImpl viewerUtils;
    private static KeycloakUtilsImpl userUtils;
    private static String containerId;
    private static DockerClient dockerClient;
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

         File dataDir = new File(keycloakTmpDir, "standalone/data");
         if (!dataDir.exists()) Files.createDirectories(dataDir.toPath());
         File deploymentDir = new File(keycloakTmpDir, "standalone/deployments");
         if (!deploymentDir.exists()) Files.createDirectories(deploymentDir.toPath());

         Files.copy(new File(testResources, "testdata/keycloak.mv.db").toPath(), new File(dataDir, "keycloak.mv.db").toPath());
         Files.copy(new File(testResources, "testdata/admin-keycloak.properties").toPath(), new File(dataDir, "admin-keycloak.properties").toPath());
         Files.copy(new File(testResources, "testdata/user-keycloak.properties").toPath(), new File(dataDir, "user-keycloak.properties").toPath());
         Files.copy(new File(testResources, "testdata/viewer-keycloak.properties").toPath(), new File(dataDir, "viewer-keycloak.properties").toPath());
         Files.copy(new File(testResources, "testdata/deltares-extension-bundle-1.0.ear").toPath(),
                 new File(deploymentDir, "deltares-extension-bundle-1.0.ear").toPath());

         KeycloakTestServer.startKeycloak(keycloakTmpDir.getPath());

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

    static void startKeycloak(String resourceDir) throws DockerCertificateException, DockerException, InterruptedException {

        String deploymentsPath = new File(resourceDir, "standalone/deployments").getAbsolutePath();
        String dataPath = new File(resourceDir, "standalone/data").getAbsolutePath();

        dockerClient = DefaultDockerClient.fromEnv().build();

        final Map<String, List<PortBinding>> portBindings = new HashMap<>();
        portBindings.put("8080", Collections.singletonList(PortBinding.of("0.0.0.0", "8080")));
        portBindings.put("8787", Collections.singletonList(PortBinding.of("0.0.0.0", "8787")));

        final HostConfig hostConfig = HostConfig.builder()
                .portBindings(portBindings)
                .appendBinds(
                        HostConfig.Bind.from(deploymentsPath).to("/opt/jboss/keycloak/standalone/deployments").readOnly(false).build(),
                        HostConfig.Bind.from(dataPath).to("/opt/jboss/keycloak/standalone/data").readOnly(false).build()
                )
                .build();

        // Create container with exposed ports
        final ContainerConfig containerConfig = ContainerConfig.builder()
                .user("root")
                .hostConfig(hostConfig)
                .image("quay.io/keycloak/keycloak:6.0.1")
                .exposedPorts("8080","8787")
                .env("DB_VENDOR=h2", "DEBUG=true", "DEBUG_PORT=8787", "HOSTNAME=keycloak")
                .hostname("keycloak")
                .cmd("-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=12345",
                        "-Dcom.sun.management.jmxremote.local.only=false", "-Dcom.sun.management.jmxremote.authenticate=false",
                        "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.rmi.port=12345",
                        "-Djava.rmi.server.hostname=$HOSTNAME", "-Dcache.export=false")
                .build();

        final ContainerCreation creation = dockerClient.createContainer(containerConfig, "test-keycloak");

        containerId = creation.id();
        dockerClient.startContainer(containerId);

        //wait for server to start
        RequestConfig config = RequestConfig.custom().setSocketTimeout(1000).build();
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpGet request = new HttpGet(String.format("http://localhost:%d/auth/", 8080));
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
        }

    }

    static void stopKeycloak() {

        if (dockerClient != null) {

            readLogs();

            // Kill container
            try {
                dockerClient.killContainer(containerId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Remove container
            try {
                dockerClient.removeContainer(containerId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Close the docker client
            dockerClient.close();
            dockerClient = null;
        }

    }

    private static void readLogs() {

        try {
            System.out.println("***** Reading docker logs *****");
            LogStream logs = dockerClient.logs(containerId, DockerClient.LogsParam.stdout(), DockerClient.LogsParam.stderr());
            System.out.print(logs.readFully());
            System.out.println("***** Finished reading docker logs *****");
        } catch (Exception e) {
            System.out.println("Error reading docker log: " + e.getMessage());
        }


        try {

            String executeId = dockerClient.execCreate(containerId, new String[]{"cat", "/opt/jboss/keycloak/standalone/log/server.log"},
                    DockerClient.ExecCreateParam.attachStdout(),
                    DockerClient.ExecCreateParam.attachStderr()).id();

            System.out.println("***** Reading keycloak server.log ******");
            try(LogStream logStream = dockerClient.execStart(executeId)){
                if (logStream.hasNext()){
                    System.out.println(UTF_8.decode(logStream.next().content()).toString());
                }
            }
            System.out.println("***** Finished reading keycloak server.log *****");

        } catch (Exception e) {
            System.out.println("Error reading keycloak log: " + e.getMessage());
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
        return new KeycloakUtilsImpl(adminProperties);
    }
}