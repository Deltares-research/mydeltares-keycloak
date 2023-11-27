package nl.deltares.keycloak.authentication.headers;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedMap;
import org.keycloak.headers.DefaultSecurityHeadersProvider;
import org.keycloak.models.BrowserSecurityHeaders;
import org.keycloak.models.KeycloakSession;


import java.util.Collections;

public class SamlSecurityHeadersProvider extends DefaultSecurityHeadersProvider {

    private String referrerPolicy = "strict-origin-when-cross-origin";

    public SamlSecurityHeadersProvider(KeycloakSession session) {
        super(session);
    }

    @Override
    public void addHeaders(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        super.addHeaders(requestContext, responseContext);

        if (isSamlLogin(requestContext)) {
            replaceReferrerHeader(responseContext);
        }
    }

    public void setReferrerPolicy(String configuredValue){
        this.referrerPolicy = configuredValue;
    }

    private boolean isSamlLogin(ContainerRequestContext requestContext) {
        return requestContext.getMethod().equals("POST");
    }

    /**
     * Replace the default Referrer Policy from 'no-referres' to 'strict-origin-when-cross-origin'.
     * <p>
     * Do this only for the SAML logins.
     *
     * @param responseContext Response context containing headers
     */
    private void replaceReferrerHeader(ContainerResponseContext responseContext) {
        final MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.replace(BrowserSecurityHeaders.REFERRER_POLICY.getHeaderName(), Collections.singletonList(referrerPolicy));
    }


}
