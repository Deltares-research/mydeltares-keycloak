package nl.deltares.keycloak.mocking;

import jakarta.persistence.EntityManager;
import org.keycloak.connections.jpa.JpaConnectionProvider;

public class MockJpaConnectionProvider implements JpaConnectionProvider {
    @Override
    public EntityManager getEntityManager() {
        return new MockEntityManager();
    }

    @Override
    public void close() {

    }
}
