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
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.*;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuthResult;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

public class UserMailingResource {

    private static final Logger logger = Logger.getLogger(UserMailingResource.class);
    private final KeycloakSession session;
    private final AppAuthManager authManager;

    private AuthenticationManager.AuthResult authResult;
    private AccountProvider account;
    private RealmModel realm;
    private final Properties properties;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection connection;

    @Context
    protected HttpRequest request;

    private List<MailingRepresentation> cachedMailings;
    private List<UserMailingRepresentation> cachedUserMailings;

    @Path("/admin")
    public UserMailingAdminResource admin() {
        UserMailingAdminResource service = new UserMailingAdminResource(session, properties);
        ResteasyProviderFactory.getInstance().injectProperties(service);
        service.init();
        return service;
    }

    @Path("/mailings-page")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response mailingsPage() {

        if (authResult == null) {
            return redirectLogin();
        }

        if (account instanceof FreeMarkerAccountProvider) {
            FreeMarkerAccountProvider account = (FreeMarkerAccountProvider) this.account;
            account.setMailings(getUserMailings(), getMailings());
            return account.createResponse("MAILINGS","mailings.ftl");
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
    @Path("{mailing_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserMailing(final @PathParam("mailing_id") String mailingId) {

        if (authResult == null) {
            return redirectLogin();
        }
        UserModel user = authResult.getUser();
        UserMailing mailing = getUserMailing(session, realm.getId(), user.getId(), mailingId);
        if (mailing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toRepresentation(mailing), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUserMailing(final UserMailingRepresentation rep) {

        if (authResult == null) {
            return redirectLogin();
        }
        if (rep.getMailingId() == null){
            logger.error("No mailing id set for user mailing!");
            return ErrorResponse.error("No mailing id set for user mailing!", Response.Status.BAD_REQUEST);
        }
        try {
            validateUserMailing(rep);
        } catch (IOException e) {
            return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
        }
        // Double-check duplicated name
        UserModel user = authResult.getUser();
        if (getUserMailing(session, realm.getId(), user.getId(), rep.getMailingId()) != null) {
            String message = String.format("User mailing already exists for user %s and mailing %s", user.getId(), rep.getMailingId());
            logger.error(message);
            return ErrorResponse.exists(message);
        }
        rep.setUserId(user.getId());
        rep.setRealmId(realm.getId());
        rep.setId(KeycloakModelUtils.generateId());
        logger.info("Adding user mailing : " + rep.getId());
        insertUserMailing(session, rep);

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserMailing(final UserMailingRepresentation rep) {

        if (authResult == null) {
            return redirectLogin();
        }

        String id = rep.getId();
        UserMailing mailing = getUserMailingById(session, id);
        if (mailing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        String userId = authResult.getUser().getId();
        if (!userId.equals(mailing.getUserId())){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        try {
            validateUserMailing(rep);
        } catch (IOException e) {
            return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
        }

        setMailingValues(rep, mailing);
        logger.infof("Updating mailing %s for user %s.", rep.getMailingId(), userId);

        getEntityManager(session).persist(mailing);
        return Response.ok().build();

    }

    private void validateUserMailing(UserMailingRepresentation userMailing) throws IOException {
        Mailing mailing = MailingAdminResource.getMailingById(session, realm.getId(), userMailing.getMailingId());
        assert mailing != null;
        if (!mailing.isValidDelivery(userMailing.getDelivery())){
            throw new IOException(String.format("Invalid delivery %s! Expected %s", userMailing.getDeliveryTxt(), Mailing.deliveries.get(mailing.getDelivery())));
        }
        if (!mailing.isValidLanguage(userMailing.getLanguage())){
            throw new IOException(String.format("Invalid language %s! Expected one of %s", userMailing.getLanguage(), Arrays.toString(mailing.getLanguages())));
        }
    }

    @DELETE
    @Path("/{mailing_id}")
    public Response deleteUserMailing(@PathParam("mailing_id") String mailingId) {

        if (authResult == null) {
            return redirectLogin();
        }

        UserMailing mailing = getUserMailing(session, realm.getId(), authResult.getUser().getId(), mailingId);
        if (mailing == null) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }

        logger.info("Delete mailing : " + mailingId);
        getEntityManager(session).remove(mailing);
        return Response.ok().build();
    }

    @GET
    @NoCache
    @Path("/unsubscribe/{mailing_id}/{code}")
    @Produces(MediaType.TEXT_HTML)
    public Response unsubscribe(final @PathParam("mailing_id") String mailingId, final @PathParam("code") String code){

        String userId = ResourceUtils.decrypt(code, mailingId);
        UserMailing mailing = getUserMailing(session, realm.getId(), userId, mailingId);
        boolean status;
        if (mailing == null) {
            status = false;
        } else {
            logger.info("Delete mailing : " + mailingId);
            getEntityManager(session).remove(mailing);
            status = true;
        }
        final String[] name = {"unknown mailing"};

        getMailings().forEach(
                m -> {
                    if (m.getId().equals(mailingId)) {
                        name[0] = m.getName();
                    }
                });

        if (account instanceof FreeMarkerAccountProvider) {

            FreeMarkerAccountProvider account = (FreeMarkerAccountProvider) this.account;
            account.setAttribute("status", String.valueOf(status));
            account.setAttribute("mailingName", name[0]);
            return account.createResponse("UNSUBSCRIBE", "unsubscribe.ftl");
        } else {
            logger.error("Failed to process template because account is not of type " + FreeMarkerAccountProvider.class.getName());
            return Response.serverError().build();
        }

    }

    public static List<UserMailing> getUserMailings(KeycloakSession session, String realmId, String userId) {
        try {
            return getEntityManager(session).createNamedQuery("findUserMailingByUserAndRealm", UserMailing.class)
                    .setParameter("realmId", realmId)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            logger.warn(String.format("Failed to retrieve user-mailings userId=%s realmId=%s", userId, realmId));
            return null;
        }
    }

    public static UserMailing getUserMailing(KeycloakSession session, String realmId, String userId, String mailingId) {
        List<UserMailing> resultList;
        try {
            resultList = getEntityManager(session).createNamedQuery("getUserMailing", UserMailing.class)
                    .setParameter("realmId", realmId)
                    .setParameter("userId", userId)
                    .setParameter("mailingId", mailingId)
                    .getResultList();
        } catch (Exception e) {
            logger.warn(String.format("Failed to retrieve user-mailing mailingId=%s userId=%s realmId=%s", mailingId, userId, realmId));
            return null;
        }
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    private static UserMailing getUserMailingById(KeycloakSession session, String id) {
        List<UserMailing> resultList = getEntityManager(session).createNamedQuery("getUserMailingById", UserMailing.class)
                .setParameter("id", id)
                .getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    private List<UserMailingRepresentation> getUserMailings() {
        if (cachedUserMailings != null) return cachedUserMailings;
        List<UserMailing> userMailings = getUserMailings(session, session.getContext().getRealm().getId(), authResult.getUser().getId());
        cachedUserMailings = new ArrayList<>();
        if (userMailings == null) return cachedUserMailings;
        for (UserMailing userMailing : userMailings) {
            cachedUserMailings.add(toRepresentation(userMailing));
        }
        return cachedUserMailings;
    }

    public static UserMailingRepresentation toRepresentation(UserMailing mailing) {
        UserMailingRepresentation rep = new UserMailingRepresentation();
        rep.setId(mailing.getId());
        rep.setUserId(mailing.getUserId());
        rep.setRealmId(mailing.getRealmId());
        rep.setMailingId(mailing.getMailingId());
        rep.setDelivery(mailing.getDelivery());
        rep.setLanguage(mailing.getLanguage());
        return rep;
    }

    private List<MailingRepresentation> getMailings() {
        if (cachedMailings != null) return cachedMailings;
        List<Mailing> mailings = MailingAdminResource.getMailingsByRealm(session, realm.getId());
        cachedMailings = new ArrayList<>();
        HashMap<String, Boolean> access = new HashMap<>();
        for (Mailing mailing : mailings) {
            cachedMailings.add(MailingAdminResource.toRepresentation(mailing, access));
        }
        return cachedMailings;
    }

    UserMailingResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.authManager = new AppAuthManager();
        ResteasyProviderFactory.getInstance().injectProperties(this);
        this.properties = properties;
    }

    void init(){

        if (isInitAccount()){
            initAccount();
        } else {
            initApi();
        }

    }

    private boolean isInitAccount() {
        List<PathSegment> pathSegments = request.getUri().getPathSegments();
        for (PathSegment pathSegment : pathSegments) {
            if (pathSegment.getPath().equals("mailings-page") || pathSegment.getPath().equals("unsubscribe")) return true;
        }
        return  false;
    }

    private void initApi(){
        ResteasyProviderFactory.getInstance().injectProperties(this);
        authResult = getAuthResult(session, httpHeaders);
        realm = session.getContext().getRealm();
    }

    private void initAccount() {
        realm = session.getContext().getRealm();
        ClientModel client = realm.getClientByClientId(Constants.ACCOUNT_MANAGEMENT_CLIENT_ID);
        if (client == null || !client.isEnabled()) {
            logger.debug("account management not enabled");
            throw new NotFoundException("account management not enabled");
        }

        String requestOrigin = UriUtils.getOrigin(session.getContext().getUri().getBaseUri());
        String origin = httpHeaders.getRequestHeaders().getFirst("Origin");
        if (origin != null && !requestOrigin.equals(origin)) {
            throw new ForbiddenException();
        }
        if (!request.getHttpMethod().equals("GET")) {
            String referrer = httpHeaders.getRequestHeaders().getFirst("Referer");
            if (referrer != null && !requestOrigin.equals(UriUtils.getOrigin(referrer))) {
                throw new ForbiddenException();
            }
        }

        account = session.getProvider(AccountProvider.class).setRealm(realm).setUriInfo(session.getContext().getUri()).setHttpHeaders(httpHeaders);
        account.setReferrer(ResourceUtils.getReferrer(session));
        authResult = authManager.authenticateIdentityCookie(session, realm);
        if (authResult != null) {
            String stateChecker = (String) session.getAttribute("state_checker");
            account.setStateChecker(stateChecker);
            Auth auth = new Auth(realm, authResult.getToken(), authResult.getUser(), client, authResult.getSession(), true);
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
        EventStoreProvider eventStore = session.getProvider(EventStoreProvider.class);
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

    static void insertUserMailing(KeycloakSession session, UserMailingRepresentation rep) {
        UserMailing userMailing = new UserMailing();
        userMailing.setRealmId(rep.getRealmId());
        userMailing.setId(rep.getId());
        userMailing.setUserId(rep.getUserId());
        userMailing.setDelivery(rep.getDelivery());
        if (rep.getLanguage() != null) userMailing.setLanguage(rep.getLanguage());
        userMailing.setMailingId(rep.getMailingId());

        try {
            getEntityManager(session).persist(userMailing);
        } catch (Exception e){
            logger.warn(String.format("Failed to insert user-mailing mailingId=%s userId=%s", userMailing.getMailingId(), userMailing.getUserId()));
        }
    }
}
