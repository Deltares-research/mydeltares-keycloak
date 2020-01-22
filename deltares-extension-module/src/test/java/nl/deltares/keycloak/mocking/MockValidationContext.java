package nl.deltares.keycloak.mocking;

import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.sessions.AuthenticationSessionModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;

public class MockValidationContext implements ValidationContext {
    private HttpRequest request;
    private EventBuilder eventBuilder;
    private String error;
    private MultivaluedMap<String, String> formData;
    private List<FormMessage> errors;
    private boolean success;

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public void setEventBuilder(EventBuilder eventBuilder) {
        this.eventBuilder = eventBuilder;
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

    public MultivaluedMap<String, String> getFormData() {
        return formData;
    }

    public List<FormMessage> getErrors() {
        return errors;
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
    public void setUser(UserModel user) {

    }

    @Override
    public RealmModel getRealm() {
        return null;
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
        return null;
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
