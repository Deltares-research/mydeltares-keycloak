package nl.deltares.keycloak.storage.rest;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.PathSegment;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.util.UriUtils;
import org.keycloak.models.ClientModel;
import org.keycloak.models.Constants;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

import java.util.List;
import java.util.Properties;

public class UserAttributesResource {

    private static final Logger logger = Logger.getLogger(UserAttributesResource.class);
    private final KeycloakSession session;
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
        ResteasyProviderFactory.getInstance().injectProperties(this);
        this.properties = properties;
    }

    void init() {

        if (isInitAccount()) {
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
        return false;
    }

    private void initApi() {
        ResteasyProviderFactory.getInstance().injectProperties(this);
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

    }

}
