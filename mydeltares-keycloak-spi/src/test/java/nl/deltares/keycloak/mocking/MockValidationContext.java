package nl.deltares.keycloak.mocking;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.services.DefaultKeycloakSessionFactory;
import org.keycloak.services.HttpRequestImpl;
import org.keycloak.sessions.AuthenticationSessionModel;

import java.util.List;

public class MockValidationContext implements ValidationContext {

    MultivaluedMap<String, String> decodedFormParameters = new MultivaluedHashMap<>();
    KeycloakSession defaultKeycloakSession ;
    MockRealmModel mockRealmModel = new MockRealmModel();
    EventBuilder eventBuilder ;
    MockUserProvider users;

    public MockValidationContext() {

        users = new MockUserProvider(defaultKeycloakSession, mockRealmModel);
        defaultKeycloakSession = new MockKeycloakSession(new DefaultKeycloakSessionFactory(), mockRealmModel, users);
        eventBuilder = new EventBuilder(mockRealmModel, defaultKeycloakSession);
    }

    @Override
    public void validationError(MultivaluedMap<String, String> multivaluedMap, List<FormMessage> list) {

    }

    @Override
    public void error(String s) {

    }

    @Override
    public void success() {

    }

    @Override
    public void excludeOtherErrors() {

    }

    @Override
    public EventBuilder getEvent() {
        return eventBuilder;
    }

    @Override
    public EventBuilder newEvent() {
        return null;
    }

    @Override
    public AuthenticationExecutionModel getExecution() {
        return null;
    }

    @Override
    public UserModel getUser() {
        return null;
    }

    @Override
    public void setUser(UserModel userModel) {

    }

    @Override
    public RealmModel getRealm() {
        return mockRealmModel;
    }

    @Override
    public AuthenticationSessionModel getAuthenticationSession() {
        return null;
    }

    @Override
    public ClientConnection getConnection() {
        return null;
    }

    @Override
    public UriInfo getUriInfo() {
        return null;
    }

    @Override
    public KeycloakSession getSession() {
        return defaultKeycloakSession;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return new HttpRequestImpl(null){
            @Override
            public MultivaluedMap<String, String> getDecodedFormParameters() {
                return decodedFormParameters;
            }
        };
    }

    @Override
    public AuthenticatorConfigModel getAuthenticatorConfig() {
        return null;
    }
}
