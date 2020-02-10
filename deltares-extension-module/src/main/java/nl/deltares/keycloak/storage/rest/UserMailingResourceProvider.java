package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.Properties;

public class UserMailingResourceProvider implements RealmResourceProvider {

    private final KeycloakSession keycloakSession;
    private final Properties properties;

    public UserMailingResourceProvider(KeycloakSession keycloakSession, Properties properties) {
        this.keycloakSession = keycloakSession;
        this.properties = properties;
    }


    @Override
    public Object getResource() {
        UserMailingResource userMailingResource = new UserMailingResource(keycloakSession, properties);
        userMailingResource.init();
        return userMailingResource;
    }

    @Override
    public void close() {
        // NOOP
    }

}
