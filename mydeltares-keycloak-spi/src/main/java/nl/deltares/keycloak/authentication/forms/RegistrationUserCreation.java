package nl.deltares.keycloak.authentication.forms;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.apache.commons.text.StringEscapeUtils;
import org.keycloak.Config;
import org.keycloak.authentication.*;
import org.keycloak.authentication.forms.RegistrationPage;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.events.EventType;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.models.utils.FormMessage;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.messages.Messages;
import org.keycloak.services.validation.Validation;
import org.keycloak.userprofile.UserProfile;
import org.keycloak.userprofile.UserProfileContext;
import org.keycloak.userprofile.UserProfileProvider;
import org.keycloak.userprofile.ValidationException;

import java.util.Collections;
import java.util.List;

public class RegistrationUserCreation implements FormAction, FormActionFactory {

    public static final String PROVIDER_ID = "registration-deltares-user-creation";

    @Override
    public String getHelpText() {
        return "This action must always be first! Validates the username and user profile of the user in validation phase.  In success phase, this will create the user in the database including his user profile. When the username is not provided it will automatically be created from the email address.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return null;
    }

    @Override
    public void validate(ValidationContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        context.getEvent().detail(Details.REGISTER_METHOD, "form");

        final String providedEmail = formData.getFirst(Details.EMAIL);

        if (isDeltaresEmail(providedEmail)){
            context.error(Errors.INVALID_REGISTRATION);
            final List<FormMessage> errors = Collections.singletonList(new FormMessage(RegistrationPage.FIELD_EMAIL, nl.deltares.keycloak.services.messages.Messages.DELTARES_EMAIL));
            context.validationError(formData, errors);
            return;
        }

        final String providedUsername = formData.getFirst(Details.USERNAME);
        final String username = createUserNameIfNotProvided(context, providedUsername, providedEmail);
        if (providedUsername == null){
            formData.add(Details.USERNAME, username);
        }
        UserProfile profile = getOrCreateUserProfile(context, formData);
        context.getEvent().detail(Details.EMAIL, providedEmail);
        context.getEvent().detail(Details.USERNAME, username);

        String firstName = profile.getAttributes().getFirstValue(UserModel.FIRST_NAME);
        String lastName = profile.getAttributes().getFirstValue(UserModel.LAST_NAME);

        // check to make sure no XSS or template injection occurs
        context.getEvent().detail(Details.FIRST_NAME, StringEscapeUtils.escapeHtml4(firstName));
        context.getEvent().detail(Details.LAST_NAME, StringEscapeUtils.escapeHtml4(lastName));

        if (context.getRealm().isRegistrationEmailAsUsername()) {
            context.getEvent().detail(Details.USERNAME, providedEmail);
        }

        try {
            profile.validate();
        } catch (ValidationException pve) {
            List<FormMessage> errors = Validation.getFormErrorsFromValidation(pve.getErrors());

            if (pve.hasError(Messages.EMAIL_EXISTS, Messages.INVALID_EMAIL)) {
                context.getEvent().detail(Details.EMAIL, profile.getAttributes().getFirstValue(UserModel.EMAIL));
            }

            if (pve.hasError(Messages.EMAIL_EXISTS)) {
                context.error(Errors.EMAIL_IN_USE);
            } else if (pve.hasError(Messages.USERNAME_EXISTS)) {
                context.error(Errors.USERNAME_IN_USE);
            } else {
                context.error(Errors.INVALID_REGISTRATION);
            }

            context.validationError(formData, errors);
            return;
        }
        context.success();
    }

    private boolean isDeltaresEmail(String email) {
        String lowerEmail = email.toLowerCase();
        return lowerEmail.endsWith("@deltares.nl") || lowerEmail.endsWith("@deltares.org") || lowerEmail.endsWith("@deltares.com");
    }

    private String createUserNameIfNotProvided(ValidationContext context, String username, String email) {

        if (username == null || username.trim().isEmpty()) {
            //create username from email
            username = createUserNameFromEmail(email, context);
        } else {
            int i = username.indexOf('\\');
            if (i > 0){
                //remove directory\ string
                username = username.substring(i+1);
            }
        }
        return username;
    }

