package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.rest.model.ExportCsvContent;
import nl.deltares.keycloak.storage.rest.model.Serializer;
import nl.deltares.keycloak.storage.rest.model.TextSerializer;
import org.jboss.logging.Logger;
import org.keycloak.common.ClientConnection;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakUriInfo;
import org.keycloak.models.RealmModel;
import org.keycloak.representations.AccessToken;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.managers.RealmManager;

import javax.persistence.EntityManager;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

public class ResourceUtils {

    private static final Logger logger = Logger.getLogger(ResourceUtils.class);


    private static AuthenticationManager.AuthResult resolveAuthentication(KeycloakSession keycloakSession) {
        AppAuthManager appAuthManager = new AppAuthManager();
        RealmModel realm = keycloakSession.getContext().getRealm();

        return appAuthManager.authenticateIdentityCookie(keycloakSession, realm);
    }

    private static String getTokenString(AppAuthManager authManager, HttpHeaders headers, KeycloakSession session) {
        String tokenString = authManager.extractAuthorizationHeaderToken(headers);
        MultivaluedMap<String, String> queryParameters = session.getContext().getUri().getQueryParameters();
        if (tokenString == null && queryParameters.containsKey("access_token")) {
            tokenString = queryParameters.getFirst("access_token");
        }
        if (tokenString == null) throw new NotAuthorizedException("No Bearer token");

        return tokenString;
    }

    static Auth getAuth(HttpHeaders httpHeaders, KeycloakSession session,
                        ClientConnection clientConnection){
        AppAuthManager authManager = new AppAuthManager();
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

        Auth auth = new Auth(realm, token, authResult.getUser(), client, authResult.getSession(), true);
        if (!auth.getRealm().equals(realmManager.getKeycloakAdminstrationRealm())
                && !auth.getRealm().equals(realm)) {
            throw new org.keycloak.services.ForbiddenException();
        }
        return  auth;
    }
    private static AccessToken getAccessToken(String tokenString) {

        try {
            JWSInput input = new JWSInput(tokenString);
            return input.readJsonContent(AccessToken.class);
        } catch (JWSInputException e) {
            throw new NotAuthorizedException("Bearer token format error");
        }
    }

    static RealmModel getRealmFromPath(KeycloakSession session) {

        KeycloakUriInfo requestUri = session.getContext().getUri();

        String substring = requestUri.getRequestUri().getPath().substring(requestUri.getBaseUri().getPath().length());
        String[] split = substring.split("/");
        assert split.length > 1;
        RealmManager realmManager = new RealmManager(session);
        return realmManager.getRealm(split[1]);
    }

    public static EntityManager getEntityManager(KeycloakSession session) {
        return session.getProvider(JpaConnectionProvider.class).getEntityManager();
    }

    static String[] getReferrer(KeycloakSession session) {
        String ref = session.getContext().getRequestHeaders().getHeaderString("Referer");
        if (ref == null) {
            return null;
        }
        int beginIndex = ref.indexOf('?');
        String referrerUri = null;
        String referrer = null;
        if (beginIndex > -1) {
            String requestParameters = ref.substring(beginIndex + 1);
            String[] params = requestParameters.split("&");
            for (String param : params) {
                if (param.startsWith("referrer_uri")) {
                    referrerUri = param.split("=")[1];
                } else if (param.startsWith("referrer")) {
                    referrer = param.split("=")[1];
                }
            }
        }
        if (referrer == null) {
            referrer = session.getContext().getUri().getQueryParameters().getFirst("referrer");
        }
        if (referrerUri == null) {
            referrerUri = session.getContext().getUri().getQueryParameters().getFirst("referrer_uri");
        }

        if (referrerUri != null) {
            try {
                referrerUri = URLDecoder.decode(referrerUri, "utf-8");
            } catch (UnsupportedEncodingException ignored) {
            }//we tried
        }
        if (referrer != null) {
            try {
                referrer = URLDecoder.decode(referrer, "utf-8");
            } catch (UnsupportedEncodingException ignored) {
            }//we tried
        }
        return new String[]{referrer, referrerUri};
    }

    static String parseContentType(String rawContentType) {

        String[] split = rawContentType.split(";");
        for (String s : split) {
            if (s.toLowerCase().startsWith("image")) return s;
        }
        return rawContentType;
    }

    static StreamingOutput getStreamingOutput(ExportCsvContent content, Serializer<ExportCsvContent> serializer) {
        if (serializer instanceof TextSerializer) {
            final TextSerializer<ExportCsvContent> textSerializer = (TextSerializer<ExportCsvContent>) serializer;
            return os -> {

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                try {
                    textSerializer.serialize(content, writer);
                } catch (Exception e) {
                    logger.warn("Error serializing csv content: %s", e);
                }
                writer.flush();
            };
        }
        return null;
    }

    static AuthenticationManager.AuthResult getAuthResult(KeycloakSession session, HttpHeaders httpHeaders, ClientConnection clientConnection) {
        AuthenticationManager.AuthResult authResult = resolveAuthentication(session);
        if (authResult == null) {
            //this is when user accesses API via openid request and not GUI
            Auth auth = getAuth(httpHeaders, session, clientConnection);
            return new AuthenticationManager.AuthResult(auth.getUser(), auth.getSession(), auth.getToken());
        }
        return authResult;
    }

    static Properties getResourceProperties(String propertiesFileName){

        InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream("META-INF/" + propertiesFileName);

        Properties properties = new Properties();
        if (is == null) {
            logger.warn("Could not find deltares-extention.properties in classpath");
        } else {
            try {
                properties.load(is);
            } catch (IOException ex) {
                logger.error("Failed to load deltares-extention.properties file", ex);
            }
        }
        return properties;
    }

    static String contentTypeToExtension(String contentType){
        String[] split = contentType.split("/");
        if (split.length > 1) return split[1];
        if (split.length > 0) return split[0];
        return "";
    }
}
