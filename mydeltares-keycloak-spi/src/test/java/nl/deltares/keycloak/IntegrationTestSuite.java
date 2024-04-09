package nl.deltares.keycloak;

import nl.deltares.keycloak.utils.KeycloakTestServer;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


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

       Files.copy(new File(buildLibs, "mydeltares-keycloak-spi-3.0.jar").toPath(),
                new File(testResources, "/keycloak/providers/mydeltares-keycloak-spi-3.0.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);

        KeycloakTestServer.startKeycloak(testResources.getPath());

    }

    public static void stopKeyCloak() {
        KeycloakTestServer.stopKeycloak();
    }
}
