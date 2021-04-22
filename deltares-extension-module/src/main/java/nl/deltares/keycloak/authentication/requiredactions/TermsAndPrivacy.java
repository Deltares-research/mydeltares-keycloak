package nl.deltares.keycloak.authentication.requiredactions;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.rest.MailingAdminResource;
import nl.deltares.keycloak.storage.rest.UserMailingResource;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.DisplayTypeRequiredActionFactory;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.authentication.requiredactions.ConsoleTermsAndConditions;
import org.keycloak.common.util.Time;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

public class TermsAndPrivacy  implements RequiredActionProvider, RequiredActionFactory, DisplayTypeRequiredActionFactory {
    private static final Logger logger = Logger.getLogger(TermsAndPrivacy.class);


    public static final String PROVIDER_ID = "terms_and_privacy";
    public static final String USER_ATTRIBUTE = "terms_and_privacy";

    public TermsAndPrivacy() {
    }

    public RequiredActionProvider create(KeycloakSession session) {
        return this;
    }

    public RequiredActionProvider createDisplay(KeycloakSession session, String displayType) {
        if (displayType == null) {
            return this;
        } else {
            return !"console".equalsIgnoreCase(displayType) ? null : ConsoleTermsAndConditions.SINGLETON;
        }
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
                || !isAccepted("acceptTerms", decodedFormParameters)
                || !isAccepted("acceptPrivacy", decodedFormParameters)) {
            context.getUser().removeAttribute("terms_and_conditions");
            context.failure();
        } else {
            context.getUser().setAttribute("terms_and_conditions", Arrays.asList(Integer.toString(Time.currentTime())));
            context.success();
            if (isAccepted("acceptSubscriptions", decodedFormParameters)){
                enableSubscriptions(context.getUser(), context.getRealm(), context.getSession());
            }
        }

    }

    private UserMailing getUserMailing(Mailing mailing, UserModel newUser, KeycloakSession session) {

        if (UserMailingResource.getUserMailing(session, mailing.getRealmId(), newUser.getId(), mailing.getId()) != null) {
            logger.warn(String.format("UserMailing already exists! mailing=%s, user email=%s", mailing.getName(), newUser.getEmail()));
            return null;
        }

        UserMailing representation = new UserMailing();
        representation.setId(KeycloakModelUtils.generateId());
        representation.setUserId(newUser.getId());
        representation.setMailingId(mailing.getId());
        representation.setDelivery(Mailing.getPreferredMailingDelivery());
        representation.setRealmId(mailing.getRealmId());
        representation.setLanguage(mailing.getLanguages()[0]);
        return representation;
    }

    private void enableSubscriptions(UserModel newUser, RealmModel realm, KeycloakSession session) {
        List<Mailing> mailingsByRealm = MailingAdminResource.getMailingsByRealm(session, realm.getId());
        mailingsByRealm.forEach(mailing -> {
            UserMailing userMailing = getUserMailing(mailing, newUser, session);
            if (userMailing != null) getEntityManager(session).persist(userMailing);
        });
    }

    private boolean isAccepted(String acceptKey, MultivaluedMap<String, String> decodedFormParameters) {
        if (!decodedFormParameters.containsKey(acceptKey)) return false;
        List<String> parameters = decodedFormParameters.get(acceptKey);
        if (parameters.size() > 0){
            return parameters.get(0).equals("on");
        }
        return false;
    }

    public String getDisplayText() {
        return "Deltares Terms and Privacy";
    }

    public void close() {
    }
}

