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

        System.setProperty("jboss.management.http.port", "9990");
        System.setProperty("jboss.management.https.port", "9993");
        System.setProperty("jboss.ajp.port", "5709");
        System.setProperty("jboss.http.port", "5780");
        System.setProperty("jboss.https.port", "5743");
        String property = System.getProperty("java.io.tmpdir");
        System.setProperty("jboss.server.temp.dir", property);

        File deltares = new File(property, "deltares");
        if (deltares.exists()){
            File[] files = deltares.listFiles();
            for (File file : files) {
                Files.delete(file.toPath());
            }
        }

        KeycloakTestServer.startKeycloak();
    }

    @AfterClass
    public static void stopKeyCloak() {
        KeycloakTestServer.stopKeycloak();
    }
}


