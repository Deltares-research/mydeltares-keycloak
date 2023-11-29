package nl.deltares.keycloak.authentication.requiredactions;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.common.util.Time;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.Collections;
import java.util.List;

public class TermsAndPrivacy implements RequiredActionProvider, RequiredActionFactory {
    public TermsAndPrivacy() {
    }

    public RequiredActionProvider create(KeycloakSession session) {
        return this;
    }


    public void init(Config.Scope config) {
    }

    public void postInit(KeycloakSessionFactory factory) {
    }

    public String getId() {
        return "terms_and_privacy";
    }

    public void evaluateTriggers(RequiredActionContext context) {
    }

    public void requiredActionChallenge(RequiredActionContext context) {
        Response challenge = context.form().createForm("terms.ftl");
        context.challenge(challenge);
    }

    public void processAction(RequiredActionContext context) {
        MultivaluedMap<String, String> decodedFormParameters = context.getHttpRequest().getDecodedFormParameters();
        if (decodedFormParameters.containsKey("cancel")
                || notAccepted("acceptTerms", decodedFormParameters)
                || notAccepted("acceptPrivacy", decodedFormParameters)) {
            context.getUser().removeAttribute("terms_and_conditions");
            context.failure();
        } else {
            context.getUser().setAttribute("terms_and_conditions", Collections.singletonList(Integer.toString(Time.currentTime())));
            context.success();
        }

    }

    private boolean notAccepted(String acceptKey, MultivaluedMap<String, String> decodedFormParameters) {
        if (!decodedFormParameters.containsKey(acceptKey)) return true;
        List<String> parameters = decodedFormParameters.get(acceptKey);
        if (!parameters.isEmpty()) {
            return !parameters.get(0).equals("on");
        }
        return true;
    }

    public String getDisplayText() {
        return "Deltares Terms and Privacy";
    }

    public void close() {
    }
}

