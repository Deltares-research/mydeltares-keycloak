package nl.deltares.keycloak.mocking;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.adapter.InMemoryUserAdapter;

public class MockUserModel extends InMemoryUserAdapter {
    public MockUserModel(KeycloakSession session, RealmModel realm, String id) {
        super(session, realm, id);
    }

}
