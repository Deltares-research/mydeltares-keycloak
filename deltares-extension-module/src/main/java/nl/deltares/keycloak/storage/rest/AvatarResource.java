package nl.deltares.keycloak.storage.rest;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.resources.RealmsResource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.authenticateRealmRequest;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.resolveAuthentication;

public class AvatarResource extends AbstractAvatarResource {

    private static final String STATE_CHECKER_ATTRIBUTE = "state_checker";
    private static final String STATE_CHECKER_PARAMETER = "stateChecker";

    private AuthenticationManager.AuthResult authResult;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    public AvatarResource(KeycloakSession session, Properties properties) {
        super(session, properties);
    }

    public void init(){
        ResteasyProviderFactory.getInstance().injectProperties(this);
        authResult = resolveAuthentication(session);
        if (authResult == null) {
            //this is when user accesses API via openid request and not GUI
            AppAuthManager authManager = new AppAuthManager();
            Auth auth = authenticateRealmRequest(authManager, httpHeaders, session, clientConnection);
            authResult = new AuthenticationManager.AuthResult(auth.getUser(), auth.getSession(), auth.getToken());
        }
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
        return Response.ok(getAvatarImage(realmId, userId)).build();
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
            InputStream imageInputStream = input.getFormDataPart(AVATAR_IMAGE_PARAMETER, InputStream.class, null);
            String userId = authResult.getUser().getId();
            int image_size = imageInputStream.available();
            if (image_size == 0){
                deleteAvatarImage(realmName, userId);
            } else {
                setAvatarImage(realmName, userId, imageInputStream);
            }

            return Response.seeOther(RealmsResource.accountUrl(session.getContext().getUri().getBaseUriBuilder()).build(realmName)).build();
        } catch (MaxSizeExceededException e){
            return Response.status(Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode(), e.getMessage()).build();
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

            if (uriInfo.getQueryParameters().containsKey("account")) {
                return Response.seeOther(RealmsResource.accountUrl(session.getContext().getUri().getBaseUriBuilder()).build(realmName)).build();
            }

            return Response.ok().build();

        } catch (Exception ex) {
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
