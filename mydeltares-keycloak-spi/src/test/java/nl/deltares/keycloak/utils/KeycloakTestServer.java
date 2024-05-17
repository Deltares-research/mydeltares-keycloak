package nl.deltares.keycloak.utils;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.shaded.org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class KeycloakTestServer {

    private static KeycloakUtilsImpl adminUtils;
    private static KeycloakUtilsImpl viewerUtils;
    private static KeycloakUtilsImpl userUtils;
    private static DockerComposeContainer dockerClient;
    private static boolean running;

    /**
     * Use main method to start keycloak server manually. Server can be stopped by manually updating the 'keepGoing'
     * variable.
     */
    public static void main(String[] args) throws Throwable {

        final File testResources = Paths.get("mydeltares-keycloak-spi", "src", "test", "resources", "docker", "keycloak").toFile();
        File keycloakTmpDir = Files.createTempDirectory("keycloak").toFile();
        final File spiBuildLib = Paths.get("mydeltares-keycloak-spi", "build", "libs").toFile();
        final File themeBuildLib = Paths.get("mydeltares-keycloak-theme", "build", "libs").toFile();
        File dataDir = new File(keycloakTmpDir, "data/import");
        dataDir.mkdirs();
        File configDir = new File(keycloakTmpDir, "conf");
        configDir.mkdir();
        File deploymentDir = new File(keycloakTmpDir, "providers");
        deploymentDir.mkdir();

        Files.copy(new File(testResources.getParent(), "docker-compose.yml").toPath(), new File(keycloakTmpDir, "docker-compose.yml").toPath());
        Files.copy(new File(testResources, "data/import/realm-export.json").toPath(), new File(dataDir, "realm-export.json").toPath());
        Files.copy(new File(testResources, "conf/keycloak.conf").toPath(), new File(configDir, "keycloak.conf").toPath());

        final WildcardFileFilter spiFilter = new WildcardFileFilter("mydeltares-keycloak-spi-*.jar");
        final File[] spiFiles = new File(spiBuildLib.getPath()).listFiles((FileFilter) spiFilter);
        for (File spiFile : spiFiles) {
            Files.copy(spiFile.toPath(), new File(deploymentDir, spiFile.getName()).toPath());
        }
        final WildcardFileFilter themeFilter = new WildcardFileFilter("mydeltares-keycloak-theme-*.jar");
        final File[] themeFiles = new File(themeBuildLib.getPath()).listFiles((FileFilter) themeFilter);
        for (File themeFile : themeFiles) {
            Files.copy(themeFile.toPath(), new File(deploymentDir, themeFile.getName()).toPath());
        }
        KeycloakTestServer.startKeycloak(keycloakTmpDir.getPath());

        try {
            while (running) {
                Thread.sleep(5000);
            }
        } finally {
            stopKeycloak();
        }


    }

    public static void startKeycloak(String keycloakDir) {

        dockerClient = new DockerComposeContainer(new File(keycloakDir, "docker-compose.yml"));
        final WaitStrategy waitStrategy = new HttpWaitStrategy().forPort(8080).withStartupTimeout(Duration.ofMinutes(1));
        dockerClient.withLogConsumer("keycloak", new ToStringConsumer() {
            @Override
            public void accept(OutputFrame outputFrame) {
                if (outputFrame.getBytes() != null) {
                    try {
                        System.out.write(outputFrame.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).withExposedService("keycloak", 8080, waitStrategy);
        dockerClient. start();
        running = true;

    }

    public static void stopKeycloak() {
        running = false;

        if (dockerClient != null) {

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


    public static boolean isRunning() {
        return running;
    }

    public static KeycloakUtilsImpl getAdminKeycloakUtils() {

        if (adminUtils != null) return adminUtils;
        adminUtils = loadPropertiesFile("admin-keycloak.properties");
        return adminUtils;
    }

    public static KeycloakUtilsImpl getViewerKeycloakUtils() {

        if (viewerUtils != null) return viewerUtils;
        viewerUtils = loadPropertiesFile("viewer-keycloak.properties");
        return viewerUtils;
    }

    public static KeycloakUtilsImpl getUserKeycloakUtils() {

        if (userUtils != null) return userUtils;
        userUtils = loadPropertiesFile("user-keycloak.properties");
        return userUtils;
    }

    private static KeycloakUtilsImpl loadPropertiesFile(String fileName) {

        final Path testResources = Paths.get( "src", "test", "resources", "docker", "keycloak");
        final File propertiesFile = new File(testResources.toFile(), fileName);
        assertTrue(propertiesFile.exists());
        return new KeycloakUtilsImpl(propertiesFile);
    }


}
