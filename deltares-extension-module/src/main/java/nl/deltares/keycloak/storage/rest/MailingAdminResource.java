package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.rest.model.ExportUserMailings;
import nl.deltares.keycloak.storage.rest.serializers.ExportCsvSerializer;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.ErrorResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.*;

public class MailingAdminResource {
    private static final Logger logger = Logger.getLogger(MailingAdminResource.class);
    private static final String SEARCH_ID_PARAMETER = "id:";
    private static String CSV_SEPARATOR = ";";
    private static int MAX_RESULTS = 100;
    private final KeycloakSession session;
    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    private AdminAuth adminAuth;
    private RealmModel callerRealm;

    MailingAdminResource(KeycloakSession session, Properties properties) {
        this.session = session;

        if (properties != null){
            MAX_RESULTS = Integer.parseInt(properties.getOrDefault("max_query_results", 100).toString());
            CSV_SEPARATOR = (String) properties.getOrDefault("csv_separator", ";");
        }

    }

    public void init() {
        Auth auth = getAuth(httpHeaders, session, clientConnection);
        this.adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, this.adminAuth.getRealm(), this.adminAuth);

        callerRealm = ResourceUtils.getRealmFromPath(session);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMailing(final MailingRepresentation rep) {
        realmAuth.users().requireManage();

        // Double-check duplicated name
        String realmId = callerRealm.getId();
        if (rep.getName() != null && getMailingByName(session, realmId, rep.getName()) != null) {
            return ErrorResponse.exists("Mailing exists with same name");
        }

        Mailing mailing = new Mailing();
        mailing.setId(KeycloakModelUtils.generateId());
        mailing.setRealmId(realmId);
        setMailingValues(rep, mailing);

        logger.info("Adding mailing : " + rep.getName());
        getEntityManager(session).persist(mailing);
        return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(mailing.getId()).build()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMailing(final MailingRepresentation rep) {
        realmAuth.users().requireManage();

        String realmId = callerRealm.getId();
        // Double-check duplicated name
        Mailing mailing = getMailingById(session, realmId, rep.getId());
        if (mailing == null) {
            return ErrorResponse.exists("Mailing does not exist with id " + rep.getId());
        }
        setMailingValues(rep, mailing);

        logger.info("Updating mailing : " + rep.getName());
        getEntityManager(session).persist(mailing);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{mailing_id}")
    public Response deleteMailing(@PathParam("mailing_id") String mailingId) {
        realmAuth.users().requireManage();

        String realmId = callerRealm.getId();
        // Double-check duplicated name
        Mailing mailing = getMailingById(session, realmId, mailingId);
        if (mailing == null) {
            return ErrorResponse.exists("Mailing does not exist with id " + mailingId);
        }

        logger.info("Delete mailing : " + mailingId);
        getEntityManager(session).remove(mailing);
        return Response.noContent().build();
    }

    /**
     * Get representation of the user
     */
    @GET
    @NoCache
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MailingRepresentation getMailing(final @PathParam("id") String id) {
        realmAuth.users().requireQuery();
        String realmId = callerRealm.getId();
        Mailing mailing = getMailingById(session, realmId, id);
        if (mailing == null) {
            throw new NotFoundException("Mailing not found for id " + id);
        }
        UserModel user = adminAuth.getUser();
        MailingRepresentation mailingRepresentation = toRepresentation(mailing, realmAuth.users().getAccess(user));
        mailingRepresentation.setAccess(realmAuth.users().getAccess(user));

        return mailingRepresentation;
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<MailingRepresentation> getMailings(@QueryParam("search") String search, @QueryParam("name") String name) {
        realmAuth.users().requireQuery();
        String realmId = callerRealm.getId();
        List<MailingRepresentation> reps = new ArrayList<>();
        UserModel user = adminAuth.getUser();
        Map<String, Boolean> access = realmAuth.users().getAccess(user);
        if (search != null) {
            if (search.startsWith(SEARCH_ID_PARAMETER)) {
                Mailing mailing = getMailingById(session, realmId, search.substring(SEARCH_ID_PARAMETER.length()).trim());
                if (mailing != null) {
                    reps.add(toRepresentation(mailing , access));
                }
            } else {
                List<Mailing> resultList = searchForMailings(session, realmId, search.trim());

                resultList.forEach(mailing -> reps.add(toRepresentation(mailing, access)));
            }
            return reps;
        }

        if (name != null) {
            Mailing mailing = getMailingByName(session, realmId, name);
            if (mailing == null) throw new NotFoundException("Mailing not found for name " + name);
            reps.add(toRepresentation(mailing, access));
        } else {
            List<Mailing> resultList = getMailingsByRealm(session, realmId);
            resultList.forEach(mailing -> reps.add(toRepresentation(mailing, access)));
        }
        return reps;
    }

    @GET
    @NoCache
    @Path("/export/usermailings/{id}")
    @Produces("text/csv")
    public Response downloadUserMailings(final @PathParam("id") String id) {

        realmAuth.users().requireQuery();
        String realmId = callerRealm.getId();
        UserProvider userProvider = session.userStorageManager();
        ExportCsvSerializer serializer = new ExportCsvSerializer();

        Mailing mailing = getMailingById(session, realmId, id);
        if (mailing == null){
            return Response.status(Response.Status.NO_CONTENT).entity("no mailing for id: " + id).build();
        }
        TypedQuery<UserMailing> query = getEntityManager(session).createNamedQuery("allUserMailingsByMailingAndRealm", UserMailing.class);
        query.setParameter("realmId", realmId);
        query.setParameter("mailingId", id);

        ExportUserMailings content = new ExportUserMailings(userProvider, callerRealm, query, mailing);
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

    private void setMailingValues(MailingRepresentation rep, Mailing mailing) {
        mailing.setName(rep.getName());
        mailing.setDescription(rep.getDescription());
        mailing.setDelivery(rep.getDelivery());
        mailing.setLanguages(rep.getLanguages());
        mailing.setFrequency(rep.getFrequency());
        mailing.setCreatedTimestamp(System.currentTimeMillis());
    }

    static Mailing getMailingById(KeycloakSession session, String realmId, String id) {
        List<Mailing> resultList = getEntityManager(session).createNamedQuery("findMailingByIdAndRealm", Mailing.class)
                .setParameter("id", id)
                .setParameter("realmId", realmId)
                .getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    static Mailing getMailingByName(KeycloakSession session, String realmId, String name) {
        List<Mailing> resultList = getEntityManager(session).createNamedQuery("findMailingByNameAndRealm", Mailing.class)
                .setParameter("name", name)
                .setParameter("realmId", realmId)
                .getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    static List<Mailing> searchForMailings(KeycloakSession session, String realmId, String search) {
        return getEntityManager(session).createNamedQuery("searchForMailing", Mailing.class)
                .setParameter("realmId", realmId)
                .setParameter("search", search)
                .getResultList();
    }

    static List<Mailing> getMailingsByRealm(KeycloakSession session, String realmId) {
        return getEntityManager(session).createNamedQuery("getAllMailingsByRealm", Mailing.class)
                .setParameter("realmId", realmId)
                .getResultList();
    }

    static MailingRepresentation toRepresentation(Mailing mailing, Map<String, Boolean> access) {
        MailingRepresentation rep = new MailingRepresentation();
        rep.setId(mailing.getId());
        rep.setRealmId(mailing.getRealmId());
        rep.setName(mailing.getName());
        rep.setDescription(mailing.getDescription());
        rep.setDelivery(mailing.getDelivery());
        rep.setLanguages(mailing.getLanguages());
        rep.setFrequency(mailing.getFrequency());
        rep.getCreatedTimestamp(mailing.getCreatedTimestamp());
        rep.setAccess(access);
        return rep;
    }

}
