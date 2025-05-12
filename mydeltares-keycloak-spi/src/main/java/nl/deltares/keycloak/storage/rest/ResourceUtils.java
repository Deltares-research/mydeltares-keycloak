package nl.deltares.keycloak.storage.rest;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import org.jboss.logging.Logger;
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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

public class ResourceUtils {

    private static final Logger logger = Logger.getLogger(ResourceUtils.class);


    private static AuthenticationManager.AuthResult resolveAuthentication(KeycloakSession keycloakSession) {
        AppAuthManager appAuthManager = new AppAuthManager();
        RealmModel realm = keycloakSession.getContext().getRealm();

        return appAuthManager.authenticateIdentityCookie(keycloakSession, realm);
    }

    private static String getTokenString(HttpHeaders headers, KeycloakSession session) {
        String tokenString = AppAuthManager.extractAuthorizationHeaderToken(headers);
        MultivaluedMap<String, String> queryParameters = session.getContext().getUri().getQueryParameters();
        if (tokenString == null && queryParameters.containsKey("access_token")) {
            tokenString = queryParameters.getFirst("access_token");
        }
        return tokenString;
    }

    static Auth getAuth(HttpHeaders httpHeaders, KeycloakSession session){
        String tokenString = getTokenString(httpHeaders, session);
        if (tokenString == null) return null;
        AccessToken token = getAccessToken(tokenString);

        String realmName = token.getIssuer().substring(token.getIssuer().lastIndexOf('/') + 1);

        RealmManager realmManager = new RealmManager(session);
        RealmModel realm = realmManager.getRealmByName(realmName);
        if (realm == null) {
            throw new NotAuthorizedException("Unknown realm in token");
        }
        session.getContext().setRealm(realm);
        final AppAuthManager.BearerTokenAuthenticator bearerTokenAuthenticator = new AppAuthManager.BearerTokenAuthenticator(session);
        bearerTokenAuthenticator.setRealm(realm).setTokenString(tokenString);
        AuthenticationManager.AuthResult authResult = bearerTokenAuthenticator.authenticate();
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
            throw new ForbiddenException();
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

    private static SecretKey setKey(String myKey) throws NoSuchAlgorithmException {
        byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }

    public static String encrypt(String strToEncrypt, String secret){
        try
        {
            SecretKey secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getUrlEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            SecretKey secretKey = setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getUrlDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e);
        }
        return null;
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
            referrerUri = URLDecoder.decode(referrerUri, StandardCharsets.UTF_8);
        }
        if (referrer != null) {
            referrer = URLDecoder.decode(referrer, StandardCharsets.UTF_8);
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

    static AuthenticationManager.AuthResult getAuthResult(KeycloakSession session, HttpHeaders httpHeaders) {
        AuthenticationManager.AuthResult authResult = resolveAuthentication(session);
        if (authResult == null) {
            //this is when user accesses API via openid request and not GUI
            Auth auth = getAuth(httpHeaders, session);
            if (auth == null) return null; //unauthenticated
            return new AuthenticationManager.AuthResult(auth.getUser(), auth.getSession(), auth.getToken(), auth.getClient());
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
