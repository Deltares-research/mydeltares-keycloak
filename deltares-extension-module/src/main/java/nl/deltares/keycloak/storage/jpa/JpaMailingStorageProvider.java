package nl.deltares.keycloak.storage.jpa;

import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

import java.util.Collections;
import java.util.List;

public class JpaMailingStorageProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return Collections.<Class<?>>singletonList(Mailing.class);
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/mailing-changelog.xml";
    }

    @Override
    public String getFactoryId() {
        return JpaMailingStorageProviderFactory.ID;
    }

    @Override
    public void close() {

    }
}
