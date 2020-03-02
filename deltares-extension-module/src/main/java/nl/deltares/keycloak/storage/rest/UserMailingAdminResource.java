package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.rest.model.ExportUserMailings;
import nl.deltares.keycloak.storage.rest.serializers.ExportCsvSerializer;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.persistence.TypedQuery;
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
import static nl.deltares.keycloak.storage.rest.ResourceUtils.*;
import static nl.deltares.keycloak.storage.rest.UserMailingResource.getUserMailing;
import static nl.deltares.keycloak.storage.rest.UserMailingResource.insertUserMailing;

public class UserMailingAdminResource {

    private static String CSV_SEPARATOR = ";";
    private static int MAX_RESULTS = 100;

    private static final String DATA_PARAMETER = "data";

    private static final Logger logger = Logger.getLogger(UserMailingAdminResource.class);
    private final KeycloakSession session;

    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    //Realm from request path
    private RealmModel callerRealm;

     UserMailingAdminResource(KeycloakSession session, Properties properties) {
        this.session = session;

         if (properties != null){
             MAX_RESULTS = Integer.parseInt(properties.getOrDefault("max_query_results", 100).toString());
             CSV_SEPARATOR = (String) properties.getOrDefault("csv_separator", ";");
         }

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
        UserProvider userProvider = session.userStorageManager();
        ExportCsvSerializer serializer = new ExportCsvSerializer();

        Mailing mailing = getMailingById(session, realmId, mailingId);
        if (mailing == null){
            return Response.status(Response.Status.NO_CONTENT).entity("no mailing for id: " + mailingId).build();
        }
        TypedQuery<UserMailing> query = getEntityManager(session).createNamedQuery("allUserMailingsByMailingAndRealm", UserMailing.class);
        query.setParameter("realmId", realmId);
        query.setParameter("mailingId", mailingId);

        ExportUserMailings content = new ExportUserMailings(userProvider, callerRealm, query, mailing.getName());
        content.setMaxResults(MAX_RESULTS);
        content.setSeparator(CSV_SEPARATOR);
        try {
            return Response.
                    ok(getStreamingOutput(content, serializer)).
                    type("text/csv").
                    build();
        } catch (Exception e){
            return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
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
