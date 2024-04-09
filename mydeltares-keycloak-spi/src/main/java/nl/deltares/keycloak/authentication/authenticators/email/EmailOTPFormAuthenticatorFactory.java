package nl.deltares.keycloak.authentication.authenticators.email;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

public class EmailOTPFormAuthenticatorFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID =  "auth-email-otp-form";

    @Override
    public Authenticator create(KeycloakSession session) {
        return new EmailOTPFormAuthenticator(session);
    }


    @Override
    public void init(Config.Scope config) {
        // NOOP
    }
    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }
    @Override
    public void close() {
        // NOOP
    }


    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getReferenceCategory() {
        return "otp";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getDisplayType() {
        return "Email OTP Form";
    }

    @Override
    public String getHelpText() {
        return "Validates an Email OTP on a separate OTP form.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return List.of(
                new ProviderConfigProperty(EmailConstants.CODE_LENGTH, "Code length",
                        "The number of digits of the generated code.",
                        ProviderConfigProperty.STRING_TYPE, String.valueOf(EmailConstants.DEFAULT_LENGTH)),
                new ProviderConfigProperty(EmailConstants.CODE_TTL, "Time-to-live",
                        "The time to live in seconds for the code to be valid.", ProviderConfigProperty.STRING_TYPE,
                        String.valueOf(EmailConstants.DEFAULT_TTL)));
    }

}
