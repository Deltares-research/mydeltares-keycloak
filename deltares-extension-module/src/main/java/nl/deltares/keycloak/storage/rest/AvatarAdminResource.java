package nl.deltares.keycloak.storage.rest;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.authenticateRealmAdminRequest;

public class AvatarAdminResource extends AbstractAvatarResource {
    private static final Logger logger = Logger.getLogger(AvatarAdminResource.class);

    private AdminPermissionEvaluator realmAuth;
    private AppAuthManager authManager;


    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    private AdminAuth adminAuth;

    public AvatarAdminResource(KeycloakSession session, Properties properties) {
        super(session, properties);
    }

    public void init() {
        RealmModel realm = session.getContext().getRealm();
        if (realm == null) throw new NotFoundException("Realm not found.");
        authManager = new AppAuthManager();

        adminAuth = authenticateRealmAdminRequest(authManager, httpHeaders, session, clientConnection);
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);
    }

    @GET
    @Path("/{user_id}")
    @Produces({"image/png", "image/jpeg", "image/gif"})
    public Response downloadUserAvatarImage(@PathParam("user_id") String userId) {
        try {
            canViewUsers();

            return Response.ok(getAvatarImage(session.getContext().getRealm().getId(), userId)).build();
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

            InputStream imageInputStream = input.getFormDataPart(AVATAR_IMAGE_PARAMETER, InputStream.class, null);
            setAvatarImage(session.getContext().getRealm().getId(), userId, imageInputStream);
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

            deleteAvatarImage(session.getContext().getRealm().getId(), userId);

        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("error deleting user avatar", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().build();
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
