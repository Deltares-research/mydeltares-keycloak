package nl.deltares.keycloak;

import nl.deltares.keycloak.utils.KeycloakTestServer;
import org.junit.BeforeClass;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SelectPackages({"nl.deltares.keycloak"})
@IncludeTags("IntegrationTestCategory")
@Suite
public class IntegrationTestSuite {

    public static boolean isKeycloakRunning(){
        return KeycloakTestServer.isRunning();
    }
    public static void startKeyCloak() throws Throwable {

        final File testResources = Paths.get( "src", "test", "resources", "docker").toFile();
        final File buildLibs = Paths.get( "build", "libs").toFile();

        File keycloakTmpDir = Files.createTempDirectory("keycloak").toFile();

        File dataDir = new File(keycloakTmpDir, "keycloak/data/import");
        dataDir.mkdirs();
        File configDir = new File(keycloakTmpDir, "keycloak/conf");
        configDir.mkdir();
        File deploymentDir = new File(keycloakTmpDir, "keycloak/providers");
        deploymentDir.mkdir();

        Files.copy(new File(testResources, "docker-compose.yml").toPath(), new File(keycloakTmpDir, "docker-compose.yml").toPath());
        Files.copy(new File(testResources, "keycloak/data/import/realm-export.json").toPath(), new File(dataDir, "realm-export.json").toPath());
        Files.copy(new File(testResources, "keycloak/conf/keycloak.conf").toPath(), new File(configDir, "keycloak.conf").toPath());
        Files.copy(new File(buildLibs, "mydeltares-keycloak-spi-3.0.jar").toPath(), new File(deploymentDir, "mydeltares-keycloak-spi-3.0.jar").toPath());
//        Files.copy(new File(testResources, "keycloak/providers/mydeltares-keycloak-theme-3.0.jar").toPath(), new File(deploymentDir, "mydeltares-keycloak-theme-3.0.jar").toPath());

        KeycloakTestServer.startKeycloak(keycloakTmpDir.getPath());

    }

    public static void stopKeyCloak() {
        KeycloakTestServer.stopKeycloak();
    }
}
