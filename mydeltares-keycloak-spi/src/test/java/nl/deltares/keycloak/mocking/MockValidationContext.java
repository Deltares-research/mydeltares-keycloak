package nl.deltares.keycloak.mocking;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.sessions.AuthenticationSessionModel;

import java.util.List;

public class MockValidationContext implements ValidationContext {
    private HttpRequest request;
    private EventBuilder eventBuilder;
    private String error;
    private boolean success;
    private KeycloakSession session;

    private UserModel userModel;
    private RealmModel realmModel;
    private List<FormMessage> errors;
    private MultivaluedMap<String, String> formData;

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public void setEventBuilder(EventBuilder eventBuilder) {
        this.eventBuilder = eventBuilder;
    }

    public void setSession(KeycloakSession session){
        this.session = session;
    }

    @Override
    public void validationError(MultivaluedMap<String, String> formData, List<FormMessage> errors) {
        this.errors = errors;
        this.formData = formData;
    }


    @Override
    public void error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public void success() {
        success = true;
    }

    public boolean isSuccess() {
        return success;
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
        return userModel;
    }

    @Override
    public void setUser(UserModel user) {
        this.userModel = user;
    }

    @Override
    public RealmModel getRealm() {
        return realmModel;
    }

    public void setRealm(RealmModel realmModel) {
        this.realmModel = realmModel;
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
        return session;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return request;
    }

    @Override
    public AuthenticatorConfigModel getAuthenticatorConfig() {
        return null;
    }
}
