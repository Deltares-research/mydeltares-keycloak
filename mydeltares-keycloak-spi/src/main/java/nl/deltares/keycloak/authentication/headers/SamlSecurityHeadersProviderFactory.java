package nl.deltares.keycloak.authentication.headers;

import org.keycloak.Config;
import org.keycloak.headers.SecurityHeadersProvider;
import org.keycloak.headers.SecurityHeadersProviderFactory;
import org.keycloak.models.KeycloakSession;

public class SamlSecurityHeadersProviderFactory implements SecurityHeadersProviderFactory {

    private String referrerPolicy = null;

    @Override
    public SecurityHeadersProvider create(KeycloakSession keycloakSession) {
        final SamlSecurityHeadersProvider samlSecurityHeadersProvider = new SamlSecurityHeadersProvider(keycloakSession);
        if (referrerPolicy != null) samlSecurityHeadersProvider.setReferrerPolicy(referrerPolicy);
        return samlSecurityHeadersProvider;
    }

    @Override
    public String getId() {
        return "deltares";
    }

    @Override
    public void init(Config.Scope config) {
        referrerPolicy = config.get("referrerPolicy");
    }
}
