package nl.deltares.keycloak.mocking;

import nl.deltares.keycloak.storage.rest.UserAttributesResourceProvider;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.UserProvider;
import org.keycloak.provider.Provider;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;
import org.keycloak.userprofile.DeclarativeUserProfileProvider;
import org.keycloak.userprofile.UserProfileContext;
import org.keycloak.userprofile.UserProfileMetadata;
import org.keycloak.userprofile.UserProfileProvider;
import org.keycloak.userprofile.validator.BlankAttributeValidator;
import org.keycloak.validate.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MockKeycloakSession extends DefaultKeycloakSession {

    private MockUserProvider provider;

    public MockKeycloakSession(DefaultKeycloakSessionFactory factory) {
        super(factory);
    }

    @Override
    public UserProvider users() {
        return userLocalStorage();
    }

    public UserProvider userLocalStorage() {
        if (provider == null) provider = new MockUserProvider(this);
        return provider;
    }

    @Override
    public KeycloakContext getContext() {
        return super.getContext();
    }

    @Override
    public <T extends Provider> T getProvider(Class<T> clazz) {
        if (clazz == JpaConnectionProvider.class) {
            return (T) new MockJpaConnectionProvider();
        }
        if (clazz == UserProfileProvider.class) {
            Map<UserProfileContext, UserProfileMetadata> metadataRegistry = new HashMap<>();
            final UserProfileMetadata userProfileMetadata = new UserProfileMetadata(UserProfileContext.REGISTRATION);
            metadataRegistry.put(UserProfileContext.REGISTRATION, userProfileMetadata);
            final DeclarativeUserProfileProvider declarativeUserProfileProvider = new DeclarativeUserProfileProvider(this, metadataRegistry, null, null);
            return (T) declarativeUserProfileProvider.create(this);
        }

        return super.getProvider(clazz);
    }

    @Override
    public <T extends Provider> T getProvider(Class<T> clazz, String id) {

        if (clazz == Validator.class && id.equals(BlankAttributeValidator.ID)) {
            return (T) new BlankAttributeValidator();
        }
        return super.getProvider(clazz, id);
    }
}
