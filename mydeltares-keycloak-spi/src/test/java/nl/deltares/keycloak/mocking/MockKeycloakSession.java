package nl.deltares.keycloak.mocking;

import org.keycloak.models.KeycloakContext;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserProvider;
import org.keycloak.provider.Provider;
import org.keycloak.representations.userprofile.config.UPConfig;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;
import org.keycloak.userprofile.DeclarativeUserProfileProvider;
import org.keycloak.userprofile.DeclarativeUserProfileProviderFactory;
import org.keycloak.userprofile.UserProfileProvider;

public class MockKeycloakSession extends DefaultKeycloakSession {

    private final DeclarativeUserProfileProvider userProfileProvider;
    UserProvider users;
    public MockKeycloakSession(DefaultKeycloakSessionFactory factory, RealmModel realm, UserProvider users) {
        super(factory);
        this.users = users;
        DeclarativeUserProfileProviderFactory.setDefaultConfig(new UPConfig());
        this.userProfileProvider = new MockUserProfileProvider(this, realm, new DeclarativeUserProfileProviderFactory());
        getContext().setRealm(realm);
    }

    @Override
    public UserProvider users() {
        if (users != null) return users;
        return super.users();
    }

    @Override
    public <T extends Provider> T getProvider(Class<T> clazz) {

        if (clazz.equals(UserProfileProvider.class)){
            return (T) userProfileProvider;
        }
        return super.getProvider(clazz);
    }

    @Override
    public KeycloakContext getContext() {
        return super.getContext();
    }
}
