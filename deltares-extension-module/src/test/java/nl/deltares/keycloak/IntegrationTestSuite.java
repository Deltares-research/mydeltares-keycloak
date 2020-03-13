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
        KeycloakTestServer.startKeycloak();
    }

    @AfterClass
    public static void stopKeyCloak() {
        KeycloakTestServer.stopKeycloak();
    }
}


