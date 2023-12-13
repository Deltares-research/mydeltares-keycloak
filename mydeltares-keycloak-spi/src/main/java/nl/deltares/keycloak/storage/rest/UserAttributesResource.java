package nl.deltares.keycloak.storage.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import nl.deltares.keycloak.storage.jpa.model.DataRequestManager;
import nl.deltares.keycloak.storage.rest.model.ExportUserAttributes;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;

public class UserAttributesResource {

    final Properties properties;
    final KeycloakSession session;
    final HttpHeaders headers;
    final AdminPermissionEvaluator realmAuth;
    final RealmModel realm;
    final HttpRequest request;
    private final boolean cacheExport;

    UserAttributesResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
        this.headers = session.getContext().getRequestHeaders();
        this.request = session.getContext().getHttpRequest();
        realm = session.getContext().getRealm();

        cacheExport = Boolean.parseBoolean(properties.getOrDefault("cache_export", "true").toString());
        Auth auth = getAuth(headers, session);
        assert auth != null;
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, auth.getRealm(), adminAuth);
    }

    @GET
    @Path("/export")
    @Produces({"text/plain", "text/csv", "application/json"})
    public Response downloadUserAttributes(@QueryParam("search") String search) {
        realmAuth.users().requireManage();
        if (search != null) {
            ExportUserAttributes content = new ExportUserAttributes(realm, session, search.trim());
            return DataRequestManager.getExportDataResponse(content, properties, cacheExport);
        } else {
            return Response.noContent().build();
        }


    }

}
