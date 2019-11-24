package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class MailingResourceProvider implements RealmResourceProvider {

    private final KeycloakSession keycloakSession;

    public MailingResourceProvider(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }


    @Override
    public Object getResource() {
        return new MailingResource(keycloakSession);
    }

    @Override
    public void close() {
        // NOOP
    }

}
