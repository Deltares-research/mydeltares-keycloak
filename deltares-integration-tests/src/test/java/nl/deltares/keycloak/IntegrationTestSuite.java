package nl.deltares.keycloak;

import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import com.spotify.docker.client.exceptions.DockerException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
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
        deleteDirectoryContent(dataDir);
        if (!dataDir.exists()) Files.createDirectories(dataDir.toPath());

        setupKeycloakDatabase(testResources);
        KeycloakTestServer.startKeycloak(testResources.getPath());
    }

    private static void setupKeycloakDatabase(File testResources) throws IOException {
        File dataDir = new File(testResources, "standalone/data");
        File dataFile = new File(dataDir, "keycloak.mv.db");
        File testDataFile = new File(testResources, "testdata/keycloak.mv.db");
        Files.copy(testDataFile.toPath(), dataFile.toPath());
    }

    private static void deleteDirectoryContent(File directory)  {
        if (directory.exists()){
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectoryContent(file);
                }
            }
            directory.delete();
        }
    }

    @AfterClass
    public static void stopKeyCloak() throws DockerException, InterruptedException {
        KeycloakTestServer.stopKeycloak();
    }
}


