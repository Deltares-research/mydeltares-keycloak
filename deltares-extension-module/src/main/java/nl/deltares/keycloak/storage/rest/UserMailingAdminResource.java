package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.jpa.model.DataRequestManager;
import nl.deltares.keycloak.storage.rest.model.ExportUserMailings;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.MailingAdminResource.getMailingById;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;
import static nl.deltares.keycloak.storage.rest.UserMailingResource.*;

public class UserMailingAdminResource {

    private static final String DATA_PARAMETER = "data";

    private static final Logger logger = Logger.getLogger(UserMailingAdminResource.class);
    private final KeycloakSession session;
    private final Properties properties;

    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    //Realm from request path
    private RealmModel callerRealm;
    private boolean cacheExport;

    UserMailingAdminResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
     }

    public void init() {
        RealmModel realm = session.getContext().getRealm();
        if (realm == null) throw new NotFoundException("Realm not found.");
        Auth auth = getAuth(httpHeaders, session);
        assert auth != null;
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);
        cacheExport = Boolean.parseBoolean(System.getProperty("cache.export", "true"));

        callerRealm = ResourceUtils.getRealmFromPath(session);
    }


    /**
     * Get representation of the user
     */
    @GET
    @NoCache
    @Path("/subscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserMailings(final @QueryParam("email") String email) {

        realmAuth.users().requireQuery();
        UserModel userByEmail = session.users().getUserByEmail(callerRealm, email);
        if (userByEmail == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("user does not exist for email: " + email).build();
        }
        List<UserMailing> userMailings = UserMailingResource.getUserMailings(session, callerRealm.getId(), userByEmail.getId());
        if (userMailings == null){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        List<UserMailingRepresentation> reps = new ArrayList<>();
        userMailings.forEach(mailing -> reps.add(UserMailingResource.toRepresentation(mailing)));

        return Response.ok(reps, MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("subscriptions/{mailing_id}")
    public Response unsubscribeUserForUserMailing(final @PathParam("mailing_id") String mailingId,
                                                final @QueryParam("email") String email ){

        realmAuth.users().requireManage();
        UserModel userByEmail = session.users().getUserByEmail(callerRealm, email);
        if (userByEmail == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("user does not exist for email: " + email).build();
        }

        UserMailing userMailing = getUserMailing(session, callerRealm.getId(), userByEmail.getId(), mailingId);
        if (userMailing == null){
            return Response.ok().entity("user not subscribed for mailing").build();
        }
        logger.info("Delete mailing : " + mailingId);
        getEntityManager(session).remove(userMailing);
        return Response.ok().type(MediaType.TEXT_PLAIN).entity("user unsubscribed for mailing").build();
    }

    @PUT
    @Path("subscriptions/{mailing_id}")
    public Response subscribeUserForUserMailing(final @PathParam("mailing_id") String mailingId,
                                                final @QueryParam("email") String email,  final @QueryParam("language") String language,
                                                final @QueryParam("delivery") String delivery){

        realmAuth.users().requireManage();
        UserModel userByEmail = session.users().getUserByEmail(callerRealm,email);
        if (userByEmail == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("user does not exist for email: " + email).build();
        }
        UserMailing userMailing = getUserMailing(session, callerRealm.getId(), userByEmail.getId(), mailingId);
        if (userMailing != null){
            updateUserMailing(userMailing, language, delivery);
        } else {
            insertNewUserMailing(callerRealm.getId(), mailingId, userByEmail.getId(), language, delivery);
        }
        return Response.ok().type(MediaType.TEXT_PLAIN).entity("user subscribed for mailing").build();
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
        ExportUserMailings content = new ExportUserMailings(callerRealm, session, mailing);
        return DataRequestManager.getExportDataResponse(content, properties, cacheExport);

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
        return Response.ok().type(MediaType.TEXT_PLAIN).build();
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
                    insertNewUserMailing(realmId, mailingId, userId, null, null);
                    writeCount++;
                } //skip existing
            }
        }
        return writeCount;
    }

    private void updateUserMailing(UserMailing userMailing, String language, String delivery){
        if(delivery == null) {
            userMailing.setDelivery(Mailing.getPreferredMailingDelivery());
        } else {
            final int index = Mailing.deliveries.indexOf(delivery);
            userMailing.setDelivery(index == -1 ?  Mailing.getPreferredMailingDelivery() : index);
        }
        if (language != null) userMailing.setLanguage(language);

        UserMailingResource.updateUserMailing(session, userMailing);
    }

    private void insertNewUserMailing(String realmId, String mailingId, String userId, String language, String delivery) {
        UserMailingRepresentation rep = new UserMailingRepresentation();
        rep.setUserId(userId);
        rep.setRealmId(realmId);
        rep.setMailingId(mailingId);
        if(delivery == null) {
            rep.setDelivery(Mailing.getPreferredMailingDelivery());
        } else {
            final int index = delivery.indexOf(delivery);
            rep.setDelivery(index == -1 ?  Mailing.getPreferredMailingDelivery() : index);
        }
        if (language != null) rep.setLanguage(language);
        rep.setId(KeycloakModelUtils.generateId());
        insertUserMailing(session, rep);
    }

}
