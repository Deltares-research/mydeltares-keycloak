package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestCategory;
import nl.deltares.keycloak.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestName;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Category(IntegrationTestCategory.class)
public class UserDetailsApiTest {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp(){

        System.setProperty("csv_prefix", name.getMethodName());
        Assert.assertTrue(KeycloakTestServer.isRunning());
    }

    @Test
    public void adminApiGetUserDetails() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        UserRepresentation user = keycloakUtils.getUserDetailsAdminApi("userdetails@test.nl");
        Assert.assertNotNull(user);
        Assert.assertEquals("64ab098f-adb7-4abf-8711-1c3d63de45cb", user.getId());
        Assert.assertEquals("userdetails", user.getUsername());
        Assert.assertEquals("userdetails@test.nl", user.getEmail());
        Assert.assertEquals("user", user.getFirstName());
        Assert.assertEquals("details", user.getLastName());

        Map<String, List<String>> attributes = user.getAttributes();
        String[] keys = {"attr1", "attr2", "attr3"};
        String[] values = {"val1", "val2", "val3"};
        for (int i = 0; i < keys.length; i++) {
            Assert.assertEquals(values[i], attributes.get(keys[i]).get(0));
        }
    }

    @Test
    public void adminApiGetUserDetailsUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        try {
            keycloakUtils.getUserDetailsAdminApi("userdetails@test.nl");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }

    }

    @Test
    public void adminApiUpdateUserDetails() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        UserRepresentation details = keycloakUtils.getUserDetailsAdminApi("admin-userdetails@test.nl");
        Assert.assertNotNull(details);
        Assert.assertNotEquals("updated", details.getFirstName());
        Assert.assertNotEquals("updated", details.getLastName());
        details.setLastName("updated");
        details.setFirstName("updated");
        Map<String, List<String>> attributes = details.getAttributes();
        for (String s : attributes.keySet()) {
            Assert.assertNotEquals("updated", attributes.get(s));
            attributes.put(s, Collections.singletonList("updated"));
        }
        Assert.assertNull(attributes.put("new", Collections.singletonList("new")));

        int status = keycloakUtils.updateUserDetailsAdminApi(details);
        Assert.assertEquals(200, status);

        details = keycloakUtils.getUserDetailsAdminApi("admin-userdetails@test.nl");
        Assert.assertNotNull(details);
        Assert.assertEquals("updated", details.getFirstName());
        Assert.assertEquals("updated", details.getLastName());
        attributes = details.getAttributes();
        boolean foundNew = false;
        for (String s : attributes.keySet()) {
            String actual = attributes.get(s).get(0);
            if (s.equals("new")){
                Assert.assertEquals("new", actual);
                foundNew = true;
            } else {
                Assert.assertEquals("updated", actual);
            }
        }
        Assert.assertTrue(foundNew);

    }

    @Test
    public void adminApiUpdateUserDetailsUnauthorized()  {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail("admin-userdetails@test.nl");
        userRepresentation.setFirstName("fail");
        userRepresentation.setLastName("fail");
        try {
            keycloakUtils.updateUserDetailsAdminApi(userRepresentation);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }

    }

    @Test
    public void userApiGetUserDetails() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        UserRepresentation  user = keycloakUtils.getUserDetailsUserApi("userdetails@test.nl", "test");
        Assert.assertNotNull(user);
        Assert.assertEquals("64ab098f-adb7-4abf-8711-1c3d63de45cb", user.getId());
        Assert.assertEquals("userdetails", user.getUsername());
        Assert.assertEquals("userdetails@test.nl", user.getEmail());
        Assert.assertEquals("user", user.getFirstName());
        Assert.assertEquals("details", user.getLastName());

        Map<String, List<String>> attributes = user.getAttributes();
        String[] keys = {"attr1", "attr2", "attr3"};
        String[] values = {"val1", "val2", "val3"};
        for (int i = 0; i < keys.length; i++) {
            Assert.assertEquals(values[i], attributes.get(keys[i]).get(0));
        }
    }

    @Test
    public void userApiGetUserDetailsUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getUserDetailsUserApi("userdetails@test.nl", "wrong");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }

    }

    @Test
    public void userApiUpdateUserDetails() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        UserRepresentation details = keycloakUtils.getUserDetailsUserApi("user-userdetails@test.nl", "test");
        Assert.assertNotNull(details);
        Assert.assertNotEquals("updated", details.getFirstName());
        Assert.assertNotEquals("updated", details.getLastName());
        details.setLastName("updated");
        details.setFirstName("updated");
        Map<String, List<String>> attributes = details.getAttributes();

        String[] keys = new String[]{"attr1", "attr2", "attr3"};
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Assert.assertNotEquals("updated", attributes.get(keys[i]));
            attributes.put(keys[i], Collections.singletonList("updated"));
        }
        Assert.assertNull(attributes.put("new", Collections.singletonList("new")));

        int status = keycloakUtils.updateUserDetailsUserApi(details, "user-userdetails@test.nl", "test");
        Assert.assertEquals(200, status);

        details = keycloakUtils.getUserDetailsUserApi("user-userdetails@test.nl", "test");
        Assert.assertNotNull(details);
        Assert.assertEquals("updated", details.getFirstName());
        Assert.assertEquals("updated", details.getLastName());

        Map<String, List<String>> updatedAttrs = details.getAttributes();
        for (int i = 0; i < keys.length; i++) {

            String actual = updatedAttrs.get(keys[i]).get(0);
            Assert.assertEquals("updated", actual);
        }
        Assert.assertNotNull(updatedAttrs.get("new"));
        Assert.assertEquals("new", updatedAttrs.get("new").get(0));

    }

    @Test
    public void userApiUpdateUserDetailsUnauthorized()  {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        UserRepresentation rep = new UserRepresentation();
        try {
            keycloakUtils.updateUserDetailsUserApi(rep, "user-userdetails@test.nl", "wrong");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }

    }

    @Test
    public void userApiUpdateUserDetailsUpdateDifferentUser() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        UserRepresentation details = keycloakUtils.getUserDetailsUserApi("userdetails@test.nl", "test");

        try {
            //try uploading to different user
            keycloakUtils.updateUserDetailsUserApi(details, "user-userdetails@test.nl", "test");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Error 400: {\"errorMessage\":\"Not the same user! Found id 64ab098f-adb7-4abf-8711-1c3d63de45cb, expected 548c9d14-afb1-4c8e-8e15-32990835f0f7\"}", e.getMessage());
        }

    }

    @Test
    public void userApiUpdateUserDetailsChangeEmail() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        UserRepresentation details = keycloakUtils.getUserDetailsUserApi("change-email@test.nl", "test");
        details.setEmail("email-changed@test.nl");
        int status = keycloakUtils.updateUserDetailsUserApi(details, "change-email@test.nl", "test");

        Assert.assertEquals(200, status);

        try {
            //this user should no longer exist.
            keycloakUtils.getUserDetailsUserApi("change-email@test.nl", "test");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }

        UserRepresentation details2 = keycloakUtils.getUserDetailsUserApi("email-changed@test.nl", "test");
        Assert.assertNotNull(details2);
        Assert.assertEquals(details.getId(), details2.getId());

    }

    @Test
    public void userApiUpdateUserDetailsChangeEmailAlreadyExists() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        UserRepresentation details = keycloakUtils.getUserDetailsUserApi("change-email2@test.nl", "test");
        details.setEmail("userdetails@test.nl");
        details.setAttributes(null);

        try {
            //cannot change email because the new email already exists.
            keycloakUtils.updateUserDetailsUserApi(details, "change-email2@test.nl", "test");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Error 400: {\"errorMessage\":\"Cannot change email to userdetails@test.nl for user change-email2! This email address already exists!\"}", e.getMessage());
        }

    }

    @Test
    public void userApiUpdateUserDetailsTryChangeUserName() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        UserRepresentation details = keycloakUtils.getUserDetailsUserApi("userdetails@test.nl", "test");
        details.setUsername("notallowed");
        try {
            //not allowed to change username
            keycloakUtils.updateUserDetailsUserApi(details, "userdetails@test.nl", "test");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Error 400: {\"errorMessage\":\"Not allowed to update username! Found username notallowed, expected userdetails\"}", e.getMessage());
        }
    }


}
