package nl.deltares.keycloak;

import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

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

        KeycloakTestServer.startKeycloak();
    }

    @AfterClass
    public static void stopKeyCloak() {
        KeycloakTestServer.stopKeycloak();
    }
}


