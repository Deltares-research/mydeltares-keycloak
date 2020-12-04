package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestCategory;
import nl.deltares.keycloak.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.io.StringWriter;


@Category(IntegrationTestCategory.class)
public class UserAttributesApiTest {


    @Before
    public void setUp(){
        Assert.assertTrue(KeycloakTestServer.isRunning());
    }

    @Test
    public void adminApiExportUserAttributes() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        try (StringWriter writer = new StringWriter()) {
            int status = keycloakUtils.exportUserAttributesAdminApi(writer, "login.");
            Assert.assertEquals(200, status);
            String exportedAttributes = writer.toString();
            System.out.println(exportedAttributes);
            Assert.assertTrue(exportedAttributes.matches( ".*login.login-count;\\d;user-getavatar@test.nl.*"));
        }

    }

    @Test
    public void adminApiExportUserAttrbutesUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        try (StringWriter writer = new StringWriter()){
            keycloakUtils.exportUserAttributesAdminApi(writer, "login.");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }

    }
}
