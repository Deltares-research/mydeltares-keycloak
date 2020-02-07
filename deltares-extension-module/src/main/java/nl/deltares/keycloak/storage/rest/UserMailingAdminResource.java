package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.UserMailing;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;
import static nl.deltares.keycloak.storage.rest.UserMailingResource.getUserMailing;
import static nl.deltares.keycloak.storage.rest.UserMailingResource.insertUserMailing;

public class UserMailingAdminResource {
    private static final String DATA_PARAMETER = "data";

    private static final Logger logger = Logger.getLogger(UserMailingAdminResource.class);
    private final KeycloakSession session;

    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    private AdminAuth adminAuth;
    //Realm from request path
    private RealmModel callerRealm;

     UserMailingAdminResource(KeycloakSession session) {
        this.session = session;
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

    @POST
    @Path("/import/{mailing_id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadUserMailings(@PathParam("mailing_id") String mailingId, MultipartFormDataInput input){

        try {
            if (adminAuth == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            canManageUsers();

            importUserMailings(callerRealm.getId(), mailingId, input);
        } catch (ForbiddenException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.error("error importing user mailings", e);
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }

    private void importUserMailings(String realmId, String mailingId, MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> formDataMap = input.getFormDataMap();
        List<InputPart> inputParts = formDataMap.get(DATA_PARAMETER);
        if (inputParts == null || inputParts.isEmpty()){
            throw new IllegalArgumentException("Missing data file");
        }
        InputPart inputPart = inputParts.get(0);
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        if (inputStream.available() == 0){
            return; //save pressed when no image selected
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String userId;
            while ((userId = reader.readLine()) != null) {
                UserMailing userMailing = getUserMailing(session, realmId, userId.trim(), mailingId);
                if (userMailing == null) {
                    UserMailingRepresentation rep = new UserMailingRepresentation();
                    rep.setUserId(userId);
                    rep.setRealmId(realmId);
                    rep.setMailingId(mailingId);
                    rep.setId(KeycloakModelUtils.generateId());
                    insertUserMailing(session, rep);
                } //skip existing
            }
        }
    }

    private void canManageUsers() {
        if (!realmAuth.users().canManage()) {
            logger.info("user does not have permission to manage users");
            throw new ForbiddenException("user does not have permission to manage users");
        }
    }

}
