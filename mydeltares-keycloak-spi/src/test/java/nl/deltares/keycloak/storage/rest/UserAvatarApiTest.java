package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestSuite;
import nl.deltares.keycloak.utils.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTestCategory")
public class UserAvatarApiTest {

    @BeforeAll
    public static void startUp() throws Throwable {

        if (!IntegrationTestSuite.isKeycloakRunning()) {
            IntegrationTestSuite.startKeyCloak();
        }
        assertTrue(IntegrationTestSuite.isKeycloakRunning());
    }

    @Test
    public void adminApiGetAvatar() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        String id = keycloakUtils.createUser("admin", "getavatar", "admin-getavatar", "admin-getavatar@test.nl");
        final Path image = Paths.get( "src", "test", "resources", "test", "avatar.jpg");
        keycloakUtils.uploadUserAvatarAdminApi(id, image.toFile());
        byte[] userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-getavatar@test.nl");
        assertNotNull(userAvatar);
        assertEquals(Files.readAllBytes(image), userAvatar.length );

    }

    @Test
    public void adminApiGetAvatarUnauthorized() {

        //No access to admin console
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getUserAvatarAdminApi("admin-getavatar@test.nl");
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Error 403"));
        }
    }

    @Test
    public void adminApiUploadAvatar() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        byte[] userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-uploadavatar@test.nl");
        assertEquals(0, userAvatar.length);

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/" +
                "avatar.jpg");
        assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        int status = keycloakUtils.uploadUserAvatarAdminApi("d09b8604-aa71-4fb5-9d8c-496ed69688a3", avatar);
        assertEquals(200, status);
        userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-uploadavatar@test.nl");
        assertNotNull(userAvatar);
        assertEquals(userAvatar.length, avatar.length());

    }

    @Test
    public void adminApiUploadAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar.jpg");
        assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarAdminApi("d09b8604-aa71-4fb5-9d8c-496ed69688a3", avatar);
            fail();
        } catch (IOException e){
            assertTrue(e.getMessage().contains("Error 403"));
        }

    }

    @Test
    public void adminApiDeleteAvatar() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        byte[] userAvatar = keycloakUtils.getUserAvatarAdminApi("admin-deleteavatar@test.nl");
        assertNotNull(userAvatar);

        int status = keycloakUtils.deleteUserAvatarAdminApi("b303f008-1ed5-4ca2-8b6a-43ac9644759f");
        assertEquals(200, status);

    }

    @Test
    public void adminApiDeleteAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();

        try {
            keycloakUtils.deleteUserAvatarAdminApi("b303f008-1ed5-4ca2-8b6a-43ac9644759f");
            fail();
        } catch (Exception e){
            assertTrue(e.getMessage().contains("Error 403"));
        }

    }

    @Test
    public void userApiGetAvatar() throws IOException {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        byte[] userAvatar = keycloakUtils.getUserAvatarUserApi("user-getavatar@test.nl", "test");
        assertNotNull(userAvatar);

    }

    @Test
    public void userApiGetAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.getUserAvatarUserApi("user-getavatar@test.nl", "wrong");
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Error 401"));
        }

    }

    @Test
    public void userApiUploadAvatar() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        byte[] userAvatar = keycloakUtils.getUserAvatarUserApi("user-uploadavatar@test.nl", "test");
        assertEquals(0, userAvatar.length);

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar.jpg");
        assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        int status = keycloakUtils.uploadUserAvatarUserApi(avatar, "user-uploadavatar", "test");
        assertEquals(200, status);

        userAvatar = keycloakUtils.getUserAvatarUserApi("user-uploadavatar@test.nl", "test");
        assertNotNull(userAvatar);
        assertEquals(userAvatar.length, avatar.length());

    }

    @Test
    public void userApiUploadAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar.jpg");
        assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarUserApi(avatar, "user-uploadavatar", "wrong");
            fail();
        } catch (IOException e){
            assertTrue(e.getMessage().contains("Error 401"));
        }

    }

    @Test
    public void userApiDeleterAvatar() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        byte[] userAvatar = keycloakUtils.getUserAvatarUserApi("user-deleteavatar", "test");
        assertNotNull(userAvatar);

        int status = keycloakUtils.deleteUserAvatarUserApi("user-deleteavatar", "test");
        assertEquals(200, status);

    }

    @Test
    public void userApiDeleterAvatarUnauthorized() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();
        try {
            keycloakUtils.deleteUserAvatarUserApi("user-deleteavatar", "wrong");
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Error 401"));
        }

    }

    @Test
    public void adminApiUploadTooLarge() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar-large.jpg");
        assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarAdminApi("d09b8604-aa71-4fb5-9d8c-496ed69688a3", avatar);
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Error 413"));
        }
    }

    @Test
    public void userApiUploadTooLarge() {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getUserKeycloakUtils();

        URL keycloakResource = KeycloakTestServer.class.getClassLoader().getResource("testdata/avatar-large.jpg");
        assertNotNull(keycloakResource);
        File avatar = new File(keycloakResource.getFile());
        try {
            keycloakUtils.uploadUserAvatarUserApi(avatar, "user-uploadavatar", "test");
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().startsWith("Error 413"));
        }

    }


    @Test
    public void adminApiCascadeDeleteOfUser(){
        //todo
    }
}
