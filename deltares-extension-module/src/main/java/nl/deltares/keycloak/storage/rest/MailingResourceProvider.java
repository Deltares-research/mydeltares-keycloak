package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.Properties;

public class MailingResourceProvider implements RealmResourceProvider {

    private final KeycloakSession keycloakSession;
    private final Properties properties;

    public MailingResourceProvider(KeycloakSession keycloakSession, Properties properties) {
        this.keycloakSession = keycloakSession;
        this.properties = properties;
    }


    @Override
    public Object getResource() {
        MailingResource mailingResource = new MailingResource(keycloakSession, properties);
        mailingResource.init();
        return mailingResource;
    }

    @Override
    public void close() {
        // NOOP
    }

}
