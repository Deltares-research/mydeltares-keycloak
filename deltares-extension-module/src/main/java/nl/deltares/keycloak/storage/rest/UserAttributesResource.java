package nl.deltares.keycloak.storage.rest;

import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.util.UriUtils;
import org.keycloak.events.EventStoreProvider;
import org.keycloak.forms.account.AccountProvider;
import org.keycloak.models.*;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import java.util.List;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuthResult;

public class UserAttributesResource {

    private static final Logger logger = Logger.getLogger(UserAttributesResource.class);
    private final KeycloakSession session;
    private final AppAuthManager authManager;

    private AuthenticationManager.AuthResult authResult;
    private RealmModel realm;
    private final Properties properties;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    protected HttpRequest request;

    @Path("/admin")
    public UserAttributesAdminResource admin() {
        UserAttributesAdminResource service = new UserAttributesAdminResource(session, properties);
        ResteasyProviderFactory.getInstance().injectProperties(service);
        service.init();
        return service;
    }


    UserAttributesResource(KeycloakSession session, Properties properties) {
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
        List<PathSegment> pathSegments = session.getContext().getUri().getPathSegments();
        for (PathSegment pathSegment : pathSegments) {
            if (pathSegment.getPath().equals("mailings-page")) return true;
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

        AccountProvider account = session.getProvider(AccountProvider.class).setRealm(realm).setUriInfo(session.getContext().getUri()).setHttpHeaders(httpHeaders);
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

}
