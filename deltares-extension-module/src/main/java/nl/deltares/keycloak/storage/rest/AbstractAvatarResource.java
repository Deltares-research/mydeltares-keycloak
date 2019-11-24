package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Avatar;
import org.jboss.logging.Logger;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.utils.KeycloakModelUtils;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.List;
import java.util.Properties;

abstract class AbstractAvatarResource {
    private static final Logger LOG = Logger.getLogger(AbstractAvatarResource.class);
    static final String AVATAR_IMAGE_PARAMETER = "image";
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

    StreamingOutput getAvatarImage(String realmId, String userId) {

        Avatar avatar = getAvatar(realmId, userId);
        InputStream in;
        if (avatar == null) {
            LOG.info("No avatar exists for user " + userId);
            in = new ByteArrayInputStream(new byte[0]);
        } else {
            in = new ByteArrayInputStream(avatar.getAvatar());
        }
        return output -> copyStream(in, output);
    }

    void setAvatarImage(String realmId, String userId, InputStream input) throws MaxSizeExceededException, IOException {

        Avatar avatar = getAvatar(realmId, userId);
        if (avatar == null) {
            avatar = new Avatar();
            avatar.setId(KeycloakModelUtils.generateId());
            avatar.setRealmId(realmId);
            avatar.setUserId(userId);
            LOG.info("Adding avatar for user: " + userId);
        } else {
            LOG.info("Updating avatar for user: " + userId);
        }

        avatar.setAvatar(readAllBytes(input));
        getEntityManager().persist(avatar);
    }

    void deleteAvatarImage(String realmId, String userId) {
        Avatar avatar = getAvatar(realmId, userId);
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

    private Avatar getAvatar(String realmId, String userId) {
        List<Avatar> resultList = getEntityManager().createNamedQuery("findByUser", Avatar.class)
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
    private void copyStream(InputStream in, OutputStream out) throws IOException {

        byte[] buffer = new byte[16384];

        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        out.flush();
    }

}
