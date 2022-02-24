package nl.deltares.keycloak.mocking;

import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.sessions.AuthenticationSessionModel;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class MockRequiredActionContext implements RequiredActionContext {

    private UserModel userModel;
    private MockAuthenticationSessionModel authenticationSession;

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public URI getActionUrl(String code) {
        return null;
    }

    @Override
    public URI getActionUrl() {
        return null;
    }

    @Override
    public URI getActionUrl(boolean authSessionIdParam) {
        return null;
    }

    @Override
    public LoginFormsProvider form() {
        return null;
    }

    @Override
    public Response getChallenge() {
        return null;
    }

    @Override
    public EventBuilder getEvent() {
        return null;
    }

    @Override
    public UserModel getUser() {
        return userModel;
    }

    @Override
    public RealmModel getRealm() {
        return null;
    }

    @Override
    public AuthenticationSessionModel getAuthenticationSession() {
        return authenticationSession;
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
        return null;
    }

    @Override
    public String generateCode() {
        return null;
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public void challenge(Response response) {

    }

    @Override
    public void failure() {

    }

    @Override
    public void success() {

    }

    @Override
    public void ignore() {

    }

    public void setAuthenticationSession(MockAuthenticationSessionModel authenticationSessionModel) {
        this.authenticationSession = authenticationSessionModel;
    }
}
