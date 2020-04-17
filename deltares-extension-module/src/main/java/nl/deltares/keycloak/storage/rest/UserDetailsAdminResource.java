package nl.deltares.keycloak.storage.rest;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.ErrorResponse;
import org.keycloak.services.managers.Auth;
import org.keycloak.services.resources.admin.AdminAuth;
import org.keycloak.services.resources.admin.permissions.AdminPermissionEvaluator;
import org.keycloak.services.resources.admin.permissions.AdminPermissions;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuth;
import static nl.deltares.keycloak.storage.rest.UserDetailsResource.*;

public class UserDetailsAdminResource {

    private final KeycloakSession session;

    private AdminPermissionEvaluator realmAuth;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection clientConnection;

    //Realm from request path
    private RealmModel callerRealm;

    UserDetailsAdminResource(KeycloakSession session) {
        this.session = session;
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
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails(final @PathParam("email") String email) {
        realmAuth.users().requireQuery();
        UserModel user = session.users().getUserByEmail(email, callerRealm);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found for email: " + email).build();
        }
        return Response.ok(toRepresentation(user), MediaType.APPLICATION_JSON).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadUserDetails(final UserRepresentation rep){
        realmAuth.users().requireManage();

        UserModel user = null;
        String id = rep.getId();
        if (id != null){
            user = session.users().getUserById(id, callerRealm);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found for id: " + id).build();
            }
        }
        String email = rep.getEmail();
        if (user == null && email != null){
            user = session.users().getUserByEmail(email, callerRealm);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found for email: " + email).build();
            }
        }
        String username = rep.getUsername();
        if (user == null && username != null){
            user = session.users().getUserByUsername(username, callerRealm);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found for username: " + username).build();
            }
        }

        try {
            validateUser(rep, user, session);
        } catch (IOException e) {
            return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
        }

        toUserModel(rep, user);
        return Response.ok().build();

    }

}
