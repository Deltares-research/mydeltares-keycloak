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

     public static void main(String[] args) throws Throwable {

         Assert.assertTrue("Usage: <path to docker root dir>",args != null && args.length ==1);

         File testResources = new File(args[0]);
         File dataDir = new File(testResources, "standalone/data");
         deleteDirectoryContent(dataDir);
         if (!dataDir.exists()) Files.createDirectories(dataDir.toPath());

         setupKeycloakDatabase(testResources);
         KeycloakTestServer.startKeycloak(testResources.getPath());

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
     static void setupKeycloakDatabase(File testResources) throws IOException {
        File dataDir = new File(testResources, "standalone/data");
        File dataFile = new File(dataDir, "keycloak.mv.db");
        File testDataFile = new File(testResources, "testdata/keycloak.mv.db");
        Files.copy(testDataFile.toPath(), dataFile.toPath());
    }

     static void deleteDirectoryContent(File directory)  {
        if (directory.exists()){
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectoryContent(file);
                }
            }
            //noinspection ResultOfMethodCallIgnored
            directory.delete();
        }
    }

    static void startKeycloak(String resourceDir) throws DockerCertificateException, DockerException, InterruptedException {

        String deploymentsPath = new File(resourceDir, "standalone/deployments").getAbsolutePath();
        String dataPath = new File(resourceDir, "standalone/data").getAbsolutePath();
        String testdataPath = new File(resourceDir, "testdata").getAbsolutePath();

        dockerClient = DefaultDockerClient.fromEnv().build();

        final Map<String, List<PortBinding>> portBindings = new HashMap<>();
        portBindings.put("8080", Collections.singletonList(PortBinding.of("0.0.0.0", "8080")));
        portBindings.put("8787", Collections.singletonList(PortBinding.of("0.0.0.0", "8787")));

        final HostConfig hostConfig = HostConfig.builder()
                .portBindings(portBindings)
                .appendBinds(
                        HostConfig.Bind.from(deploymentsPath).to("/opt/jboss/keycloak/standalone/deployments").readOnly(false).build(),
                        HostConfig.Bind.from(dataPath).to("/opt/jboss/keycloak/standalone/data").readOnly(false).build(),
                        HostConfig.Bind.from(testdataPath).to("/opt/jboss/keycloak/testdata").readOnly(false).build()
                )
                .build();

        // Create container with exposed ports
        final ContainerConfig containerConfig = ContainerConfig.builder()
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
            String executeId = dockerClient.execCreate(containerId, new String[]{"cat", "/opt/jboss/keycloak/standalone/log/server.log"},
                    DockerClient.ExecCreateParam.attachStdout(),
                    DockerClient.ExecCreateParam.attachStderr()).id();

            System.out.println("***** Reading keycloak server.log ******");
            try(LogStream logStream = dockerClient.execStart(executeId)){
                if (logStream.hasNext()){
                    System.out.println(UTF_8.decode(logStream.next().content()).toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("***** Finished reading keycloak server.log *****");
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
