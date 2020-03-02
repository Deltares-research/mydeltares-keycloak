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
    public void adminApiGetMailingsByNameUnauthorized()  {

        String name = "GetMailingByName";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getMailingsAdminApi(null, name);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }
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
    public void adminApiGetMailingsBySearchUnauthorized() {

        String search = "GetMailingBySearch";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getMailingsAdminApi(search, null);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
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
    public void adminApiCreateMailingUnauthorized(){

        MailingRepresentation mailing = new MailingRepresentation();
        mailing.setName("WillFail");
        mailing.setFrequency(3); //annually
        mailing.setDelivery(1); //post
        mailing.setLanguages(new String[]{"nl","en"});
        mailing.setDescription("Please fail");

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();
        try {
            keycloakUtils.createMailingAdminApi(mailing);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }

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
    public void adminApiDeleteMailingUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();
        try {
            keycloakUtils.deleteMailingAdminApi("57f7aaff-ffee-49cc-9c96-02c8333bfb37");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }

    }

    @Test
    public void adminApiUpdateMailing() throws IOException {

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
    public void adminApiUpdateMailingUnauthorized() throws IOException {
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

        KeycloakUtilsImpl viewerKeycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();
        try {
            viewerKeycloakUtils.updateMailingAdminApi(newMailing);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }
    }

    @Test
    public void adminApiGetMailingById() throws IOException {

        String name = "GetMailingByName";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        MailingRepresentation mailing = keycloakUtils.getMailingAdminApi("cb36038e-f139-43e0-a51b-874115fce148");
        Assert.assertNotNull(mailing);
        Assert.assertEquals(name, mailing.getName());
    }

    @Test
    public void adminApiGetMailingByIdUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getMailingAdminApi("cb36038e-f139-43e0-a51b-874115fce148");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }
    }

    @Test
    public void userApiGetMailingById() throws IOException {

        String name = "GetMailingByName";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        MailingRepresentation mailing = keycloakUtils.getMailingUserApi("cb36038e-f139-43e0-a51b-874115fce148", "user-getmailing@test.nl", "test");
        Assert.assertNotNull(mailing);
        Assert.assertEquals(name, mailing.getName());
    }

    @Test
    public void userApiGetMailingByIdUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getMailingUserApi("cb36038e-f139-43e0-a51b-874115fce148", "user-getmailing@test.nl", "wrong");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }
    }

    @Test
    public void userApiGetMailingsByName() throws IOException {

        String name = "GetMailingByName";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        List<MailingRepresentation> mailings = keycloakUtils.getMailingsUserApi(null, name, "user-getmailing@test.nl", "test");
        Assert.assertEquals(1, mailings.size());
        Assert.assertEquals(name, mailings.get(0).getName());
    }


    @Test
    public void userApiGetMailingsByNameUnauthorized()  {

        String name = "GetMailingByName";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getMailingsUserApi(null, name, "user-getmailing@test.nl", "wrong");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }
    }

    @Test
    public void userApiGetMailingsBySearch() throws IOException {

        String search = "GetMailingBySearch";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        List<MailingRepresentation> mailings = keycloakUtils.getMailingsUserApi(search, null, "user-getmailing@test.nl", "test");
        Assert.assertEquals(2, mailings.size());

        for (MailingRepresentation mailing : mailings) {
            Assert.assertTrue(mailing.getName().equals(search + '1') || mailing.getName().equals(search + '2'));
        }
    }

    @Test
    public void userApiGetMailingsBySearchUnauthorized() {

        String search = "GetMailingBySearch";
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getMailingsUserApi(search, null, "user-getmailing@test.nl", "wrong");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }
    }

}
