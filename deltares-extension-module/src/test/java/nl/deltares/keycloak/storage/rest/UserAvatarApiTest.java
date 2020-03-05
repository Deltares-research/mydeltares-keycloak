package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestCategory;
import nl.deltares.keycloak.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Category(IntegrationTestCategory.class)
public class UserAvatarApiTest {

    @Before
    public void setUp()  {
        Assert.assertTrue(KeycloakTestServer.isRunning());
    }

    @Test
    public void adminApiGetAvatar() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        byte[] userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-getavatar@test.nl");
        Assert.assertNotNull(userAvatar);
        Assert.assertTrue(userAvatar.length > 0);

    }

    @Test
    public void adminApiGetAvatarUnauthorized() {

        //No access to admin console
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getUserAvatarAdminApi("admin-getavatar@test.nl");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 403"));
        }
    }

    @Test
    public void adminApiUploadAvatar() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        byte[] userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-uploadavatar@test.nl");
        Assert.assertEquals(0, userAvatar.length);

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar.jpg");
        Assert.assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        int status = keycloakUtils.uploadUserAvatarAdminApi("d09b8604-aa71-4fb5-9d8c-496ed69688a3", avatar);
        Assert.assertEquals(200, status);
        userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-uploadavatar@test.nl");
        Assert.assertNotNull(userAvatar);
        Assert.assertEquals(userAvatar.length, avatar.length());

    }

    @Test
    public void adminApiUploadAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar.jpg");
        Assert.assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarAdminApi("d09b8604-aa71-4fb5-9d8c-496ed69688a3", avatar);
            Assert.fail();
        } catch (IOException e){
            Assert.assertTrue(e.getMessage().contains("Error 403"));
        }

    }

    @Test
    public void adminApiDeleteAvatar() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        byte[] userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-deleteavatar@test.nl");
        Assert.assertNotNull(userAvatar);

        int status = keycloakUtils.deleteUserAvatarAdminApi("b303f008-1ed5-4ca2-8b6a-43ac9644759f");
        Assert.assertEquals(200, status);

    }

    @Test
    public void adminApiDeleteAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();

        try {
            keycloakUtils.deleteUserAvatarAdminApi("b303f008-1ed5-4ca2-8b6a-43ac9644759f");
            Assert.fail();
        } catch (Exception e){
            Assert.assertTrue(e.getMessage().contains("Error 403"));
        }

    }

    @Test
    public void userApiGetAvatar() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        byte[] userAvatar = keycloakUtils.getUserAvatarUserApi("user-getavatar@test.nl", "test");
        Assert.assertNotNull(userAvatar);

    }

    @Test
    public void userApiGetAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getUserAvatarUserApi("user-getavatar@test.nl", "wrong");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }

    }

    @Test
    public void userApiUploadAvatar() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        byte[] userAvatar = keycloakUtils.getUserAvatarUserApi("user-uploadavatar@test.nl", "test");
        Assert.assertEquals(0, userAvatar.length);

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar.jpg");
        Assert.assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        int status = keycloakUtils.uploadUserAvatarUserApi(avatar, "user-uploadavatar", "test");
        Assert.assertEquals(200, status);

        userAvatar = keycloakUtils.getUserAvatarUserApi("user-uploadavatar@test.nl", "test");
        Assert.assertNotNull(userAvatar);
        Assert.assertEquals(userAvatar.length, avatar.length());

    }

    @Test
    public void userApiUploadAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar.jpg");
        Assert.assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarUserApi(avatar, "user-uploadavatar", "wrong");
            Assert.fail();
        } catch (IOException e){
            Assert.assertTrue(e.getMessage().contains("Error 401"));
        }

    }

    @Test
    public void userApiDeleterAvatar() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        byte[] userAvatar = keycloakUtils.getUserAvatarUserApi("user-deleteavatar", "test");
        Assert.assertNotNull(userAvatar);

        int status = keycloakUtils.deleteUserAvatarUserApi("user-deleteavatar", "test");
        Assert.assertEquals(200, status);

    }

    @Test
    public void userApiDeleterAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.deleteUserAvatarUserApi("user-deleteavatar", "wrong");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 401"));
        }

    }

    @Test
    public void adminApiUploadTooLarge() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar-large.jpg");
        Assert.assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarAdminApi("d09b8604-aa71-4fb5-9d8c-496ed69688a3", avatar);
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 413"));
        }
    }

    @Test
    public void userApiUploadTooLarge() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar-large.jpg");
        Assert.assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarUserApi(avatar, "user-uploadavatar", "test");
            Assert.fail();
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage().startsWith("Error 413"));
        }

    }
}
