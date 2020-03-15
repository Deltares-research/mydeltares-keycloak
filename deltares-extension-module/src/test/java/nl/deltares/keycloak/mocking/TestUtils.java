package nl.deltares.keycloak.mocking;

import org.jboss.resteasy.mock.MockHttpRequest;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;

import java.net.URISyntaxException;

public class TestUtils {

    public static MockValidationContext getMockValidationContext() throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.get("http://localhost:8080/test");
        MockValidationContext context = new MockValidationContext();
        context.setRequest(request);
        KeycloakSession session = new MockKeycloakSession(new DefaultKeycloakSessionFactory());
        MockRealmModel realm = new MockRealmModel();
        ClientConnection connection = new MockClientConnection();
        EventBuilder eventBuilder = new EventBuilder(realm, session, connection);
        context.setEventBuilder(eventBuilder);
        context.setSession(session);

        return context;
    }
}
