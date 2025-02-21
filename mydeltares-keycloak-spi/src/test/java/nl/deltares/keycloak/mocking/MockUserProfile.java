package nl.deltares.keycloak.mocking;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.AbstractUserRepresentation;
import org.keycloak.userprofile.AttributeChangeListener;
import org.keycloak.userprofile.Attributes;
import org.keycloak.userprofile.UserProfile;
import org.keycloak.userprofile.ValidationException;

import java.util.Map;

public class MockUserProfile implements UserProfile {

    private final Attributes attributes;
    private final KeycloakSession session;
    private final RealmModel realm;

    public MockUserProfile(KeycloakSession session, RealmModel realm, Attributes attributes) {
        this.session = session;
        this.realm = realm;
        this.attributes = attributes;
    }

    @Override
    public void validate() throws ValidationException {

    }

    @Override
    public UserModel create() throws ValidationException {
        return new MockUserModel(session, realm, "0");
    }

    @Override
    public void update(boolean b, AttributeChangeListener... attributeChangeListeners) throws ValidationException {

    }

    @Override
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public <R extends AbstractUserRepresentation> R toRepresentation() {
        return null;
    }
}
