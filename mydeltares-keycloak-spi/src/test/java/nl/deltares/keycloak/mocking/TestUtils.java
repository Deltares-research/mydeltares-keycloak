package nl.deltares.keycloak.mocking;

import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.jpa.ClientAdapter;
import org.keycloak.models.jpa.entities.ClientEntity;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;

public class TestUtils {

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
