package nl.deltares.keycloak.storage.rest;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.HttpHeaders;
import nl.deltares.keycloak.storage.jpa.Avatar;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.parseContentType;

abstract class AbstractAvatarResource {
    private static final Logger LOG = Logger.getLogger(AbstractAvatarResource.class);
    static int MAX_SIZE; //bytes
    static int MAX_SIZE_KB = 65; //bytes
    final Properties properties;
    final KeycloakSession session;
    final HttpHeaders headers;
    final AdminPermissionEvaluator realmAuth;
    final RealmModel realm;
    final HttpRequest request;

    AbstractAvatarResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
        this.headers = session.getContext().getRequestHeaders();
        this.request = session.getContext().getHttpRequest();
        realm = session.getContext().getRealm();
        if (properties != null) {
            MAX_SIZE_KB = Integer.parseInt(properties.getOrDefault("avatar_maxsize_kb", MAX_SIZE_KB).toString());
        }
        MAX_SIZE = MAX_SIZE_KB * 1024;

        Auth auth = getAuth(headers, session);
        assert auth != null;
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, auth.getRealm(), adminAuth);
    }

    void setAvatarImage(String realmId, String userId, FileUpload input) throws MaxSizeExceededException, IOException {

        String contentType = parseContentType(input.contentType());
        InputStream imageInputStream = new FileInputStream(input.uploadedFile().toFile());
        if (imageInputStream.available() == 0) {
            return; //save pressed when no image selected
        }
        Avatar avatar = getAvatarEntity(realmId, userId);
        if (avatar == null) {
            avatar = new Avatar();
            avatar.setId(KeycloakModelUtils.generateId());
            avatar.setRealmId(realmId);
            avatar.setUserId(userId);
            LOG.info("Adding avatar for user: " + userId);
        } else {
            LOG.info("Updating avatar for user: " + userId);
        }
        avatar.setContentType(contentType);
        avatar.setAvatar(readAllBytes(imageInputStream));
        getEntityManager().persist(avatar);
    }

    void deleteAvatarImage(String realmId, String userId) {
        Avatar avatar = getAvatarEntity(realmId, userId);
        if (avatar == null) {
            LOG.info("No avatar exists for user " + userId);
            return;
        }
        LOG.info("Deleting avatar of user " + userId);
        getEntityManager().remove(avatar);
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    Avatar getAvatarEntity(String realmId, String userId) {
        List<Avatar> resultList = getEntityManager().createNamedQuery("findAvatarByUserAndRealm", Avatar.class)
                .setParameter("userId", userId)
                .setParameter("realmId", realmId)
                .getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    private byte[] readAllBytes(InputStream input) throws IOException, MaxSizeExceededException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];
        int maxRead = 0;
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            maxRead += nRead;
            if (maxRead > MAX_SIZE) {
                throw new MaxSizeExceededException("Upload size exceeds maximum allowed size of " + MAX_SIZE_KB + " (Kb)");
            }
            buffer.write(data, 0, nRead);

        }

        return buffer.toByteArray();
    }

}
