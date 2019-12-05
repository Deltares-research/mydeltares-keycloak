package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import static org.keycloak.Config.Scope;

public class UserMailingResourceProviderFactory implements RealmResourceProviderFactory {

    @Override
    public RealmResourceProvider create(KeycloakSession keycloakSession) {
        return new UserMailingResourceProvider(keycloakSession);
    }

    @Override
    public void init(Scope scope) {
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public String getId() {
        return "user-mailings";
    }
}
