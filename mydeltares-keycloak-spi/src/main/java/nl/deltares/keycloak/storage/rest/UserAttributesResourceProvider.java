package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.Properties;

public class UserAttributesResourceProvider implements RealmResourceProvider {

    private final KeycloakSession keycloakSession;
    private final Properties properties;

    public UserAttributesResourceProvider(KeycloakSession keycloakSession, Properties properties) {
        this.keycloakSession = keycloakSession;
        this.properties = properties;
    }


    @Override
    public Object getResource() {
        return new UserAttributesResource(keycloakSession, properties);
    }

    @Override
    public void close() {
        // NOOP
    }

}
