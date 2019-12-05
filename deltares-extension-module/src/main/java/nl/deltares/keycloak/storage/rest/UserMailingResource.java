package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.forms.account.freemarker.FreeMarkerAccountProvider;
import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.ClientConnection;
import org.keycloak.common.util.UriUtils;
import org.keycloak.events.EventStoreProvider;
import org.keycloak.forms.account.AccountProvider;
import org.keycloak.models.*;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.services.ErrorResponse;
import org.keycloak.services.ForbiddenException;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

public class UserMailingResource {

    private static final Logger logger = Logger.getLogger(UserMailingResource.class);
    private final KeycloakSession session;
    private final AppAuthManager authManager;

    private Auth auth;
    private AccountProvider account;
    private String stateChecker;
    private EventStoreProvider eventStore;
    private RealmModel realm;
    private ClientModel client;

    @Context
    private HttpHeaders headers;

    @Context
    private ClientConnection connection;

    @Context
    protected HttpRequest request;

    private List<MailingRepresentation> cachedMailings;
    private List<UserMailingRepresentation> cachedUserMailings;


    @Path("/mailings-page")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response mailingsPage() {

        if (auth == null) {
            return redirectLogin();
        }

        if (account instanceof FreeMarkerAccountProvider) {
            FreeMarkerAccountProvider account = (FreeMarkerAccountProvider) this.account;
            account.setMailings(getUserMailings(), getMailings());
            return account.createCustomResponse("mailings.ftl");
        } else {
            logger.error("Failed to process template because account is not of type " + FreeMarkerAccountProvider.class.getName());
            return Response.serverError().build();
        }

    }

