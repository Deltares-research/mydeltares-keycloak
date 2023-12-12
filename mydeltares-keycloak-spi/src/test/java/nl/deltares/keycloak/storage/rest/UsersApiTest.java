package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestSuite;
import nl.deltares.keycloak.utils.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTestCategory")
public class UsersApiTest {

    @BeforeAll
    public static void startUp() throws Throwable {
        if (!IntegrationTestSuite.isKeycloakRunning()) {
            IntegrationTestSuite.startKeyCloak();
        }
        assertTrue(IntegrationTestSuite.isKeycloakRunning());
    }

    @Test
    public void adminApiGetDisabledUsersUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try (StringWriter writer = new StringWriter()) {
            keycloakUtils.exportInvalidUsers(writer);
            fail();
        } catch (IOException e) {
            assertEquals("Error 403", e.getMessage().substring(0, "Error 403".length()));
        }

    }

    @Test
    public void adminApiGetInvalidUsers() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        String userId = keycloakUtils.getOrCreateUser("disabled", "user1", "adminApiGetInvalidUsers", "adminApiGetInvalidUsers@test.nl");

        try (StringWriter writer = new StringWriter()) {
            keycloakUtils.exportInvalidUsers(writer);
            assertFalse(writer.toString().contains("adminApiGetInvalidUsers@test.nl"));
        }

        UserRepresentation user = keycloakUtils.getUserByEmail("adminApiGetInvalidUsers@test.nl");
        user.setEnabled(false);

        keycloakUtils.updateUser(user);

        try (StringWriter writer = new StringWriter()) {
            keycloakUtils.exportInvalidUsers(writer);
            assertTrue(writer.toString().contains("adminapigetinvalidusers@test.nl"));
        }
    }

    @Test
    public void adminApiTestCheckNonExistingUsers() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        String userId1 = keycloakUtils.getOrCreateUser("existing", "user1", "existing-user1", "existing-user1@test.nl");
        String userId2 = keycloakUtils.getOrCreateUser("existing", "user2", "existing-user2", "existing-user2@test.nl");


        final File tempFile = File.createTempFile("test", ".csv");
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {
            fileWriter.write("Existing User1;existing-user1@test.nl\n");
            fileWriter.write("Existing User2;existing-user2@test.nl\n");
            fileWriter.write("Non, existing User1;none-existing-user3@test.nl");
            fileWriter.flush();
        }

        String response = keycloakUtils.uploadCheckUsersExistAdminApi(tempFile);
        assertTrue(response.contains("none-existing-user3@test.nl"));
        assertFalse(response.contains("existing-user1@test.nl"));
        assertFalse(response.contains("existing-user2@test.nl"));

    }

    @Test
    public void adminApiTestCheckNonExistingUsersWithQuotes() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        String userId4 = keycloakUtils.getOrCreateUser("existing", "user4", "existing-user4", "existing-user4@test.nl");
        String userId5 = keycloakUtils.getOrCreateUser("existing", "user5", "existing-user5", "existing-user5@test.nl");

        final File tempFile = File.createTempFile("test", ".csv");
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(tempFile))) {
            fileWriter.write("\"Existing, User1\";existing-user4@test.nl\n");
            fileWriter.write("\"Existing; User2\";existing-user5@test.nl\n");
            fileWriter.write("Non existing User1;none-existing-user3@test.nl");
            fileWriter.flush();
        }

        String response = keycloakUtils.uploadCheckUsersExistAdminApi(tempFile);
        assertTrue(response.contains("none-existing-user3@test.nl"));
        assertFalse(response.contains("existing-user1@test.nl"));
        assertFalse(response.contains("existing-user2@test.nl"));

    }

}
