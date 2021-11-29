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
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static nl.deltares.keycloak.storage.rest.TestUtils.newUserRepresentation;

@Category(IntegrationTestCategory.class)
public class UsersApiTest {

    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() {

        System.setProperty("csv_prefix", name.getMethodName());
        Assert.assertTrue(KeycloakTestServer.isRunning());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Test
    public void adminApiGetDisabledUsersUnauthorized() {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try (StringWriter writer = new StringWriter()){
            keycloakUtils.exportDisabledUsers(writer);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertEquals("Error 403", e.getMessage().substring(0, "Error 403".length()));
        }

    }

    @Test
    public void adminApiGetDisabledUsers() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        UserRepresentation user = newUserRepresentation("disabled-user1@test.nl");
        keycloakUtils.createUser(user);

        try(StringWriter writer = new StringWriter()) {
            keycloakUtils.exportDisabledUsers(writer);
            Assert.assertFalse(writer.toString().contains("disabled-user1@test.nl"));
        }

        user = keycloakUtils.getUserByEmail("disabled-user1@test.nl");
        user.setEnabled(false);

        keycloakUtils.updateUser(user);

        try(StringWriter writer = new StringWriter()) {
            keycloakUtils.exportDisabledUsers(writer);
            Assert.assertTrue(writer.toString().contains("disabled-user1@test.nl"));
        }
    }

    @Test
    public void adminApiTestUserDisabledEventListener() throws IOException, ParseException {

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        //Check that user is member of 2 groups
        UserRepresentation user = keycloakUtils.getUserByEmail("user.withgroups@test.nl");

        final List<GroupRepresentation> groups = keycloakUtils.getGroups();
        Assert.assertEquals(2 , groups.size());
        for (GroupRepresentation group : groups) {
            final List<UserRepresentation> members = keycloakUtils.getGroupMember(group.getId());
            boolean[] contains = {false} ;
            members.forEach(userRepresentation -> {if (userRepresentation.getEmail().equals("user.withgroups@test.nl")) contains[0] = true;});
            Assert.assertTrue(String.format("Group %s does not contain member", group.getName()), contains[0]);
        }

        //Disabling user will trigger UPDATE event
        user.setEnabled(false);
        keycloakUtils.updateUser(user);

        //Update has removed user from all groups
        for (GroupRepresentation group : groups) {
            final List<UserRepresentation> members = keycloakUtils.getGroupMember(group.getId());
            boolean[] contains = {false} ;
            members.forEach(userRepresentation -> {if (userRepresentation.getEmail().equals("user.withgroups@test.nl")) contains[0] = true;});
            Assert.assertFalse(String.format("Group %s should not contain member", group.getName()), contains[0]);
        }

        //Attribute disabledTime added to user.
        user = keycloakUtils.getUserByEmail("user.withgroups@test.nl");
        List<String> disabledTime = user.getAttributes().get("disabledTime");
        Assert.assertEquals(1, disabledTime.size());
        final Date disabledDate = simpleDateFormat.parse(disabledTime.get(0));

        Assert.assertEquals(System.currentTimeMillis(), disabledDate.getTime(), 1000); //compare timestamps. should only differ by 1 second

        //Re-enable the user
        user.setEnabled(true);
        keycloakUtils.updateUser(user);

        //Attribute disableTime is removed
        user = keycloakUtils.getUserByEmail("user.withgroups@test.nl");
        Assert.assertNull(user.getAttributes());

    }

    @Test
    public void adminApiGetDisabledUsersByTime() throws IOException {

        final long startTest = System.currentTimeMillis();
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        UserRepresentation user = newUserRepresentation("disabled-user2@test.nl");
        final HashMap<String, List<String>> attributes = new HashMap<>();
        attributes.put("disabledTime", Collections.singletonList(simpleDateFormat.format(new Date(startTest))));
        user.setAttributes(attributes);
        user.setEnabled(false);
        keycloakUtils.createUser(user);


        //Get users with disabled time after now - 5sec
        try(StringWriter writer = new StringWriter()) {
            keycloakUtils.exportDisabledUsers(writer, startTest - 5000, null);
            Assert.assertTrue(writer.toString().contains("disabled-user2@test.nl"));
        }

        //Get users with disabled time after now + 5sec
        try(StringWriter writer = new StringWriter()) {
            keycloakUtils.exportDisabledUsers(writer, startTest + 5000, null);
            Assert.assertFalse(writer.toString().contains("disabled-user2@test.nl"));
        }

        //Get users with disabled time before now + 5sec
        try(StringWriter writer = new StringWriter()) {
            keycloakUtils.exportDisabledUsers(writer, null,  startTest + 5000);
            Assert.assertTrue(writer.toString().contains("disabled-user2@test.nl"));
        }

        //Get users with disabled time before now - 5sec
        try(StringWriter writer = new StringWriter()) {
            keycloakUtils.exportDisabledUsers(writer, null,  startTest - 5000);
            Assert.assertFalse(writer.toString().contains("disabled-user2@test.nl"));
        }

        //Get users with disabled time between now - 5sec and now + 5sec
        try(StringWriter writer = new StringWriter()) {
            keycloakUtils.exportDisabledUsers(writer, startTest - 5000,  startTest + 5000);
            Assert.assertTrue(writer.toString().contains("disabled-user2@test.nl"));
        }

    }

}
