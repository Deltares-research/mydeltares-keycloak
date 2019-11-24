package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Mailing;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
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

    private final KeycloakSession session;
    private AdminPermissionEvaluator realmAuth;
    private AppAuthManager authManager;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;
    private RealmModel realm;


    public MailingResource(KeycloakSession session) {
        this.session = session;
        this.authManager = new AppAuthManager();
        init();
    }

    public void init() {
        realm = session.getContext().getRealm();
        AdminAuth auth = authenticateRealmAdminRequest(authManager, httpHeaders, session, clientConnection);
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
        return Response.ok().build();
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
        return Response.ok().build();
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
        return Response.ok().build();
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

        Mailing mailing = getMailingById(id, realm.getId());
        if (mailing == null) {
            throw new NotFoundException("Mailing not found for id " + id);
        }

        return toRepresentation(mailing);
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public List<MailingRepresentation> getMailings(@QueryParam("name") String name) {
        realmAuth.users().requireQuery();

        List<MailingRepresentation> reps = new ArrayList<>();
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
    }

    private Mailing getMailingById(String realmId, String id) {
        List<Mailing> resultList = getEntityManager().createNamedQuery("findById", Mailing.class)
                .setParameter("id", id)
                .setParameter("realmId", realmId)
                .getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    private Mailing getMailingByName(String realmId, String name) {
        List<Mailing> resultList = getEntityManager().createNamedQuery("findByName", Mailing.class)
                .setParameter("name", name)
                .setParameter("realmId", realmId)
                .getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    private List<Mailing> getMailingsByRealm(String realmId) {
        return getEntityManager().createNamedQuery("getAllByRealm", Mailing.class)
                .setParameter("realmId", realmId)
                .getResultList();
    }

    private EntityManager getEntityManager() {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    private static MailingRepresentation toRepresentation(Mailing mailing) {
        MailingRepresentation rep = new MailingRepresentation();
        rep.setId(mailing.getId());
        rep.setRealmId(mailing.getRealmId());
        rep.setName(mailing.getName());
        rep.setDescription(mailing.getDescription());
        rep.setDelivery(mailing.getDelivery());
        rep.setFrequency(mailing.getFrequency());
        return rep;
    }

}
