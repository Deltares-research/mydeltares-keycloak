package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Avatar;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.KeycloakModelUtils;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

abstract class AbstractAvatarResource {
    private static final Logger LOG = Logger.getLogger(AbstractAvatarResource.class);
    static final String AVATAR_IMAGE_PARAMETER = "image";
    static final String AVATAR_CONTENTTYPE_PARAMETER = "Content-Type";
    static int MAX_SIZE; //bytes
    static int MAX_SIZE_KB = 50; //bytes
    final Properties properties;
    KeycloakSession session;

    AbstractAvatarResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
        if (properties != null){
            MAX_SIZE_KB = Integer.parseInt(properties.getOrDefault("avatar_maxsize_kb", MAX_SIZE_KB).toString());
        }
        MAX_SIZE = MAX_SIZE_KB*1024;

    }

    Response badRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    void setAvatarImage(String realmId, String userId, MultipartFormDataInput input) throws MaxSizeExceededException, IOException {

        Map<String, List<InputPart>> formDataMap = input.getFormDataMap();
        List<InputPart> inputParts = formDataMap.get(AVATAR_IMAGE_PARAMETER);
        if (inputParts == null || inputParts.isEmpty()){
            throw new IllegalArgumentException("Missing image");
        }
        InputPart inputPart = inputParts.get(0);
        String contentType = inputPart.getHeaders().getFirst(AVATAR_CONTENTTYPE_PARAMETER);
        InputStream imageInputStream = inputPart.getBody(InputStream.class, null);

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
        avatar.setContentType(contentType != null ? contentType : URLConnection.guessContentTypeFromStream(imageInputStream));
        avatar.setAvatar(readAllBytes(imageInputStream));
        getEntityManager().persist(avatar);
    }

    void deleteAvatarImage(String realmId, String userId) {
        Avatar avatar = getAvatarEntity(realmId, userId);
        if (avatar == null){
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
            if (maxRead > MAX_SIZE){
                throw new MaxSizeExceededException("Upload size exceeds maximum allowed size of " + MAX_SIZE_KB + " (Kb)");
            }
            buffer.write(data, 0, nRead);

        }

        return buffer.toByteArray();
    }

    static String getExtension(String contentType) {
        return contentType.substring("image/".length());
    }
}
