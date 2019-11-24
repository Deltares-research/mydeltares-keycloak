package nl.deltares.keycloak.storage.rest;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.keycloak.Config.Scope;

public class AvatarResourceProviderFactory implements RealmResourceProviderFactory {
    private static final Logger logger = Logger.getLogger(AvatarResourceProviderFactory.class);

    protected Properties properties = new Properties();

    @Override
    public RealmResourceProvider create(KeycloakSession keycloakSession) {
        return new AvatarResourceProvider(keycloakSession, properties);
    }

    @Override
    public void init(Scope scope) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/deltares-extention.properties");

        if (is == null) {
            logger.warn("Could not find deltares-extention.properties in classpath");
        } else {
            try {
                properties.load(is);
            } catch (IOException ex) {
                logger.error("Failed to load deltares-extention.properties file", ex);
            }
        }
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
        return "avatar-provider";
    }
}
