package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class UserMailingResourceProvider implements RealmResourceProvider {

    private final KeycloakSession keycloakSession;

    public UserMailingResourceProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }


    @Override
    public Object getResource() {
        UserMailingResource userMailingResource = new UserMailingResource(keycloakSession);
        userMailingResource.init();
        return userMailingResource;
    }

    @Override
    public void close() {
        // NOOP
    }

}
