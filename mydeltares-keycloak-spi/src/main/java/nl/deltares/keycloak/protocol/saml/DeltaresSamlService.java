package nl.deltares.keycloak.protocol.saml;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.keycloak.dom.saml.v2.protocol.RequestAbstractType;
import org.keycloak.events.EventBuilder;
import org.keycloak.events.EventType;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.protocol.AuthorizationEndpointBase;
import org.keycloak.protocol.saml.SamlProtocol;
import org.keycloak.saml.SAMLRequestParser;
import org.keycloak.saml.processing.core.saml.v2.common.SAMLDocumentHolder;
import org.keycloak.services.ErrorPage;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.util.CacheControlUtil;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.sessions.CommonClientSessionModel;

import java.util.Collections;

/**
 * This class listens on path: http://keycloak:8080/auth/realms/liferay-portal/protocol-saml-deltares/clients/https%3A%2F%2Fgithub.com%2Fenterprises%2Fdeltares
 * Purpose of this class is to add the SAML AuthnRequest ID to the session so it can be returned in the response.
 *
 */
public class DeltaresSamlService extends AuthorizationEndpointBase implements RealmResourceProvider {

    protected static final Logger logger = Logger.getLogger(DeltaresSamlService.class);

    public DeltaresSamlService(KeycloakSession session, EventBuilder event) {
        super(session, event);
    }

    @GET
    @Path("clients/{client}")
    @Produces({"text/html; charset=utf-8"})
    public Response idpInitiatedSSO(@PathParam("client") String clientUrlName, @QueryParam("RelayState") String relayState, @QueryParam("SAMLRequest") String samlRequest) {
        this.event.event(EventType.LOGIN);
        CacheControlUtil.noBackButtonCacheControlHeader(super.session);
        ClientModel client = this.session.clients().searchClientsByAttributes(this.realm, Collections.singletonMap("saml_idp_initiated_sso_url_name", clientUrlName), 0, 1).findFirst().orElse(null);
        if (client == null) {
            this.event.error("client_not_found");
            return ErrorPage.error(this.session, null, Response.Status.BAD_REQUEST, "clientNotFoundMessage");
        } else if (!client.isEnabled()) {
            this.event.error("client_disabled");
            return ErrorPage.error(this.session, null, Response.Status.BAD_REQUEST, "clientDisabledMessage");
        } else if (!this.isClientProtocolCorrect(client)) {
            this.event.error("invalid_client");
            return ErrorPage.error(this.session, null, Response.Status.BAD_REQUEST, "Wrong client protocol.");
        } else {
            this.session.getContext().setClient(client);
            AuthenticationSessionModel authSession = this.getOrCreateLoginSessionForIdpInitiatedSso(client, relayState, samlRequest);
            if (authSession == null) {
                logger.error("SAML assertion consumer url not set up");
                this.event.error("invalid_redirect_uri");
                return ErrorPage.error(this.session, null, Response.Status.BAD_REQUEST, "invalidRedirectUriMessage");
            } else {


                return this.newBrowserAuthentication(authSession);
            }
        }
    }


    private boolean isClientProtocolCorrect(ClientModel clientModel) {
        return "saml".equals(clientModel.getProtocol());
    }

    public AuthenticationSessionModel getOrCreateLoginSessionForIdpInitiatedSso(ClientModel client, String relayState, String samlRequest) {
        String[] bindingProperties = this.getUrlAndBindingForIdpInitiatedSso(client);
        if (bindingProperties == null) {
            return null;
        } else {
            String redirect = bindingProperties[0];
            String bindingType = bindingProperties[1];
            AuthenticationSessionModel authSession = this.createAuthenticationSession(client, null);
            authSession.setProtocol("saml");
            authSession.setAction(CommonClientSessionModel.Action.AUTHENTICATE.name());
            authSession.setClientNote("saml_binding", bindingType);
            authSession.setClientNote("saml_idp_initiated_login", "true");
            authSession.setRedirectUri(redirect);
            if (relayState == null) {
                relayState = client.getAttribute("saml_idp_initiated_sso_relay_state");
            }

            if (relayState != null && !relayState.trim().isEmpty()) {
                authSession.setClientNote("RelayState", relayState);
            }

            if (samlRequest != null) {
                final String samlRequestId = getAuthnRequestID(samlRequest);
                authSession.setClientNote(SamlProtocol.SAML_REQUEST_ID, samlRequestId);
            }

            return authSession;
        }
    }

    private String getAuthnRequestID(String samlRequest) {
        final SAMLDocumentHolder holder = SAMLRequestParser.parseRequestRedirectBinding(samlRequest);
        RequestAbstractType requestAbstractType = (RequestAbstractType) holder.getSamlObject();
        return requestAbstractType.getID();
    }

    private String[] getUrlAndBindingForIdpInitiatedSso(ClientModel client) {
        String postUrl = client.getAttribute("saml_assertion_consumer_url_post");
        String getUrl = client.getAttribute("saml_assertion_consumer_url_redirect");
        if (postUrl != null && !postUrl.trim().isEmpty()) {
            return new String[]{postUrl.trim(), "post"};
        } else if (client.getManagementUrl() != null && !client.getManagementUrl().trim().isEmpty()) {
            return new String[]{client.getManagementUrl().trim(), "post"};
        } else {
            return getUrl != null && !getUrl.trim().isEmpty() ? new String[]{getUrl.trim(), "get"} : null;
        }
    }


    protected Response newBrowserAuthentication(AuthenticationSessionModel authSession) {
        SamlProtocol samlProtocol = (new SamlProtocol()).setEventBuilder(this.event).setHttpHeaders(this.headers).setRealm(this.realm).setSession(this.session).setUriInfo(this.session.getContext().getUri());
        return this.newBrowserAuthentication(authSession, samlProtocol);
    }

    protected Response newBrowserAuthentication(AuthenticationSessionModel authSession, SamlProtocol samlProtocol) {
        return this.handleBrowserAuthenticationRequest(authSession, samlProtocol, false, false);
    }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() {

    }
}
