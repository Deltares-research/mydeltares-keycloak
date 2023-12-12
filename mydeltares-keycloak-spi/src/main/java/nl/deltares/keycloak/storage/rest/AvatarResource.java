package nl.deltares.keycloak.storage.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import nl.deltares.keycloak.storage.jpa.Avatar;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.ForbiddenException;

import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.contentTypeToExtension;

public class AvatarResource extends AbstractAvatarResource {

    private static final Logger logger = Logger.getLogger(AvatarResource.class);


    AvatarResource(KeycloakSession session, Properties properties) {
        super(session, properties);
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

}
