package nl.deltares.keycloak.mocking;

import org.keycloak.models.UserProvider;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;

public class MockKeycloakSession extends DefaultKeycloakSession {

    private MockUserProvider provider;
    public MockKeycloakSession(DefaultKeycloakSessionFactory factory) {
        super(factory);
    }

    @Override
    public UserProvider users() {
        return userLocalStorage();
    }

    @Override
    public UserProvider userLocalStorage() {
        if (provider == null) provider = new MockUserProvider(this);
        return provider;
    }
}
