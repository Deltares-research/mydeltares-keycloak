package nl.deltares.keycloak.storage.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.deltares.keycloak.storage.jpa.model.DataRequestManager;
import nl.deltares.keycloak.storage.rest.model.ExportInvalidUser;
import nl.deltares.keycloak.storage.rest.model.ExtractNonKeycloakUsers;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;

public class UsersResource {
    private File tempDir;

    final Properties properties;
    final KeycloakSession session;
    final HttpHeaders headers;
    final AdminPermissionEvaluator realmAuth;
    final RealmModel realm;
    final HttpRequest request;

    public UsersResource(KeycloakSession session, Properties properties) {
        this.session = session;
        this.properties = properties;
        this.headers = session.getContext().getRequestHeaders();
        this.request = session.getContext().getHttpRequest();
        realm = session.getContext().getRealm();

        Auth auth = getAuth(headers, session);
        assert auth != null;
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, auth.getRealm(), adminAuth);
    }

    @GET
    @NoCache
    @Path("/invalid{p : /?}{id : (.+)?}")
    @Produces({"text/plain", "text/csv", "application/json"} )
    public Response exportInvalidUsers(final @PathParam("id") String id) {

        realmAuth.users().requireQuery();
        if (id.isEmpty()) {
            ExportInvalidUser content = new ExportInvalidUser(realm, session);
            return DataRequestManager.getExportDataResponse(content, properties, false);
        } else {
            return DataRequestManager.getExportDataResponse(id);
        }
    }

    @GET
    @Path("/check-users-exist/{id}")
    @Produces({"text/plain", "text/csv", "application/json"} )
    public Response checkIfUsersExist(final @PathParam("id") String id) {
        realmAuth.users().requireQuery();
        return DataRequestManager.getExportDataResponse(id);
    }

    @POST
    @Path("/check-users-exist")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"text/plain", "text/csv", "application/json"} )
    public Response checkIfUsersExist(@RestForm("data") FileUpload file) {

        realmAuth.users().requireQuery();
        File temp;
        try {
            temp = saveInputFile(file);
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        ExtractNonKeycloakUsers content = new ExtractNonKeycloakUsers(realm, session, temp);
        return DataRequestManager.getExportDataResponse(content, properties, false);
    }

    private File saveInputFile(FileUpload input) throws IOException {

        File temp;
        try(InputStream inputStream = new FileInputStream(input.uploadedFile().toFile())){
            if (inputStream.available() == 0) {
                return null; //save pressed when no image selected
            }
             temp = new File(getExportDir(), "non-existing-users-" + realmAuth.adminAuth().getUser().getId());
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return temp;
    }

    private File getExportDir() throws IOException {
        if (tempDir != null) return tempDir;
        String property = System.getProperty("java.io.tmpdir");
        if (property == null) {
            throw new IOException("Missing system property: java.io.tmpdir");
        }
        tempDir = new File(property, "deltares");
        if (!tempDir.exists()) Files.createDirectory(tempDir.toPath());
        return tempDir;
    }
}
