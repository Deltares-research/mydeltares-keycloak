package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.Properties;

public class UsersResourceProvider implements RealmResourceProvider {

    private final KeycloakSession keycloakSession;
    private final Properties properties;

    public UsersResourceProvider(KeycloakSession keycloakSession, Properties properties) {
        this.keycloakSession = keycloakSession;
        this.properties = properties;
    }


    @Override
    public Object getResource() {
        return new UsersResource(keycloakSession, properties);
    }

    @Override
    public void close() {
        // NOOP
    }

}
