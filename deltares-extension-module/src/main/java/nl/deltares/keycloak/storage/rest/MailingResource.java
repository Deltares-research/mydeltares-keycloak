package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Mailing;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.ErrorResponse;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.authenticateRealmAdminRequest;

public class MailingResource {
    private static final Logger logger = Logger.getLogger(MailingResource.class);
    private static final String SEARCH_ID_PARAMETER = "id:";
    private final KeycloakSession session;
    private AdminPermissionEvaluator realmAuth;
    private AppAuthManager authManager;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;
    private RealmModel realm;
    private UserModel user;

    @Path("/admin")
    public MailingResource admin() {
        ResteasyProviderFactory.getInstance().injectProperties(this);
        init();
        return this;
    }


    public MailingResource(KeycloakSession session) {
        this.session = session;
        this.authManager = new AppAuthManager();
    }

    public void init() {
        realm = session.getContext().getRealm();
        AdminAuth auth = authenticateRealmAdminRequest(authManager, httpHeaders, session, clientConnection);
        user = auth.getUser();
        realmAuth = AdminPermissions.evaluator(session, realm, auth);
        session.getContext().setRealm(realm);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMailing(final MailingRepresentation rep) {
        realmAuth.users().requireManage();

        // Double-check duplicated name
        if (rep.getName() != null && getMailingByName(realm.getId(), rep.getName()) != null) {
            return ErrorResponse.exists("Mailing exists with same name");
        }

        Mailing mailing = new Mailing();
        mailing.setId(KeycloakModelUtils.generateId());
        mailing.setRealmId(realm.getId());
        setMailingValues(rep, mailing);

        logger.info("Adding mailing : " + rep.getName());
        getEntityManager().persist(mailing);
        return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(mailing.getId()).build()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMailing(final MailingRepresentation rep) {
        realmAuth.users().requireManage();

        // Double-check duplicated name
        Mailing mailing = getMailingById(realm.getId(), rep.getId());
        if (mailing == null) {
            return ErrorResponse.exists("Mailing does not exist with id " + rep.getId());
        }
        setMailingValues(rep, mailing);

        logger.info("Updating mailing : " + rep.getName());
        getEntityManager().persist(mailing);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{mailing_id}")
    public Response deleteMailing(@PathParam("mailing_id") String mailingId) {
        realmAuth.users().requireManage();

        // Double-check duplicated name
        Mailing mailing = getMailingById(realm.getId(), mailingId);
        if (mailing == null) {
            return ErrorResponse.exists("Mailing does not exist with id " + mailingId);
        }

        logger.info("Delete mailing : " + mailingId);
        getEntityManager().remove(mailing);
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

        Mailing mailing = getMailingById(realm.getId(), id);
        if (mailing == null) {
            throw new NotFoundException("Mailing not found for id " + id);
        }

        return toRepresentation(mailing);
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<MailingRepresentation> getMailings(@QueryParam("search") String search, @QueryParam("name") String name) {
        realmAuth.users().requireQuery();

        List<MailingRepresentation> reps = new ArrayList<>();
        if (search != null) {
            if (search.startsWith(SEARCH_ID_PARAMETER)) {
                Mailing mailing = getMailingById(realm.getId(), search.substring(SEARCH_ID_PARAMETER.length()).trim());
                if (mailing != null) {
                    reps.add(toRepresentation(mailing));
                }
            } else {
                List<Mailing> resultList = searchForMailings(realm.getId(), search.trim());
                resultList.forEach(mailing -> reps.add(toRepresentation(mailing)));
            }
            return reps;
        }

        if (name != null) {
            Mailing mailing = getMailingByName(realm.getId(), name);
            if (mailing == null) throw new NotFoundException("Mailing not found for name " + name);
            reps.add(toRepresentation(mailing));
        } else {
            List<Mailing> resultList = getMailingsByRealm(realm.getId());
            resultList.forEach(mailing -> reps.add(toRepresentation(mailing)));
        }
        return reps;
    }

    private void setMailingValues(MailingRepresentation rep, Mailing mailing) {
        mailing.setName(rep.getName());
        mailing.setDescription(rep.getDescription());
        mailing.setDelivery(rep.getDelivery());
        mailing.setLanguages(rep.getLanguages());
        mailing.setFrequency(rep.getFrequency());
        mailing.setCreatedTimestamp(System.currentTimeMillis());
    }

    private Mailing getMailingById(String realmId, String id) {
        List<Mailing> resultList = getEntityManager().createNamedQuery("findMailingByIdAndRealm", Mailing.class)
                .setParameter("id", id)
                .setParameter("realmId", realmId)
                .getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    private Mailing getMailingByName(String realmId, String name) {
        List<Mailing> resultList = getEntityManager().createNamedQuery("findMailingByNameAndRealm", Mailing.class)
                .setParameter("name", name)
                .setParameter("realmId", realmId)
                .getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    private List<Mailing> searchForMailings(String realmId, String search) {
        return getEntityManager().createNamedQuery("searchForMailing", Mailing.class)
                .setParameter("realmId", realmId)
                .setParameter("search", search)
                .getResultList();
    }

    private List<Mailing> getMailingsByRealm(String realmId) {
        return getEntityManager().createNamedQuery("getAllMailingsByRealm", Mailing.class)
                .setParameter("realmId", realmId)
                .getResultList();
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    private MailingRepresentation toRepresentation(Mailing mailing) {
        MailingRepresentation rep = new MailingRepresentation();
        rep.setId(mailing.getId());
        rep.setRealmId(mailing.getRealmId());
        rep.setName(mailing.getName());
        rep.setDescription(mailing.getDescription());
        rep.setDelivery(mailing.getDelivery());
        rep.setLanguages(mailing.getLanguages());
        rep.setFrequency(mailing.getFrequency());
        rep.getCreatedTimestamp(mailing.getCreatedTimestamp());

        rep.setAccess(realmAuth.users().getAccess(user));
        return rep;
    }

}
