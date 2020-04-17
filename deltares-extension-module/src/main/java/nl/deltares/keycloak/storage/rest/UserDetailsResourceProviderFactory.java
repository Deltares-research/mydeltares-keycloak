package nl.deltares.keycloak.storage.rest;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import static org.keycloak.Config.Scope;

public class UserDetailsResourceProviderFactory implements RealmResourceProviderFactory {

//    private Properties properties = new Properties();

    @Override
    public RealmResourceProvider create(KeycloakSession keycloakSession) {
        return new UserDetailsResourceProvider(keycloakSession);
    }

    @Override
    public void init(Scope scope) {
//        properties = getResourceProperties("deltares-extention.properties");
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
        return "user-details";
    }
}
