package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestSuite;
import nl.deltares.keycloak.utils.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Tag("IntegrationTestCategory")
public class UserAttributesApiTest {


    @BeforeAll
    public static void startUp() throws Throwable {
        if (!IntegrationTestSuite.isKeycloakRunning()) {
            IntegrationTestSuite.startKeyCloak();
        }
        assertTrue(IntegrationTestSuite.isKeycloakRunning());
    }

    @Test
    public void adminApiExportUserAttributes() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        final HashMap<String, List<String>> attributes = new HashMap<>();
        attributes.put("attribute.city", Collections.singletonList("delft"));
        attributes.put("attribute.country", Collections.singletonList("nl"));
        String userId1 = keycloakUtils.getOrCreateUser("existing", "user1", "user1", "user1@test.nl", attributes);
        String userId2 = keycloakUtils.getOrCreateUser("existing", "user2", "user2", "user2@test.nl", attributes);

        try (StringWriter writer = new StringWriter()) {
            int status = keycloakUtils.exportUserAttributesAdminApi(writer, "attribute.");
            assertEquals(200, status);
            String exportedAttributes = writer.toString();
            System.out.println(exportedAttributes);
            assertTrue(exportedAttributes.contains("attribute.country;nl;user1@test.nl"));
            assertTrue(exportedAttributes.contains("attribute.country;nl;user2@test.nl"));
            assertTrue(exportedAttributes.contains("attribute.city;delft;user1@test.nl"));
            assertTrue(exportedAttributes.contains("attribute.city;delft;user2@test.nl"));
        }

    }

    @Test
    public void adminApiExportUserAttributesUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        try (StringWriter writer = new StringWriter()){
            keycloakUtils.exportUserAttributesAdminApi(writer, "login.");
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Error 403"));
        }

    }
}