    @Override
    public void buildPage(FormContext context, LoginFormsProvider form) {
        checkNotOtherUserAuthenticating(context);
    }

    @Override
    public void success(FormContext context) {
        checkNotOtherUserAuthenticating(context);

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();

        String email = formData.getFirst(UserModel.EMAIL);
        String username = formData.getFirst(UserModel.USERNAME);

        if (context.getRealm().isRegistrationEmailAsUsername()) {
            username = email;
        }

        context.getEvent().detail(Details.USERNAME, username)
                .detail(Details.REGISTER_METHOD, "form")
                .detail(Details.EMAIL, email);

        UserProfile profile = getOrCreateUserProfile(context, formData);
        UserModel user = profile.create();

        user.setEnabled(true);

        context.setUser(user);

        context.getAuthenticationSession().setClientNote(OIDCLoginProtocol.LOGIN_HINT_PARAM, username);

        context.getEvent().user(user);
        context.getEvent().success();
        context.newEvent().event(EventType.LOGIN);
        context.getEvent().client(context.getAuthenticationSession().getClient().getClientId())
                .detail(Details.REDIRECT_URI, context.getAuthenticationSession().getRedirectUri())
                .detail(Details.AUTH_METHOD, context.getAuthenticationSession().getProtocol());
        String authType = context.getAuthenticationSession().getAuthNote(Details.AUTH_TYPE);
        if (authType != null) {
            context.getEvent().detail(Details.AUTH_TYPE, authType);
        }
    }

    private void checkNotOtherUserAuthenticating(FormContext context) {
        if (context.getUser() != null) {
            // the user probably did some back navigation in the browser, hitting this page in a strange state
            context.getEvent().detail(Details.EXISTING_USER, context.getUser().getUsername());
            throw new AuthenticationFlowException(AuthenticationFlowError.GENERIC_AUTHENTICATION_ERROR, Errors.DIFFERENT_USER_AUTHENTICATING, Messages.EXPIRED_ACTION);
        }
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }


    @Override
    public void close() {

    }

    @Override
    public String getDisplayType() {
        return "Deltares Registration User Profile Creation";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.DISABLED
    };
    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }
    @Override
    public FormAction create(KeycloakSession session) {
        return this;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    private MultivaluedMap<String, String> normalizeFormParameters(MultivaluedMap<String, String> formParams) {
        MultivaluedHashMap<String, String> copy = new MultivaluedHashMap<>(formParams);

        // Remove "password" and "password-confirm" to avoid leaking them in the user-profile data
        copy.remove(RegistrationPage.FIELD_PASSWORD);
        copy.remove(RegistrationPage.FIELD_PASSWORD_CONFIRM);

        return copy;
    }

    /**
     * Get user profile instance for current HTTP request (KeycloakSession) and for given context. This assumes that there is
     * single user registered within HTTP request, which is always the case in Keycloak
     */
    public UserProfile getOrCreateUserProfile(FormContext formContext, MultivaluedMap<String, String> formData) {
        KeycloakSession session = formContext.getSession();
        UserProfile profile = (UserProfile) session.getAttribute("UP_REGISTER");
        if (profile == null) {
            formData = normalizeFormParameters(formData);
            UserProfileProvider profileProvider = session.getProvider(UserProfileProvider.class);
            profile = profileProvider.create(UserProfileContext.REGISTRATION, formData);
            session.setAttribute("UP_REGISTER", profile);
        }
        return profile;
    }

    private String createUserNameFromEmail(String email, ValidationContext context) {

        if (email == null) return null;
        int i = email.indexOf('@');
        String userName;
        if (i > 0){
            //remove domain
            userName =  email.substring(0, i);
        } else {
            userName = email;
        }

        UserProvider users = context.getSession().users();
        i = 0;
        String validUserName = userName;
        while (users.getUserByUsername(context.getRealm(), validUserName) != null){
            validUserName = userName + '_' + i++;
        }
        return validUserName;
    }
}

