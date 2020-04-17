package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class UserDetailsResourceProvider implements RealmResourceProvider {

    private final KeycloakSession keycloakSession;

    public UserDetailsResourceProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }


    @Override
    public Object getResource() {
        UserDetailsResource userDetailsResource = new UserDetailsResource(keycloakSession);
        userDetailsResource.init();
        return userDetailsResource;
    }

    @Override
    public void close() {
        // NOOP
    }

}
