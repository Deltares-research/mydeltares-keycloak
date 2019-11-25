package nl.deltares.keycloak.storage.jpa;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

import java.util.Collections;
import java.util.List;

public class JpaUserMailingStorageProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return Collections.<Class<?>>singletonList(UserMailing.class);
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/usermailing-changelog.xml";
    }

    @Override
    public String getFactoryId() {
        return JpaUserMailingStorageProviderFactory.ID;
    }

    @Override
    public void close() {

    }
}
