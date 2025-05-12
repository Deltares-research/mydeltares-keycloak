package nl.deltares.keycloak.storage.rest;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import nl.deltares.keycloak.storage.jpa.Avatar;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
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

import static nl.deltares.keycloak.storage.rest.ResourceUtils.*;

public class AvatarResource {

    private static final Logger logger = Logger.getLogger(AvatarResource.class);
    static int MAX_SIZE; //bytes
    static int MAX_SIZE_KB = 65; //bytes
    final Properties properties;
    final KeycloakSession session;
    final HttpHeaders headers;
    final AdminPermissionEvaluator realmAuth;
    final RealmModel realm;
    final HttpRequest request;


    AvatarResource(KeycloakSession session, Properties properties) {
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

    @GET
    @Path("/hello")
    public Response sayHello() {
        return Response.ok("hello").build();
    }

    @GET
    @Path("/{user_id}")
    @Produces({"image/png", "image/jpeg", "image/gif"})
    public Response downloadUserAvatarImage(@PathParam("user_id") String userId) {
        try {
            canViewUsers();
            UserModel user = session.users().getUserById(realm, userId);
            if (user == null) throw new NotFoundException("User not found for id " + userId);
            Avatar avatar = getAvatarEntity(realm.getId(), userId);
            if (avatar == null) {
//                logger.info("No avatar exists for user " + userId);
                return Response.status(Response.Status.NO_CONTENT).build();
            }

            return Response.ok(avatar.getAvatar(), avatar.getContentType())
                    .header("Content-Disposition", "inline; filename = \"" + user.getUsername() + '.' + contentTypeToExtension(avatar.getContentType()) + '\"')
                    .build();

        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("error getting user avatar", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces({"image/png", "image/jpeg", "image/gif"})
    public Response getAvatar(@Context UriInfo uriInfo) {

        canViewUsers();

        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        UserModel user;
        if (queryParameters.containsKey("username")) {
            final String username = queryParameters.get("username").get(0);
            user = session.users().getUserByUsername(realm, username);
            if (user == null) throw new NotFoundException("User not found for username " + username);
        } else if (queryParameters.containsKey("email")) {
            final String email = queryParameters.get("email").get(0);
            user = session.users().getUserByEmail(realm, email);
            if (user == null) throw new NotFoundException("User not found for email " + email);
        } else {
            throw new IllegalArgumentException("no filter parameters defined!");
        }

        Avatar avatar = getAvatarEntity(realm.getId(), user.getId());
        if (avatar == null) {
            logger.info("No avatar exists for user " + user.getId());
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(avatar.getAvatar(), avatar.getContentType())
                .header("Content-Disposition", "inline; filename = \"" + user.getUsername() + '.' + contentTypeToExtension(avatar.getContentType()) + '\"')
                .build();
    }

    @POST
    @Path("/{user_id}")
    @Consumes("multipart/form-data")
    public Response uploadUserAvatarImage(@PathParam("user_id") String userId, @RestForm("image") FileUpload file) {

        try {
            canManageUsers();
            if (file == null) {
                throw new IllegalArgumentException("Missing image");
            }
            UserModel user = session.users().getUserById(realm, userId);
            if (user == null) throw new NotFoundException("User not found for id " + userId);
            setAvatarImage(realm.getId(), userId, file);
        } catch (MaxSizeExceededException e) {
            return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).entity(e.getMessage()).build();
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("error saving user avatar", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().type(MediaType.TEXT_PLAIN).build();
    }

    @DELETE
    @Path("/{user_id}")
    public Response deleteUserAvatarImage(@PathParam("user_id") String userId) {
        try {
            canManageUsers();

            deleteAvatarImage(realm.getId(), userId);

        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("error deleting user avatar", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().type(MediaType.TEXT_PLAIN).build();
    }


    private void canViewUsers() {
        if (realmAuth == null) {
            throw new NotAuthorizedException("User not authorized!");
        }
        if (!realmAuth.users().canView()) {
            logger.info("user does not have permission to view users");
            throw new ForbiddenException("user does not have permission to view users");
        }
    }

    private void canManageUsers() {
        if (realmAuth == null) {
            throw new NotAuthorizedException("User not authorized!");
        }

        if (!realmAuth.users().canManage()) {
            logger.info("user does not have permission to manage users");
            throw new ForbiddenException("user does not have permission to manage users");
        }
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
            logger.info("Adding avatar for user: " + userId);
        } else {
            logger.info("Updating avatar for user: " + userId);
        }
        avatar.setContentType(contentType);
        avatar.setAvatar(readAllBytes(imageInputStream));
        getEntityManager().persist(avatar);
    }

    void deleteAvatarImage(String realmId, String userId) {
        Avatar avatar = getAvatarEntity(realmId, userId);
        if (avatar == null) {
            logger.info("No avatar exists for user " + userId);
            return;
        }
        logger.info("Deleting avatar of user " + userId);
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
