package nl.deltares.keycloak.mocking;

import org.jboss.resteasy.mock.MockHttpRequest;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.jpa.ClientAdapter;
import org.keycloak.models.jpa.entities.ClientEntity;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;
import org.keycloak.services.HttpRequestImpl;

import java.net.URISyntaxException;

public class TestUtils {

    public static MockValidationContext getMockValidationContext() throws URISyntaxException {
        final MockHttpRequest delegate = MockHttpRequest.get("http://localhost:8080/test");
        delegate.addFormHeader("Content-Type","application/x-www-form-urlencoded");
        HttpRequest request = new HttpRequestImpl(delegate);
        MockValidationContext context = new MockValidationContext();
        context.setRequest( request);
        KeycloakSession session = new MockKeycloakSession(new DefaultKeycloakSessionFactory());
        MockRealmModel realm = new MockRealmModel();
        context.setRealm(realm);
        ClientConnection connection = new MockClientConnection();
        EventBuilder eventBuilder = new EventBuilder(realm, session, connection);
        context.setEventBuilder(eventBuilder);
        context.setSession(session);

        return context;

    }

    public static RequiredActionContext getRequiredActionContext(String id) {

        KeycloakSession session = new DefaultKeycloakSession(new DefaultKeycloakSessionFactory());
        MockRealmModel realm = new MockRealmModel();
        MockUserModel userModel = new MockUserModel(session, realm, id);
        MockRequiredActionContext context = new MockRequiredActionContext();
        context.setUserModel(userModel);
        final MockAuthenticationSessionModel authenticationSessionModel = new MockAuthenticationSessionModel();
        final ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientId("test-client");
        clientEntity.setBaseUrl("http://localhost/test-client");
        clientEntity.setEnabled(true);
        final ClientAdapter clientAdapter = new ClientAdapter(realm, null, session, clientEntity);
        authenticationSessionModel.setClient(clientAdapter);
        context.setAuthenticationSession(authenticationSessionModel);
        return context;
    }
}
