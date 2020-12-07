package nl.deltares.keycloak.mocking;

import org.keycloak.connections.jpa.JpaConnectionProvider;

import javax.persistence.EntityManager;

public class MockJpaConnectionProvider implements JpaConnectionProvider {
    @Override
    public EntityManager getEntityManager() {
        return new MockEntityManager();
    }

    @Override
    public void close() {

    }
}
