package nl.deltares.keycloak.provider.user;

import nl.deltares.keycloak.IntegrationTestCategory;
import nl.deltares.keycloak.KeycloakTestServer;
import nl.deltares.keycloak.storage.rest.MailingRepresentation;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Category(IntegrationTestCategory.class)
public class UserEventListenerProviderTest {

    private KeycloakUtilsImpl adminUtils = KeycloakTestServer.getAdminKeycloakUtils();
    private UserRepresentation newUser;

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {

        System.setProperty("csv_prefix", name.getMethodName());

        newUser = new UserRepresentation();
        newUser.setFirstName("First");
        newUser.setLastName("Last");
        newUser.setEmail("first.last@test.nl");
        newUser.setUsername("first.last");
        newUser.setEnabled(true);
        newUser.setEmailVerified(true);

        List<CredentialRepresentation> credentials = new ArrayList<>();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setValue("test");
        credential.setType("password");
        credentials.add(credential);
        newUser.setCredentials(credentials);

        adminUtils.createUser(newUser);

    }

    @Test
    public void testHandleEvent() throws IOException {

        List<MailingRepresentation> existingMailings = adminUtils.getMailingsAdminApi(null, null);

        for (MailingRepresentation mailing : existingMailings) {
            StringWriter writer = new StringWriter();
            adminUtils.exportUserMailingsAdminApi(writer, mailing.getId());
            Assert.assertTrue(writer.toString().contains(newUser.getEmail()));
        }

    }

    @After
    public void tearDown() throws Exception {
        adminUtils.deleteUser(adminUtils.getUserId("first.last@test.nl")); //cleanup user mailings
    }
}
