package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Avatar;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.contentTypeToExtension;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;

public class AvatarAdminResource extends AbstractAvatarResource {
    private static final Logger logger = Logger.getLogger(AvatarAdminResource.class);

    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    private AdminAuth adminAuth;
    //Realm from request path
    private RealmModel callerRealm;

    AvatarAdminResource(KeycloakSession session, Properties properties) {
        super(session, properties);
    }

    public void init() {
        RealmModel realm = session.getContext().getRealm();
        if (realm == null) throw new NotFoundException("Realm not found.");
        Auth auth = getAuth(httpHeaders, session, clientConnection);
        adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);

        callerRealm = ResourceUtils.getRealmFromPath(session);
    }

    @GET
    @Path("/{user_id}")
    @Produces({"image/png", "image/jpeg", "image/gif"})
    public Response downloadUserAvatarImage(@PathParam("user_id") String userId) {
        try {
            canViewUsers();
            Avatar avatar = getAvatarEntity(callerRealm.getId(), userId);
            if (avatar == null){
                logger.info("No avatar exists for user " + userId);
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            UserModel user = session.users().getUserById(userId, callerRealm);
            if (user == null) throw new NotFoundException("User not found for id " + userId);

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

    @POST
    @NoCache
    @Path("/{user_id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadUserAvatarImage(@PathParam("user_id") String userId, MultipartFormDataInput input) {
        try {
            if (adminAuth == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            canManageUsers();

            setAvatarImage(callerRealm.getId(), userId, input);
        } catch (MaxSizeExceededException e) {
            return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE).entity(e.getMessage()).build();
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("error saving user avatar", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @NoCache
    @Path("/{user_id}")
    public Response deleteUserAvatarImage(@PathParam("user_id") String userId) {
        try {
            if (adminAuth == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            canManageUsers();

            deleteAvatarImage(callerRealm.getId(), userId);

        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("error deleting user avatar", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }


    @GET
    @NoCache
    @Produces({"image/png", "image/jpeg", "image/gif"})
    public Response getAvatar(@QueryParam("username") String username, @QueryParam("email") String email) {

        if (adminAuth == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        canViewUsers();

        String realmId = callerRealm.getId();
        UserModel user;
        if (username != null) {
             user = session.users().getUserByUsername(username, callerRealm);
            if (user == null) throw new NotFoundException("User not found for username " + username);
        } else if (email != null) {
            user = session.users().getUserByEmail(email, callerRealm);
            if (user == null) throw new NotFoundException("User not found for email " + email);
        } else {
            throw new IllegalArgumentException("no filter parameters defined!");
        }

        Avatar avatar = getAvatarEntity(realmId, user.getId());
        if (avatar == null){
            logger.info("No avatar exists for user " + user.getId());
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(avatar.getAvatar(), avatar.getContentType())
                .header("Content-Disposition", "inline; filename = \"" + user.getUsername() + '.' + contentTypeToExtension(avatar.getContentType()) + '\"')
                .build();
    }

    private void canViewUsers() {
        if (!realmAuth.users().canView()) {
            logger.info("user does not have permission to view users");
            throw new ForbiddenException("user does not have permission to view users");
        }
    }

    private void canManageUsers() {
        if (!realmAuth.users().canManage()) {
            logger.info("user does not have permission to manage users");
            throw new ForbiddenException("user does not have permission to manage users");
        }
    }
}
