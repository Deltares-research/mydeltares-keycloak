package nl.deltares.keycloak.storage.jpa;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

import java.util.Collections;
import java.util.List;

public class JpaAvatarStorageProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return Collections.<Class<?>>singletonList(Avatar.class);
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/avatar-changelog.xml";
    }

    @Override
    public String getFactoryId() {
        return JpaAvatarStorageProviderFactory.ID;
    }

    @Override
    public void close() {

    }
}
