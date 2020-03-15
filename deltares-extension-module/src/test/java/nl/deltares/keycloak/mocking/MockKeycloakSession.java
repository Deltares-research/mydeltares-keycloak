package nl.deltares.keycloak.mocking;

import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.UserProvider;
import org.keycloak.provider.Provider;
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

    @Override
    public <T extends Provider> T getProvider(Class<T> clazz) {
        if (clazz == JpaConnectionProvider.class){
            return (T) new MockJpaConnectionProvider();
        }
        return super.getProvider(clazz);
    }
}
