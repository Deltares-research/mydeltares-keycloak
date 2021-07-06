package nl.deltares.keycloak.mocking;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.*;

import java.util.*;
import java.util.stream.Stream;

public class MockUserProvider implements UserProvider {

    private ArrayList<UserModel> users = new ArrayList<>();

    private final KeycloakSession session;

    public MockUserProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void addFederatedIdentity(RealmModel realm, UserModel user, FederatedIdentityModel socialLink) {

    }

    @Override
    public boolean removeFederatedIdentity(RealmModel realm, UserModel user, String socialProvider) {
        return false;
    }

    @Override
    public void updateFederatedIdentity(RealmModel realm, UserModel federatedUser, FederatedIdentityModel federatedIdentityModel) {

    }

    @Override
    public Set<FederatedIdentityModel> getFederatedIdentities(UserModel user, RealmModel realm) {
        return null;
    }

    @Override
    public Stream<FederatedIdentityModel> getFederatedIdentitiesStream(RealmModel realm, UserModel user) {
        return UserProvider.super.getFederatedIdentitiesStream(realm, user);
    }

    @Override
    public FederatedIdentityModel getFederatedIdentity(RealmModel realm, UserModel user, String socialProvider) {
        return UserProvider.super.getFederatedIdentity(realm, user, socialProvider);
    }

    @Override
    public FederatedIdentityModel getFederatedIdentity(UserModel user, String socialProvider, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel getUserByFederatedIdentity(RealmModel realm, FederatedIdentityModel socialLink) {
        return UserProvider.super.getUserByFederatedIdentity(realm, socialLink);
    }

    @Override
    public UserModel getUserByFederatedIdentity(FederatedIdentityModel socialLink, RealmModel realm) {
        return null;
    }

    @Override
    public void addConsent(RealmModel realm, String userId, UserConsentModel consent) {

    }

    @Override
    public UserConsentModel getConsentByClient(RealmModel realm, String userId, String clientInternalId) {
        return null;
    }

    @Override
    public List<UserConsentModel> getConsents(RealmModel realm, String userId) {
        return null;
    }

    @Override
    public Stream<UserConsentModel> getConsentsStream(RealmModel realm, String userId) {
        return UserProvider.super.getConsentsStream(realm, userId);
    }

    @Override
    public void updateConsent(RealmModel realm, String userId, UserConsentModel consent) {

    }

    @Override
    public boolean revokeConsentForClient(RealmModel realm, String userId, String clientInternalId) {
        return false;
    }

    @Override
    public void setNotBeforeForUser(RealmModel realm, UserModel user, int notBefore) {

    }

    @Override
    public int getNotBeforeOfUser(RealmModel realm, UserModel user) {
        return 0;
    }

    @Override
    public UserModel getServiceAccount(ClientModel client) {
        return null;
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, boolean includeServiceAccounts) {
        return null;
    }

    @Override
    public Stream<UserModel> getUsersStream(RealmModel realm, boolean includeServiceAccounts) {
        return UserProvider.super.getUsersStream(realm, includeServiceAccounts);
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults, boolean includeServiceAccounts) {
        return null;
    }

    @Override
    public Stream<UserModel> getUsersStream(RealmModel realm, Integer firstResult, Integer maxResults, boolean includeServiceAccounts) {
        return UserProvider.super.getUsersStream(realm, firstResult, maxResults, includeServiceAccounts);
    }

    @Override
    public UserModel addUser(RealmModel realm, String id, String username, boolean addDefaultRoles, boolean addDefaultRequiredActions) {
        return null;
    }

    @Override
    public void preRemove(RealmModel realm) {

    }

    @Override
    public void preRemove(RealmModel realmModel, IdentityProviderModel identityProviderModel) {

    }

    @Override
    public void removeImportedUsers(RealmModel realm, String storageProviderId) {

    }

    @Override
    public void unlinkUsers(RealmModel realm, String storageProviderId) {

    }

    @Override
    public void preRemove(RealmModel realm, RoleModel role) {

    }

    @Override
    public void preRemove(RealmModel realm, GroupModel group) {

    }

    @Override
    public void preRemove(RealmModel realm, ClientModel client) {

    }

    @Override
    public void preRemove(ProtocolMapperModel protocolMapper) {

    }

    @Override
    public void preRemove(ClientScopeModel clientScope) {

    }

    @Override
    public void close() {

    }

    @Override
    public void preRemove(RealmModel realm, ComponentModel component) {

    }

    @Override
    public void grantToAllUsers(RealmModel realm, RoleModel role) {

    }

    @Override
    public UserModel getUserById(String id, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realm) {
        for (UserModel user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realm) {
        return null;
    }

    @Override
    public int getUsersCount(RealmModel realm) {
        return users.size();
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm) {
        return users;
    }

    @Override
    public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(String search, RealmModel realm, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
        return null;
    }

    @Override
    public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult, int maxResults) {
        return null;
    }

    @Override
    public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
        return null;
    }

    @Override
    public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue, RealmModel realm) {
        return null;
    }

    @Override
    public UserModel addUser(RealmModel realm, String username) {

        UserModel user = getUserByUsername(username, realm);
        if (user == null) {
            user = new MockUserModel(session, realm, String.valueOf(users.size()));
        }
        user.setUsername(username);
        users.add(user);
        return user;
    }

    @Override
    public boolean removeUser(RealmModel realm, UserModel user) {
        return false;
    }
}
