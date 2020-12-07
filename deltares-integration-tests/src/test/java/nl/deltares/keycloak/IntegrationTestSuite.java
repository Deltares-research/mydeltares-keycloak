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

        File testResources = new File("src/test/resources/docker");
        File dataDir = new File(testResources, "standalone/data");
        KeycloakTestServer.deleteDirectoryContent(dataDir);
        if (!dataDir.exists()) Files.createDirectories(dataDir.toPath());

        KeycloakTestServer.setupKeycloakDatabase(testResources);
        KeycloakTestServer.startKeycloak(testResources.getPath());
    }

    @AfterClass
    public static void stopKeyCloak() {
        KeycloakTestServer.stopKeycloak();
    }
}


