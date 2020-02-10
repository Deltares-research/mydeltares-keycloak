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
        MailingResource mailingResource = new MailingResource(keycloakSession);
        mailingResource.init();
        return mailingResource;
    }

    @Override
    public void close() {
        // NOOP
    }

}
