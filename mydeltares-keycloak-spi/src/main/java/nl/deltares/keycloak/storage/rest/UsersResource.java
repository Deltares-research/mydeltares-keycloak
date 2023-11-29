package nl.deltares.keycloak.storage.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.deltares.keycloak.storage.jpa.model.DataRequestManager;
import nl.deltares.keycloak.storage.rest.model.ExportInvalidUser;
import nl.deltares.keycloak.storage.rest.model.ExtractNonKeycloakUsers;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;

public class UsersResource {

    private static final String DATA_PARAMETER = "data";
    private File tempDir;

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
        Auth auth = ResourceUtils.getAuth(httpHeaders, session);
        assert auth != null;
        AdminAuth adminAuth = new AdminAuth(auth.getRealm(), auth.getToken(), auth.getUser(), auth.getClient());
        realmAuth = AdminPermissions.evaluator(session, realm, adminAuth);
        session.getContext().setRealm(realm);
        callerRealm = ResourceUtils.getRealmFromPath(session);
    }

    @GET
    @NoCache
    @Path("/invalid{p : /?}{id : (.+)?}")
    @Produces({"text/plain", "text/csv", "application/json"} )
    public Response exportInvalidUsers(final @PathParam("id") String id) {

        realmAuth.users().requireQuery();
        if (id.isEmpty()) {
            ExportInvalidUser content = new ExportInvalidUser(callerRealm, session);
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
    public Response checkIfUsersExist(MultipartFormDataInput input) {

        realmAuth.users().requireQuery();
        File temp;
        try {
            temp = saveInputFile(input);
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getMessage()).type(MediaType.TEXT_PLAIN).build();
        }
        ExtractNonKeycloakUsers content = new ExtractNonKeycloakUsers(callerRealm, session, temp);
        return DataRequestManager.getExportDataResponse(content, properties, false);
    }

    private File saveInputFile(MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> formDataMap = input.getFormDataMap();
        List<InputPart> inputParts = formDataMap.get(DATA_PARAMETER);
        if (inputParts == null || inputParts.isEmpty()) {
            throw new IllegalArgumentException("Missing data file");
        }
        InputPart inputPart = inputParts.get(0);
        File temp = new File(getExportDir(), "non-existing-users-" + realmAuth.adminAuth().getUser().getId());
        try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
            if (inputStream.available() == 0) {
                return null; //save pressed when no image selected
            }
            Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return temp;
    }

    private File getExportDir() throws IOException {
        if (tempDir != null) return tempDir;
        String property = System.getProperty("jboss.server.temp.dir");
        if (property == null) {
            throw new IOException("Missing system property: jboss.server.temp.dir");
        }
        tempDir = new File(property, "deltares");
        if (!tempDir.exists()) Files.createDirectory(tempDir.toPath());
        return tempDir;
    }
}
