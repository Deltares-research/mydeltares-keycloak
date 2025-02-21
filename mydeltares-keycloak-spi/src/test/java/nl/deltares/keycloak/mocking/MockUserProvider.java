package nl.deltares.keycloak.mocking;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class MockUserProvider implements UserProvider {

    KeycloakSession session;
    RealmModel realm;

    List<UserModel> users = new ArrayList<>();

    public MockUserProvider(KeycloakSession session, RealmModel realm) {
        this.session = session;
        this.realm = realm;
    }

    @Override
    public void setNotBeforeForUser(RealmModel realmModel, UserModel userModel, int i) {

    }

    @Override
    public int getNotBeforeOfUser(RealmModel realmModel, UserModel userModel) {
        return 0;
    }

    @Override
    public UserModel getServiceAccount(ClientModel clientModel) {
        return null;
    }

    @Override
    public UserModel addUser(RealmModel realmModel, String id, String userName, boolean b, boolean b1) {

        MockUserModel user = new MockUserModel(session, realm, id);
        user.setUsername(userName);
        users.add(user);
        return user;
    }

    @Override
    public void removeImportedUsers(RealmModel realmModel, String s) {

    }

    @Override
    public void unlinkUsers(RealmModel realmModel, String s) {

    }

    @Override
    public void addConsent(RealmModel realmModel, String s, UserConsentModel userConsentModel) {

    }

    @Override
    public UserConsentModel getConsentByClient(RealmModel realmModel, String s, String s1) {
        return null;
    }

    @Override
    public Stream<UserConsentModel> getConsentsStream(RealmModel realmModel, String s) {
        return Stream.empty();
    }

    @Override
    public void updateConsent(RealmModel realmModel, String s, UserConsentModel userConsentModel) {

    }

    @Override
    public boolean revokeConsentForClient(RealmModel realmModel, String s, String s1) {
        return false;
    }

    @Override
    public void addFederatedIdentity(RealmModel realmModel, UserModel userModel, FederatedIdentityModel federatedIdentityModel) {

    }

    @Override
    public boolean removeFederatedIdentity(RealmModel realmModel, UserModel userModel, String s) {
        return false;
    }

    @Override
    public void updateFederatedIdentity(RealmModel realmModel, UserModel userModel, FederatedIdentityModel federatedIdentityModel) {

    }

    @Override
    public Stream<FederatedIdentityModel> getFederatedIdentitiesStream(RealmModel realmModel, UserModel userModel) {
        return Stream.empty();
    }

    @Override
    public FederatedIdentityModel getFederatedIdentity(RealmModel realmModel, UserModel userModel, String s) {
        return null;
    }

    @Override
    public UserModel getUserByFederatedIdentity(RealmModel realmModel, FederatedIdentityModel federatedIdentityModel) {
        return null;
    }

    @Override
    public void preRemove(RealmModel realmModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, IdentityProviderModel identityProviderModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, RoleModel roleModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, GroupModel groupModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, ClientModel clientModel) {

    }

    @Override
    public void preRemove(ProtocolMapperModel protocolMapperModel) {

    }

    @Override
    public void preRemove(ClientScopeModel clientScopeModel) {

    }

    @Override
    public void preRemove(RealmModel realmModel, ComponentModel componentModel) {

    }

    @Override
    public void close() {

    }

    @Override
    public void grantToAllUsers(RealmModel realmModel, RoleModel roleModel) {

    }

    @Override
    public UserModel getUserById(RealmModel realmModel, String s) {
        Optional<UserModel> first = users.stream().filter(userModel -> userModel.getId().equals(s)).findFirst();
        return first.orElse(null);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realmModel, String s) {
        Optional<UserModel> first = users.stream().filter(userModel -> userModel.getUsername().equals(s)).findFirst();
        return first.orElse(null);
    }

    @Override
    public UserModel getUserByEmail(RealmModel realmModel, String s) {
        Optional<UserModel> first = users.stream().filter(userModel -> userModel.getEmail().equals(s)).findFirst();
        return first.orElse(null);
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realmModel, Map<String, String> map, Integer integer, Integer integer1) {
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realmModel, GroupModel groupModel, Integer integer, Integer integer1) {
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realmModel, String s, String s1) {
        return Stream.empty();
    }

    @Override
    public UserModel addUser(RealmModel realmModel, String s) {
        return null;
    }

    @Override
    public boolean removeUser(RealmModel realmModel, UserModel userModel) {
        return false;
    }
}
