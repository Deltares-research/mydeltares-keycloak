package nl.deltares.keycloak.storage.rest;

import nl.deltares.keycloak.storage.jpa.Mailing;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AuthenticationManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.MailingAdminResource.*;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getAuthResult;

public class MailingResource {
    private static final String SEARCH_ID_PARAMETER = "id:";
    private final KeycloakSession session;

    private AuthenticationManager.AuthResult authResult;

    @Context
    private HttpHeaders httpHeaders;

    MailingResource(KeycloakSession session) {
        this.session = session;
    }

    public void init() {
        ResteasyProviderFactory.getInstance().injectProperties(this);
        authResult = getAuthResult(session, httpHeaders);
    }

    @Path("/admin")
    public MailingAdminResource admin() {
        MailingAdminResource service = new MailingAdminResource(session);
        ResteasyProviderFactory.getInstance().injectProperties(service);
        service.init();
        return service;

    }
    /**
     * Get representation of the user
     */
    @GET
    @NoCache
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMailing(final @PathParam("id") String id) {
        if (authResult == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
          }

        String realmId = authResult.getSession().getRealm().getId();
        Mailing mailing = getMailingById(session, realmId, id);
        if (mailing == null) {
            throw new NotFoundException("Mailing not found for id " + id);
        }

        return Response.ok(toRepresentation(mailing, null)).build();
    }

    @GET
    @NoCache
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMailings(@QueryParam("search") String search, @QueryParam("name") String name) {

        if (authResult == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String realmId = authResult.getSession().getRealm().getId();
        List<MailingRepresentation> reps = new ArrayList<>();
        if (search != null) {

            if (search.startsWith(SEARCH_ID_PARAMETER)) {
                Mailing mailing = getMailingById(session, realmId, search.substring(SEARCH_ID_PARAMETER.length()).trim());
                if (mailing != null) {
                    reps.add(toRepresentation(mailing , null));
                }
            } else {
                List<Mailing> resultList = searchForMailings(session, realmId, search.trim());

                resultList.forEach(mailing -> reps.add(toRepresentation(mailing, null)));
            }
            return Response.ok(reps).build();
        }

        if (name != null) {
            Mailing mailing = getMailingByName(session, realmId, name);
            if (mailing == null) throw new NotFoundException("Mailing not found for name " + name);
            reps.add(toRepresentation(mailing, null));
        } else {
            List<Mailing> resultList = getMailingsByRealm(session, realmId);
            resultList.forEach(mailing -> reps.add(toRepresentation(mailing, null)));
        }
        return Response.ok(reps).build();
    }


}
