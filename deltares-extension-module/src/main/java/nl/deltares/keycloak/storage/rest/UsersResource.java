package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.model.DataRequestManager;
import nl.deltares.keycloak.storage.rest.model.ExportDisabledUser;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;

@Deprecated
public class UsersResource {

    private final KeycloakSession session;
    private final Properties properties;

    @Context
    private HttpHeaders httpHeaders;

    private AdminPermissionEvaluator realmAuth;

    //Realm from request path
    private RealmModel callerRealm;

    public UsersResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
        ResteasyProviderFactory.getInstance().injectProperties(this);
    }

    public void init() {
        RealmModel realm = session.getContext().getRealm();
        if (realm == null) throw new NotFoundException("Realm not found.");
        Auth auth = getAuth(httpHeaders, session);
        assert auth != null;
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);
        callerRealm = ResourceUtils.getRealmFromPath(session);
    }

    @GET
    @NoCache
    @Path("/disabled")
    @Produces("text/plain")
    public Response exportDisabledUsers(@QueryParam("disabledTimeAfter") Long disabledAfterMillis,
                                        @QueryParam("disabledTimeBefore") Long disabledBeforeMillis) {

        realmAuth.users().requireQuery();
        ExportDisabledUser content = new ExportDisabledUser(callerRealm, session, disabledAfterMillis, disabledBeforeMillis);
        return DataRequestManager.getExportDataResponse(content, properties, false);
    }
}
