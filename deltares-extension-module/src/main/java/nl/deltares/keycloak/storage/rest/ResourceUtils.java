package nl.deltares.keycloak.storage.rest;

import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakUriInfo;
import org.keycloak.models.RealmModel;
import org.keycloak.protocol.oidc.utils.RedirectUtils;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.util.ResolveRelative;
import org.keycloak.services.validation.Validation;

import javax.persistence.EntityManager;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

class ResourceUtils {

    static AuthenticationManager.AuthResult resolveAuthentication(KeycloakSession keycloakSession) {
        AppAuthManager appAuthManager = new AppAuthManager();
        RealmModel realm = keycloakSession.getContext().getRealm();

        return appAuthManager.authenticateIdentityCookie(keycloakSession, realm);
    }

    private static String getTokenString(AppAuthManager authManager, HttpHeaders headers, KeycloakSession session){
        String tokenString = authManager.extractAuthorizationHeaderToken(headers);
        MultivaluedMap<String, String> queryParameters = session.getContext().getUri().getQueryParameters();
        if (tokenString == null && queryParameters.containsKey("access_token")) {
            tokenString = queryParameters.getFirst("access_token");
        }
        if (tokenString == null) throw new NotAuthorizedException("No Bearer token");

        return tokenString;
    }

    static AdminAuth authenticateRealmAdminRequest(AppAuthManager authManager, HttpHeaders httpHeaders, KeycloakSession session, ClientConnection clientConnection) {

        String tokenString = getTokenString(authManager, httpHeaders, session);
        AccessToken token = getAccessToken(tokenString);

        String realmName = token.getIssuer().substring(token.getIssuer().lastIndexOf('/') + 1);
        RealmManager realmManager = new RealmManager(session);
        RealmModel realm = realmManager.getRealmByName(realmName);
        if (realm == null) {
            throw new NotAuthorizedException("Unknown realm in token");
        }
        session.getContext().setRealm(realm);
        AuthenticationManager.AuthResult authResult = authManager.authenticateBearerToken(tokenString, session, realm, session.getContext().getUri(), clientConnection, httpHeaders);
        if (authResult == null) {
            throw new NotAuthorizedException("Bearer");
        }

        ClientModel client = realm.getClientByClientId(token.getIssuedFor());
        if (client == null) {
            throw new NotFoundException("Could not find client for authorization");

        }

        AdminAuth auth = new AdminAuth(realm, authResult.getToken(), authResult.getUser(), client);

        if (!auth.getRealm().equals(realmManager.getKeycloakAdminstrationRealm())
                && !auth.getRealm().equals(realm)) {
            throw new org.keycloak.services.ForbiddenException();
        }
        return auth;

    }

    private static AccessToken getAccessToken(String tokenString){

        try {
            JWSInput input = new JWSInput(tokenString);
            return input.readJsonContent(AccessToken.class);
        } catch (JWSInputException e) {
            throw new NotAuthorizedException("Bearer token format error");
        }
    }

    static RealmModel getRealmFromPath(KeycloakSession session){

        KeycloakUriInfo requestUri = session.getContext().getUri();

        String substring = requestUri.getRequestUri().getPath().substring(requestUri.getBaseUri().getPath().length());
        String[] split = substring.split("/");
        assert split.length > 1;
        RealmManager realmManager = new RealmManager(session);
        return realmManager.getRealm(split[1]);
    }

    static EntityManager getEntityManager(KeycloakSession session) {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    static Auth authenticateRealmRequest(AppAuthManager authManager, HttpHeaders httpHeaders, KeycloakSession session, ClientConnection clientConnection) {
        String tokenString = getTokenString(authManager, httpHeaders, session);
        AccessToken token = getAccessToken(tokenString);

        String realmName = token.getIssuer().substring(token.getIssuer().lastIndexOf('/') + 1);
        RealmManager realmManager = new RealmManager(session);
        RealmModel realm = realmManager.getRealmByName(realmName);
        if (realm == null) {
            throw new NotAuthorizedException("Unknown realm in token");
        }
        session.getContext().setRealm(realm);
        AuthenticationManager.AuthResult authResult = authManager.authenticateBearerToken(tokenString, session, realm, session.getContext().getUri(), clientConnection, httpHeaders);
        if (authResult == null) {
            throw new NotAuthorizedException("Bearer");
        }

        ClientModel client = realm.getClientByClientId(token.getIssuedFor());
        if (client == null) {
            throw new NotFoundException("Could not find client for authorization");

        }

        return new Auth(realm, authResult.getToken(), authResult.getUser(), client, authResult.getSession(), true);

    }

    static String[] getReferrer(KeycloakSession session, RealmModel realm, ClientModel client) {
        String referrer = session.getContext().getUri().getQueryParameters().getFirst("referrer");
        if (referrer == null) {
            return null;
        }

        String referrerUri = session.getContext().getUri().getQueryParameters().getFirst("referrer_uri");

        ClientModel referrerClient = realm.getClientByClientId(referrer);
        if (referrerClient != null) {
            if (referrerUri != null) {
                referrerUri = RedirectUtils.verifyRedirectUri(session.getContext().getUri(), referrerUri, realm, referrerClient);
            } else {
                referrerUri = ResolveRelative.resolveRelativeUri(session.getContext().getUri().getRequestUri(), client.getRootUrl(), referrerClient.getBaseUrl());
            }

            if (referrerUri != null) {
                String referrerName = referrerClient.getName();
                if (Validation.isBlank(referrerName)) {
                    referrerName = referrer;
                }
                return new String[]{referrerName, referrerUri};
            }
        } else if (referrerUri != null) {
            referrerClient = realm.getClientByClientId(referrer);
            if (client != null) {
                referrerUri = RedirectUtils.verifyRedirectUri(session.getContext().getUri(), referrerUri, realm, referrerClient);

                if (referrerUri != null) {
                    return new String[]{referrer, referrerUri};
                }
            }
        }

        return null;
    }

    static String parseContentType(String rawContentType){

        String[] split = rawContentType.split(";");
        for (String s : split) {
            if (s.toLowerCase().startsWith("image")) return s;
        }
        return rawContentType;
    }
    static UriBuilder appendReferrer(UriBuilder builder, KeycloakSession session) {
        String referrer = session.getContext().getUri().getQueryParameters().getFirst("referrer");
        if (referrer != null) {
            builder = builder.queryParam("referrer", referrer);
        }
        String referrerUri = session.getContext().getUri().getQueryParameters().getFirst("referrer_uri");
        if (referrerUri != null){
            builder = builder.queryParam("referrer_uri", referrerUri);
        }
        return builder;

    }
}
