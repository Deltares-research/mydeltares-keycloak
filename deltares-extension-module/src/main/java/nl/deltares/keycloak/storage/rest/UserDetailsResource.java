package nl.deltares.keycloak.storage.rest;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.ClientConnection;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.ErrorResponse;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuthResult;

public class UserDetailsResource {

    private final KeycloakSession session;

    private AuthenticationManager.AuthResult authResult;

    @Context
    private HttpHeaders httpHeaders;

    @Context
    private ClientConnection connection;

    @Context
    protected HttpRequest request;

    UserDetailsResource(KeycloakSession session) {
        this.session = session;
        ResteasyProviderFactory.getInstance().injectProperties(this);
    }

    void init(){

        ResteasyProviderFactory.getInstance().injectProperties(this);
        authResult = getAuthResult(session, httpHeaders, connection);
    }

    @Path("/admin")
    public UserDetailsAdminResource admin() {
        UserDetailsAdminResource service = new UserDetailsAdminResource(session);
        ResteasyProviderFactory.getInstance().injectProperties(service);
        service.init();
        return service;
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails() {

        if (authResult == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserModel user = authResult.getUser();
        return Response.ok(toRepresentation(user), MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserDetails(final UserRepresentation rep) {

        if (authResult == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UserModel userModel = authResult.getUser();
        try {
            validateUser(rep, userModel, session);
        } catch (IOException e) {
            return ErrorResponse.error(e.getMessage(), Response.Status.BAD_REQUEST);
        }

        toUserModel(rep, userModel);
        return Response.ok().build();

    }

    static void validateUser(UserRepresentation rep, UserModel userModel, KeycloakSession session) throws IOException {

        String id = rep.getId();
        if (id != null && !userModel.getId().equals(id)) {
            throw new IOException(String.format("Not the same user! Found id %s, expected %s", id, userModel.getId()));
        }
        String username = rep.getUsername();
        if (username != null && !userModel.getUsername().equals(username)){
            throw new IOException(String.format("Not allowed to update username! Found username %s, expected %s", username, userModel.getUsername()));
        }

        String email = rep.getEmail();
        if (email != null && !userModel.getEmail().equals(email)){
            UserModel userByEmail = session.users().getUserByEmail(email, session.getContext().getRealm());
            if (userByEmail != null){
                throw new IOException(String.format("Cannot change email to %s for user %s! This email address already exists!", email, userModel.getUsername()));
            }
        }

    }

    static UserModel toUserModel(UserRepresentation userRepresentation, UserModel userModel){

        String email = userRepresentation.getEmail();
        if (email != null && !userModel.getEmail().equals(email)) userModel.setEmail(email);
        String firstName = userRepresentation.getFirstName();
        if (firstName != null) userModel.setFirstName(firstName);
        String lastName = userRepresentation.getLastName();
        if (lastName != null) userModel.setLastName(lastName);

        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes == null) return userModel;
        for (Map.Entry<String, List<String>> attributeEntry : attributes.entrySet()) {
            userModel.setAttribute(attributeEntry.getKey(), attributeEntry.getValue());
        }
        return userModel;
    }

    static UserRepresentation toRepresentation(UserModel user) {

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(user.getId());
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setAttributes(user.getAttributes());
        return userRepresentation;
    }

}