    /**
     * Get representation of the user
     */
    @GET
    @NoCache
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserMailingRepresentation getUserMailing(final @PathParam("id") String id) {

        if (auth == null) {
            return null;
        }

        UserMailing mailing = getUserMailingById(session, id);
        if (mailing == null) {
            throw new NotFoundException("User mailing not found for id " + id);
        }

        return toRepresentation(mailing);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUserMailing(final UserMailingRepresentation rep) {

        if (auth == null) {
            return redirectLogin();
        }
        // Double-check duplicated name
        if (rep.getMailingId() != null && getUserMailing(session, realm.getId(), auth.getUser().getId(), rep.getMailingId()) != null) {
            return ErrorResponse.exists(String.format("User mailing already exists for user %s and mailing %s", auth.getUser().getId(), rep.getMailingId()));
        }

        UserMailing userMailing = new UserMailing();
        userMailing.setRealmId(realm.getId());
        userMailing.setId(KeycloakModelUtils.generateId());
        userMailing.setUserId(auth.getUser().getId());
        userMailing.setDelivery(rep.getDelivery());
        userMailing.setLanguage(rep.getLanguage());
        userMailing.setMailingId(rep.getMailingId());
        logger.info("Adding user mailing : " + userMailing.getId());
        getEntityManager(session).persist(userMailing);

        return Response.created(session.getContext().getUri().getAbsolutePathBuilder().path(userMailing.getId()).build()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserMailing(final UserMailingRepresentation rep) {

        if (auth == null) {
            return redirectLogin();
        }

        String userId = auth.getUser().getId();
        UserMailing userMailing = getUserMailing(session, realm.getId(), userId, rep.getMailingId());
        if (userMailing == null) {
            return ErrorResponse.exists(String.format("User mailing does not exist for user %s and mailing %s ", userId, rep.getMailingId()));
        }
        setMailingValues(rep, userMailing);
        logger.infof("Updating mailing %s for user %s.", rep.getMailingId(), userId);

        getEntityManager(session).persist(userMailing);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{mailing_id}")
    public Response deleteUserMailing(@PathParam("mailing_id") String mailingId) {

        if (auth == null) {
            return  redirectLogin();
        }

        UserMailing mailing = getUserMailingById(session, mailingId);
        if (mailing == null) {
            return ErrorResponse.exists("User mailing does not exist with id " + mailingId);
        }

        logger.info("Delete mailing : " + mailingId);
        getEntityManager(session).remove(mailing);
        return Response.noContent().build();
    }

    public static List<UserMailing> getUserMailings(KeycloakSession session, String realmId, String userId) {
        return getEntityManager(session).createNamedQuery("findUserMailingByUserAndRealm", UserMailing.class)
                .setParameter("realmId", realmId)
                .setParameter("userId", userId)
                .getResultList();
    }

    public static UserMailing getUserMailing(KeycloakSession session, String realmId, String userId, String mailingId) {
        List<UserMailing> resultList = getEntityManager(session).createNamedQuery("getUserMailing", UserMailing.class)
                .setParameter("realmId", realmId)
                .setParameter("userId", userId)
                .setParameter("mailingId", mailingId)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public static UserMailing getUserMailingById(KeycloakSession session, String id) {
        List<UserMailing> resultList = getEntityManager(session).createNamedQuery("getUserMailingById", UserMailing.class)
                .setParameter("id", id)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    private List<UserMailingRepresentation> getUserMailings() {
        if (cachedUserMailings != null) return cachedUserMailings;
        List<UserMailing> userMailings = getUserMailings(session, realm.getId(), auth.getUser().getId());
        cachedUserMailings = new ArrayList<>();
        for (UserMailing userMailing : userMailings) {
            cachedUserMailings.add(toRepresentation(userMailing));
        }
        return cachedUserMailings;
    }

    public static UserMailingRepresentation toRepresentation(UserMailing mailing) {
        UserMailingRepresentation rep = new UserMailingRepresentation();
        rep.setId(mailing.getId());
        rep.setMailingId(mailing.getMailingId());
        rep.setDelivery(mailing.getDelivery());
        rep.setLanguage(mailing.getLanguage());
        return rep;
    }

    private List<MailingRepresentation> getMailings() {
        if (cachedMailings != null) return cachedMailings;
        List<Mailing> mailings = MailingResource.getMailingsByRealm(session, realm.getId());
        cachedMailings = new ArrayList<>();
        HashMap<String, Boolean> access = new HashMap<>();
        for (Mailing mailing : mailings) {
            cachedMailings.add(MailingResource.toRepresentation(mailing, access));
        }
        return cachedMailings;
    }

    public UserMailingResource(KeycloakSession session) {
        this.session = session;
        this.authManager = new AppAuthManager();
        ResteasyProviderFactory.getInstance().injectProperties(this);
        init();
    }

    private void init() {
        eventStore = session.getProvider(EventStoreProvider.class);
        realm = session.getContext().getRealm();
        client = realm.getClientByClientId(Constants.ACCOUNT_MANAGEMENT_CLIENT_ID);
        if (client == null || !client.isEnabled()) {
            logger.debug("account management not enabled");
            throw new NotFoundException("account management not enabled");
        }

        account = session.getProvider(AccountProvider.class).setRealm(realm).setUriInfo(session.getContext().getUri()).setHttpHeaders(headers);

        AuthenticationManager.AuthResult authResult = authManager.authenticateIdentityCookie(session, realm);
        if (authResult != null) {
            stateChecker = (String) session.getAttribute("state_checker");
            auth = new Auth(realm, authResult.getToken(), authResult.getUser(), client, authResult.getSession(), true);
            account.setStateChecker(stateChecker);
        }

        String requestOrigin = UriUtils.getOrigin(session.getContext().getUri().getBaseUri());
        String origin = headers.getRequestHeaders().getFirst("Origin");
        if (origin != null && !requestOrigin.equals(origin)) {
            throw new ForbiddenException();
        }

        if (!request.getHttpMethod().equals("GET")) {
            String referrer = headers.getRequestHeaders().getFirst("Referer");
            if (referrer != null && !requestOrigin.equals(UriUtils.getOrigin(referrer))) {
                throw new ForbiddenException();
            }
        }

        if (authResult != null) {
            UserSessionModel userSession = authResult.getSession();
            if (userSession != null) {
                AuthenticatedClientSessionModel clientSession = userSession.getAuthenticatedClientSessionByClient(client.getId());
                if (clientSession == null) {
                    clientSession = session.sessions().createClientSession(userSession.getRealm(), client, userSession);
                }
                auth.setClientSession(clientSession);
            }

            account.setUser(auth.getUser());
        }

        account.setFeatures(realm.isIdentityFederationEnabled(), eventStore != null && realm.isEventsEnabled(), true, true);

    }

    private void setMailingValues(UserMailingRepresentation rep, UserMailing mailing) {
        mailing.setDelivery(rep.getDelivery());
        mailing.setLanguage(rep.getLanguage());
    }

    private Response redirectLogin() {
        UriBuilder uriBuilder = UriBuilder.fromUri(session.getContext().getUri().getBaseUri()).path("realms").path(realm.getName()).path("account");
        return Response.temporaryRedirect(uriBuilder.build()).build();
    }

}
