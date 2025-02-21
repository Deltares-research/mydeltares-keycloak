package nl.deltares.keycloak.mocking;

import org.hibernate.metamodel.mapping.AttributeMetadata;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.representations.userprofile.config.UPConfig;
import org.keycloak.userprofile.*;

import java.util.Collections;
import java.util.Map;

public class MockUserProfileProvider extends DeclarativeUserProfileProvider {

    private final KeycloakSession session;
    private final RealmModel realm;

    public MockUserProfileProvider(KeycloakSession session, RealmModel realm, DeclarativeUserProfileProviderFactory factory) {
        super(session, factory);
        this.session = session;
        this.realm = realm;
    }

    @Override
    public UserProfile create(UserProfileContext context, Map<String, ?> attributes) {

        UserProfileMetadata defaultMetadata = new UserProfileMetadata(context);

        UserProfileMetadata metadata = this.configureUserProfile(defaultMetadata, this.session);
        metadata.addAttributes(Collections.emptyList());
        DefaultAttributes defaultAttributes = new DefaultAttributes(context, attributes, new MockUserModel(session, realm, "0"), metadata, session);
        return  new MockUserProfile(session, realm, defaultAttributes);
    }

    @Override
    public UPConfig getConfiguration() {
        return super.getConfiguration();
    }
}
