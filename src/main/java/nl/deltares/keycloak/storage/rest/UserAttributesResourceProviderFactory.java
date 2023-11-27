package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getResourceProperties;
import static org.keycloak.Config.Scope;

public class UserAttributesResourceProviderFactory implements RealmResourceProviderFactory {

    private Properties properties = new Properties();
    @Override
    public RealmResourceProvider create(KeycloakSession keycloakSession) {
        return new UserAttributesResourceProvider(keycloakSession, properties);
    }

    @Override
    public void init(Scope scope) {
        properties = ResourceUtils.getResourceProperties("deltares-extention.properties");
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
        return "user-attributes";
    }
}
