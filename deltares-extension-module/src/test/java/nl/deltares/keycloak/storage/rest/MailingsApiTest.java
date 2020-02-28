package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestCategory;
import nl.deltares.keycloak.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.List;


@Category(IntegrationTestCategory.class)
public class MailingsApiTest {

    @Test
    public void adminApiGetMailingsByName() throws IOException {

        String name = "GetMailingByName";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        List<MailingRepresentation> mailings = keycloakUtils.getMailingsAdminApi(null, name);
        Assert.assertEquals(1, mailings.size());
        Assert.assertEquals(name, mailings.get(0).getName());
    }

    @Test
    public void adminApiGetMailingsBySearch() throws IOException {

        String search = "GetMailingBySearch";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        List<MailingRepresentation> mailings = keycloakUtils.getMailingsAdminApi(search, null);
        Assert.assertEquals(2, mailings.size());

        for (MailingRepresentation mailing : mailings) {
            Assert.assertTrue(mailing.getName().equals(search + '1') || mailing.getName().equals(search + '2'));
        }
    }

    @Test
    public void adminApiCreateMailing() throws IOException {

        String name = "CreateMailing";

        MailingRepresentation mailing = new MailingRepresentation();
        mailing.setName(name);
        mailing.setFrequency(3); //annually
        mailing.setDelivery(1); //post
        mailing.setLanguages(new String[]{"nl","en"});
        mailing.setDescription("hello");

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        List<MailingRepresentation> results = keycloakUtils.getMailingsAdminApi(null, name);
        Assert.assertEquals(0, results.size());

        Assert.assertEquals(201, keycloakUtils.createMailingAdminApi(mailing));

        results = keycloakUtils.getMailingsAdminApi(null, name);
        Assert.assertEquals(1, results.size());

        MailingRepresentation resultMailing = results.get(0);
        Assert.assertEquals(mailing.delivery, resultMailing.delivery);
        Assert.assertEquals(mailing.description, resultMailing.description);
        Assert.assertEquals(mailing.frequency, resultMailing.frequency);
        Assert.assertArrayEquals(mailing.languages, resultMailing.languages);

    }

    @Test
    public void adminApiDeleteMailing() throws IOException {

        String name = "DeleteMailing";

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        List<MailingRepresentation> results = keycloakUtils.getMailingsAdminApi(null, name);
        Assert.assertEquals(1, results.size());

        Assert.assertEquals(200, keycloakUtils.deleteMailingAdminApi("57f7aaff-ffee-49cc-9c96-02c8333bfb37"));

        results = keycloakUtils.getMailingsAdminApi(null, name);
        Assert.assertEquals(0, results.size());

    }

    @Test
    public void userApiGetMailings() throws IOException {

        String name = "UpdateMailing";

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        List<MailingRepresentation> results = keycloakUtils.getMailingsAdminApi(null, name);
        Assert.assertEquals(1, results.size());
        MailingRepresentation resultMailing = results.get(0);
        Assert.assertArrayEquals(new String[]{"en"}, resultMailing.languages);
        Assert.assertEquals(0, resultMailing.frequency);
        Assert.assertEquals(0, resultMailing.delivery);
        Assert.assertEquals("Before update", resultMailing.description);

        MailingRepresentation newMailing = new MailingRepresentation();
        newMailing.setId(resultMailing.getId());
        newMailing.setName(resultMailing.getName());
        newMailing.setLanguages(new String[]{"nl"});
        newMailing.setFrequency(3); //annually
        newMailing.setDelivery(2); //both
        newMailing.setDescription("After update");

        Assert.assertEquals(200, keycloakUtils.updateMailingAdminApi(newMailing));

        results = keycloakUtils.getMailingsAdminApi(null, name);
        Assert.assertEquals(1, results.size());

        resultMailing = results.get(0);
        Assert.assertArrayEquals(new String[]{"nl"}, resultMailing.languages);
        Assert.assertEquals(3, resultMailing.frequency);
        Assert.assertEquals(2, resultMailing.delivery);
        Assert.assertEquals("After update", resultMailing.description);

    }

    @Test
    public void adminApiGetMailingById() throws IOException {

        String name = "GetMailingByName";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        MailingRepresentation mailing = keycloakUtils.getMailingAdminApi("cb36038e-f139-43e0-a51b-874115fce148");
        Assert.assertNotNull(mailing);
        Assert.assertEquals(name, mailing.getName());
    }
}
