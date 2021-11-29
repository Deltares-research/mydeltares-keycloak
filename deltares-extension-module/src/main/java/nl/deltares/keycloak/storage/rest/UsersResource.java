package nl.deltares.keycloak.storage.rest;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;
import org.keycloak.services.resources.admin.permissions.UserPermissionEvaluator;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;

public class UsersResource {

    private static final Logger logger = Logger.getLogger(UsersResource.class);

    private final KeycloakSession session;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;
    private AdminPermissionEvaluator realmAuth;
    private RealmModel callerRealm;

    public UsersResource(KeycloakSession session, Properties properties) {
        this.session = session;
        ResteasyProviderFactory.getInstance().injectProperties(this);
    }

    public void init() {
        RealmModel realm = session.getContext().getRealm();
        if (realm == null) throw new NotFoundException("Realm not found.");
        Auth auth = getAuth(httpHeaders, session, clientConnection);
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);
        callerRealm = ResourceUtils.getRealmFromPath(session);
    }

    @GET
    @NoCache
    @Path("/disabled")
    @Produces({"application/json"})
    public List<UserRepresentation> getDisabledUsers(@QueryParam("first") Integer firstResult, @QueryParam("max") Integer maxResults,
                                                     @QueryParam("briefRepresentation") Boolean briefRepresentation,
                                                     @QueryParam("disabledTimeAfter") Long disabledAfterMillis,
                                                     @QueryParam("disabledTimeBefore") Long disabledBeforeMillis) {
        UserPermissionEvaluator userPermissionEvaluator = this.realmAuth.users();
        userPermissionEvaluator.requireQuery();
        firstResult = firstResult != null ? firstResult : -1;
        maxResults = maxResults != null ? maxResults : 100;

        disabledBeforeMillis = disabledBeforeMillis != null ? disabledBeforeMillis : Long.MAX_VALUE;
        disabledAfterMillis = disabledAfterMillis != null ? disabledAfterMillis : Long.MIN_VALUE;
        return searchForDisabledUsers(callerRealm, userPermissionEvaluator, briefRepresentation,
                firstResult, maxResults, false, disabledAfterMillis, disabledBeforeMillis);

    }

    private List<UserRepresentation> searchForDisabledUsers(RealmModel realm, UserPermissionEvaluator usersEvaluator,
                                                            Boolean briefRepresentation, Integer firstResult, Integer maxResults,
                                                            Boolean includeServiceAccounts,
                                                            Long disabledAfterMillis, Long disabledBeforeMillis) {
        this.session.setAttribute("keycloak.session.realm.users.query.include_service_account", includeServiceAccounts);
        realmAuth.users().requireQuery();

        final UserProvider users = this.session.users();
        final List<UserModel> disabledUsers = new ArrayList<>(maxResults);

        int disabledUserCount = 0;
        int index = firstResult;
        while (disabledUsers.size() < maxResults) {
            final List<UserModel> allUsers = users.getUsers(realm, index, 100, includeServiceAccounts);
            if (allUsers.size() == 0) break;
            for (UserModel aUser : allUsers) {
                index++;
                if (aUser.isEnabled()) continue;
                if (!isValidPeriod(aUser, disabledAfterMillis, disabledBeforeMillis)) continue;
                if (disabledUserCount < firstResult) {
                    disabledUserCount++;
                    continue;
                }
                disabledUsers.add(aUser);
                if (disabledUsers.size() == maxResults) break;
            }
        }
        return toRepresentation(realm, usersEvaluator, briefRepresentation, disabledUsers);
    }

    private boolean isValidPeriod(UserModel aUser, Long disabledAfterMillis, Long disabledBeforeMillis) {
        final List<String> disableTime = aUser.getAttribute("disabledTime");
        if (disableTime == null || disableTime.size() == 0) return true;

        final String timeStamp = disableTime.get(0);
        try {
            final long disabledTime = dateFormat.parse(timeStamp).getTime();
            return disabledAfterMillis < disabledTime && disabledTime < disabledBeforeMillis;
        } catch (ParseException e) {
            logger.error(String.format("Failed to parse disabledTime %s for user %s: %s", timeStamp, aUser.getEmail(), e.getMessage()));
            return true;
        }

    }

    private List<UserRepresentation> toRepresentation(RealmModel realm, UserPermissionEvaluator usersEvaluator, Boolean briefRepresentation, List<UserModel> userModels) {
        boolean briefRepresentationB = briefRepresentation != null && briefRepresentation;
        List<UserRepresentation> results = new ArrayList<>();
        boolean canViewGlobal = usersEvaluator.canView();
        usersEvaluator.grantIfNoPermission(this.session.getAttribute("keycloak.session.realm.users.query.groups") != null);
        Iterator var8 = userModels.iterator();

        while (true) {
            UserModel user;
            do {
                if (!var8.hasNext()) {
                    return results;
                }

                user = (UserModel) var8.next();
            } while (!canViewGlobal && !usersEvaluator.canView(user));

            UserRepresentation userRep = briefRepresentationB ? ModelToRepresentation.toBriefRepresentation(user) : ModelToRepresentation.toRepresentation(this.session, realm, user);
            userRep.setAccess(usersEvaluator.getAccess(user));
            results.add(userRep);
        }
    }
}
