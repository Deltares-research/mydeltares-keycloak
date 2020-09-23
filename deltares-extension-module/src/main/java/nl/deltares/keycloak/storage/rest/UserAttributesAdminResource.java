package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.model.DataRequestManager;
import nl.deltares.keycloak.storage.rest.model.ExportUserAttributes;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.common.ClientConnection;
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

public class UserAttributesAdminResource {

private final KeycloakSession session;
    private final Properties properties;

    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    //Realm from request path
    private RealmModel callerRealm;
    private boolean cacheExport;

    UserAttributesAdminResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
     }

    public void init() {
        RealmModel realm = session.getContext().getRealm();
        if (realm == null) throw new NotFoundException("Realm not found.");
        Auth auth = getAuth(httpHeaders, session, clientConnection);
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);
        cacheExport = Boolean.parseBoolean(System.getProperty("cache.export", "true"));
        callerRealm = ResourceUtils.getRealmFromPath(session);
    }

    @GET
    @NoCache
    @Path("/export")
    @Produces("text/plain")
    public Response downloadUserAttributes(@QueryParam("search") String search) {
        realmAuth.users().requireManage();
        if (search != null) {
            ExportUserAttributes content = new ExportUserAttributes(callerRealm, session, search.trim());
            return DataRequestManager.getExportDataResponse(content, properties, cacheExport);
        } else {
            return Response.noContent().build();
        }


    }

}
