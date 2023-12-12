package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.IntegrationTestSuite;
import nl.deltares.keycloak.utils.KeycloakTestServer;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
    public void SayHello() throws IOException {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        final String s = keycloakUtils.sayHello();
        assertEquals("hello", s);
    }
    @Test
    public void GetAvatar() throws Exception {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        String id = keycloakUtils.getOrCreateUser("admin", "getavatar", "getavatar", "getavatar@test.nl");
        final Path image = Paths.get("src", "test", "resources", "testdata", "avatar.jpg");
        keycloakUtils.uploadUserAvatarApi(id, image.toFile());
        byte[] userAvatar = keycloakUtils.getUserAvatarApiByEmail("getavatar@test.nl");
        assertNotNull(userAvatar);
        assertEquals(image.toFile().length(), userAvatar.length);

    }

    @Test
    public void UploadAvatar() throws Exception {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        String id = keycloakUtils.getOrCreateUser("admin", "uploadavatar", "uploadavatar", "uploadavatar@test.nl");
//        byte[] userAvatar = keycloakUtils.getUserAvatarApiByEmail("uploadavatar@test.nl");
//        assertEquals(0, userAvatar.length, "Avatar already exists for user 'uploadavatar'");

        final Path image = Paths.get("src", "test", "resources", "testdata", "avatar.jpg");

        int status = keycloakUtils.uploadUserAvatarApi(id, image.toFile());
        assertEquals(200, status);
        byte[] userAvatar = keycloakUtils.getUserAvatarApiById(id);
        assertNotNull(userAvatar);
        assertEquals(image.toFile().length(), userAvatar.length, "expected image file length " + image.toFile().length());

    }

    @Test
    public void UploadAvatarUnauthorized() throws Exception {

        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        String id = keycloakUtils.getOrCreateUser("admin", "unauthorized", "unauthorized", "unauthorized@test.nl");

        KeycloakUtilsImpl viewerKeycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();

        final Path image = Paths.get("src", "test", "resources", "testdata", "avatar.jpg");
        final int i = viewerKeycloakUtils.uploadUserAvatarApi(id, image.toFile());
        assertEquals(403, i);

    }

    @Test
    public void DeleteAvatar() throws Exception {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();
        String id = keycloakUtils.getOrCreateUser("admin", "deleteavatar", "deleteavatar", "deleteavatar@test.nl");

        final Path image = Paths.get("src", "test", "resources", "testdata", "avatar.jpg");
        final int i = keycloakUtils.uploadUserAvatarApi(id, image.toFile());
        assertEquals(200, i);

        byte[] userAvatar = keycloakUtils.getUserAvatarApiById(id);
        assertNotNull(userAvatar);
        assertEquals(userAvatar.length, image.toFile().length());

        int status = keycloakUtils.deleteUserAvatarApi(id);
        assertEquals(200, status);

        userAvatar = keycloakUtils.getUserAvatarApiById(id);
        assertEquals(0, userAvatar.length);

    }

    @Test
    public void DeleteAvatarUnauthorized() {

        KeycloakUtilsImpl viewerKeycloakUtils = KeycloakTestServer.getViewerKeycloakUtils();

        try {
            viewerKeycloakUtils.deleteUserAvatarApi("dummy");
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Error 403"));
        }

    }

    @Test
    public void UploadTooLarge() throws Exception {
        KeycloakUtilsImpl keycloakUtils = KeycloakTestServer.getAdminKeycloakUtils();

        String id = keycloakUtils.getOrCreateUser("admin", "avatartoolarge", "avatartoolarge", "avatartoolarge@test.nl");
        byte[] userAvatar = keycloakUtils.getUserAvatarApiByEmail("avatartoolarge@test.nl");
        assertEquals(0, userAvatar.length);

        final Path image = Paths.get("src", "test", "resources", "testdata", "avatar-large.jpg");

        final int i = keycloakUtils.uploadUserAvatarApi(id, image.toFile());
        assertEquals(413, i);

    }

}
