package nl.deltares.keycloak.mocking;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.*;
import org.keycloak.representations.userprofile.config.UPConfig;
import org.keycloak.userprofile.AbstractUserProfileProvider;
import org.keycloak.userprofile.UserProfile;
import org.keycloak.userprofile.UserProfileContext;
import org.keycloak.userprofile.UserProfileProvider;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class MockUserProfileProvider extends AbstractUserProfileProvider {

    @Override
    protected UserProfileProvider create(KeycloakSession session, Map metadataRegistry) {
        return new MockUserProfileProvider();
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public int order() {
        return super.order();
    }

    @Override
    public boolean isEnabled(RealmModel realm) {
        return false;
    }
}