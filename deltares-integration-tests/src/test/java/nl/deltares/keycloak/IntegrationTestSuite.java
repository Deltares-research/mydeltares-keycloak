package nl.deltares.keycloak;

import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.file.Files;

@RunWith(WildcardPatternSuite.class)
@Categories.IncludeCategory(IntegrationTestCategory.class)
@SuiteClasses("**/*Test.class")
public class IntegrationTestSuite {

    @BeforeClass
    public static void startKeyCloak() throws Throwable {

        System.setProperty("log4j.configurationFile", "log4j.xml");

        File testResources = new File("src/test/resources/docker/testdata");
        File keycloakTmpDir = Files.createTempDirectory("keycloak").toFile();

        File dataDir = new File(keycloakTmpDir, "standalone/data");
        if (!dataDir.exists()) Files.createDirectories(dataDir.toPath());
        File deploymentDir = new File(keycloakTmpDir, "standalone/deployments");
        if (!deploymentDir.exists()) Files.createDirectories(deploymentDir.toPath());

        Files.copy(new File(testResources, "keycloak.mv.db").toPath(), new File(dataDir, "keycloak.mv.db").toPath());
        Files.copy(new File(testResources, "admin-keycloak.properties").toPath(), new File(dataDir, "admin-keycloak.properties").toPath());
        Files.copy(new File(testResources, "user-keycloak.properties").toPath(), new File(dataDir, "user-keycloak.properties").toPath());
        Files.copy(new File(testResources, "viewer-keycloak.properties").toPath(), new File(dataDir, "viewer-keycloak.properties").toPath());
        Files.copy(new File(testResources, "deltares-extension-bundle-1.0.ear").toPath(),
                new File(deploymentDir, "deltares-extension-bundle-1.0.ear").toPath());

        KeycloakTestServer.startKeycloak(deploymentDir.getPath(), dataDir.getPath());
    }

    @AfterClass
    public static void stopKeyCloak() {
        KeycloakTestServer.stopKeycloak();
    }
}


