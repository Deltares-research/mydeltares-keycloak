package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.jpa.model.DataRequest;
import nl.deltares.keycloak.storage.jpa.model.DataRequestManager;
import nl.deltares.keycloak.storage.jpa.model.ExportCsvDataRequest;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
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
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.MailingAdminResource.getMailingById;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getStreamingOutput;
import static nl.deltares.keycloak.storage.rest.UserMailingResource.getUserMailing;
import static nl.deltares.keycloak.storage.rest.UserMailingResource.insertUserMailing;

public class UserMailingAdminResource {

    private static final String DATA_PARAMETER = "data";

    private static final Logger logger = Logger.getLogger(UserMailingAdminResource.class);
    private final KeycloakSession session;
    private final Properties properties;

    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    //Realm from request path
    private RealmModel callerRealm;

     UserMailingAdminResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
     }

    public void init() {
        RealmModel realm = session.getContext().getRealm();
        if (realm == null) throw new NotFoundException("Realm not found.");
        Auth auth = getAuth(httpHeaders, session, clientConnection);
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);

        callerRealm = ResourceUtils.getRealmFromPath(session);
    }

    @GET
    @NoCache
    @Path("/export/{mailing_id}")
    @Produces("text/plain")
    public Response downloadUserMailings(final @PathParam("mailing_id") String mailingId) {
        realmAuth.users().requireQuery();
        String realmId = callerRealm.getId();
        Mailing mailing = getMailingById(session, realmId, mailingId);
        if (mailing == null){
            return Response.status(Response.Status.NO_CONTENT).entity("no mailing for id: " + mailingId).build();
        }
        return getExportDataResponse(mailing);

    }

    private Response getExportDataResponse(Mailing mailing) {

        DataRequestManager instance = DataRequestManager.getInstance();
        DataRequest dataRequest = instance.getDataRequest(mailing.getId());
        if (dataRequest == null || dataRequest.getStatus() == DataRequest.STATUS.expired){
            try {
                dataRequest = new ExportCsvDataRequest(mailing, session, callerRealm, properties);
                if (dataRequest.getStatus() == DataRequest.STATUS.pending) {
                    instance.addToQueue(dataRequest);
                }
            } catch (IOException e) {
                return Response.serverError().entity(e.getMessage()).build();
            }
        }
        DataRequest.STATUS status = dataRequest.getStatus();
        if (status == DataRequest.STATUS.available && dataRequest.getDataFile().exists()){
            return Response.
                    ok(getStreamingOutput(dataRequest.getDataFile())).
                    type("text/csv").
                    build();
        } else if (status == DataRequest.STATUS.terminated) {
            instance.removeDataRequest(dataRequest);
            return Response.serverError().entity(dataRequest.getErrorMessage()).build();
        } else {
            return Response.ok(dataRequest.getStatusMessage()).type("text/plain").build();
        }

    }

    @POST
    @Path("/import/{mailing_id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadUserMailings(@PathParam("mailing_id") String mailingId, MultipartFormDataInput input){
        realmAuth.users().requireManage();
        try {
            int written = importUserMailings(callerRealm.getId(), mailingId, input);
            if (written == 0){
                return Response.notModified().build();
            }
        } catch (Exception e) {
            logger.error("error importing user mailings", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok().build();
    }

    private int importUserMailings(String realmId, String mailingId, MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> formDataMap = input.getFormDataMap();
        List<InputPart> inputParts = formDataMap.get(DATA_PARAMETER);
        if (inputParts == null || inputParts.isEmpty()){
            throw new IllegalArgumentException("Missing data file");
        }
        InputPart inputPart = inputParts.get(0);
        InputStream inputStream = inputPart.getBody(InputStream.class, null);
        if (inputStream.available() == 0){
            return 0; //save pressed when no image selected
        }
        int writeCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String userId;
            while ((userId = reader.readLine()) != null) {
                UserMailing userMailing = getUserMailing(session, realmId, userId.trim(), mailingId);
                if (userMailing == null) {
                    UserMailingRepresentation rep = new UserMailingRepresentation();
                    rep.setUserId(userId);
                    rep.setRealmId(realmId);
                    rep.setMailingId(mailingId);
                    rep.setDelivery(Mailing.getPreferredMailingDelivery());
                    rep.setId(KeycloakModelUtils.generateId());
                    insertUserMailing(session, rep);
                    writeCount++;
                } //skip existing
            }
        }
        return writeCount;
    }

}
