package nl.deltares.keycloak.storage.jpa;

import org.keycloak.Config;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class JpaMailingStorageProviderFactory implements JpaEntityProviderFactory {


    protected static final String ID = "mailing-entity-provider";

    @Override
    public JpaEntityProvider create(KeycloakSession keycloakSession) {
        return new JpaMailingStorageProvider();
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public String getId() {
        return ID;
    }

}
