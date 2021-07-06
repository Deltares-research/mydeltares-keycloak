package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Avatar;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resources.RealmsResource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Objects;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuthResult;

public class AvatarResource extends AbstractAvatarResource {

    private static final String STATE_CHECKER_ATTRIBUTE = "state_checker";
    private static final String STATE_CHECKER_PARAMETER = "stateChecker";

    private AuthenticationManager.AuthResult authResult;

    @Context
    private HttpHeaders httpHeaders;

    AvatarResource(KeycloakSession session, Properties properties) {
        super(session, properties);
    }

    public void init(){
        ResteasyProviderFactory.getInstance().injectProperties(this);
        authResult = getAuthResult(session, httpHeaders);
    }

    @Path("/admin")
    public AvatarAdminResource admin() {
        AvatarAdminResource service = new AvatarAdminResource(session, super.properties);
        ResteasyProviderFactory.getInstance().injectProperties(service);
        service.init();
        return service;
    }

    @GET
    @Produces({"image/png", "image/jpeg", "image/gif"})
    public Response downloadCurrentUserAvatarImage() {

        if (authResult == null) {
            return badRequest();
        }

        String  userId = authResult.getUser().getId();
        String realmId = authResult.getSession().getRealm().getId();

        Avatar avatar = getAvatarEntity(realmId, userId);
        if (avatar == null){
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(avatar.getAvatar(), avatar.getContentType())
                .header("Content-Disposition", "inline; filename = \"" + authResult.getUser().getUsername() + '.' + ResourceUtils.contentTypeToExtension(avatar.getContentType()) + '\"')
                .build();

    }

    @POST
    @NoCache
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadCurrentUserAvatarImage(MultipartFormDataInput input, @Context UriInfo uriInfo) {

        if (authResult == null) {
            return badRequest();
        }

        if (!isValidStateChecker(input)) {
            return badRequest();
        }
        String realmName = authResult.getSession().getRealm().getId();

        try {
            if (input.getFormDataMap().get("deleteAction") != null){
                Response response = deleteCurrentUserAvatarImage(uriInfo);
                if (response.getStatus() == 500) return response;
            } else {
                String userId = authResult.getUser().getId();
                setAvatarImage(realmName, userId, input);
            }
            UriBuilder builder = RealmsResource.accountUrl(session.getContext().getUri().getBaseUriBuilder());
            return Response.seeOther(builder.build(realmName)).build();
        } catch (MaxSizeExceededException e){
            return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode(), e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @NoCache
    public Response deleteCurrentUserAvatarImage(@Context UriInfo uriInfo) {

        if (authResult == null) {
            return badRequest();
        }
        try {
            String realmName = authResult.getSession().getRealm().getId();
            String userId = authResult.getUser().getId();
            deleteAvatarImage(realmName, userId);
            return Response.ok().type(MediaType.TEXT_PLAIN).build();
        }  catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    private boolean isValidStateChecker(MultipartFormDataInput input) {

        try {
            String actualStateChecker = input.getFormDataPart(STATE_CHECKER_PARAMETER, String.class, null);
            String requiredStateChecker = (String) session.getAttribute(STATE_CHECKER_ATTRIBUTE);

            return Objects.equals(requiredStateChecker, actualStateChecker);
        } catch (Exception ex) {
            return false;
        }
    }

}
