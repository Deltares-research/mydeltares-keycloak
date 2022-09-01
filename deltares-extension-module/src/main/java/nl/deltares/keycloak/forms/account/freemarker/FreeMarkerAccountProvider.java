/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.deltares.keycloak.forms.account.freemarker;

import nl.deltares.keycloak.forms.account.freemarker.model.ExtendedUrlBean;
import nl.deltares.keycloak.forms.account.freemarker.model.UserMailingsBean;
import nl.deltares.keycloak.forms.common.model.PasswordPolicyBean;
import nl.deltares.keycloak.storage.rest.MailingRepresentation;
import nl.deltares.keycloak.storage.rest.UserMailingRepresentation;
import org.jboss.logging.Logger;
import org.keycloak.forms.account.AccountPages;
import org.keycloak.forms.account.AccountProvider;
import org.keycloak.forms.account.freemarker.Templates;
import org.keycloak.forms.account.freemarker.model.*;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakUriInfo;
import org.keycloak.models.UserModel;
import org.keycloak.theme.FreeMarkerException;
import org.keycloak.theme.FreeMarkerUtil;
import org.keycloak.theme.Theme;
import org.keycloak.theme.beans.AdvancedMessageFormatterMethod;
import org.keycloak.theme.beans.LocaleBean;
import org.keycloak.utils.MediaType;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class FreeMarkerAccountProvider extends org.keycloak.forms.account.freemarker.FreeMarkerAccountProvider {

    private static final Logger logger = Logger.getLogger(FreeMarkerAccountProvider.class);
    private boolean authorizationSupported;

    public FreeMarkerAccountProvider(KeycloakSession session, FreeMarkerUtil freeMarker) {
        super(session, freeMarker);
    }

    @Override
    public AccountProvider setFeatures(boolean identityProviderEnabled, boolean eventsEnabled, boolean passwordUpdateSupported, boolean authorizationSupported) {
        super.setFeatures(identityProviderEnabled, eventsEnabled, passwordUpdateSupported, authorizationSupported);
        this.authorizationSupported = authorizationSupported;
        return this;
    }

    /**
     * override the createResponse method to replace the UrlBean attribute
     */
    public Response createResponse(AccountPages page) {
        return createResponse(page.name(), Templates.getTemplate(page));
    }


    public Response createResponse(String pageName, String templateName) {
        Map<String, Object> attributes = new HashMap<>();

        if (this.attributes != null) {
            attributes.putAll(this.attributes);
        }

        Theme theme;
        try {
            theme = getTheme();
        } catch (IOException e) {
            logger.error("Failed to create theme", e);
            return Response.serverError().build();
        }

        Locale locale = session.getContext().resolveLocale(user);
        Properties messagesBundle = handleThemeResources(theme, locale, attributes);

        URI baseUri = uriInfo.getBaseUri();
        UriBuilder baseUriBuilder = uriInfo.getBaseUriBuilder();
        if (referrer == null){
            referrer = new String[2];
        }
        for (Map.Entry<String, List<String>> e : ((KeycloakUriInfo) uriInfo).getDelegate().getQueryParameters().entrySet()) {
            Object[] values = e.getValue().toArray();
            baseUriBuilder.queryParam(e.getKey(), values);
            if (e.getKey().equals("referrer") && values.length > 0) referrer[0] = (String) values[0];
            if (e.getKey().equals("referrer_uri") && values.length > 0) referrer[1] = (String) values[0];
        }
        URI baseQueryUri = baseUriBuilder.build();

        if (stateChecker != null) {
            attributes.put("stateChecker", stateChecker);
        }

        handleMessages(locale, messagesBundle, attributes);

        if (Arrays.stream(referrer).allMatch(Objects::nonNull)) {
            attributes.put("referrer", new ReferrerBean(this.referrer));
        }

        if (realm != null) {
            attributes.put("realm", new RealmBean(realm));
        }

        attributes.put("url", new ExtendedUrlBean(realm, theme, baseUri, baseQueryUri, uriInfo.getRequestUri(), stateChecker));
        attributes.put("ppolicy", new PasswordPolicyBean(realm.getPasswordPolicy()));

        if (realm.isInternationalizationEnabled()) {
            UriBuilder b = UriBuilder.fromUri(baseQueryUri).path(uriInfo.getPath());
            attributes.put("locale", new LocaleBean(realm, locale, b, messagesBundle));
        }
        boolean updatePasswordSupportedForUser = checkPasswordUpdateSupported(passwordUpdateSupported, user);
        attributes.put("features", new FeaturesBean(identityProviderEnabled, eventsEnabled, updatePasswordSupportedForUser, authorizationSupported));
        if (user != null) {
            attributes.put("account", new AccountBean(user, profileFormData));
        }

        switch (pageName) {
            case "TOTP":
                attributes.put("totp", new TotpBean(session, realm, user, uriInfo.getRequestUriBuilder()));
                break;
            case "FEDERATED_IDENTITY":
                attributes.put("federatedIdentity", new AccountFederatedIdentityBean(session, realm, user, uriInfo.getBaseUri(), stateChecker));
                break;
            case "LOG":
                attributes.put("log", new LogBean(events));
                break;
            case "SESSIONS":
                attributes.put("sessions", new SessionsBean(realm, sessions));
                break;
            case "APPLICATIONS":
                attributes.put("applications", new ApplicationsBean(session, realm, user));
                attributes.put("advancedMsg", new AdvancedMessageFormatterMethod(locale, messagesBundle));
                break;
            case "PASSWORD":
                attributes.put("password", new PasswordBean(passwordSet));
                break;
            case "RESOURCES":
            case "RESOURCE_DETAIL":
                if (!realm.isUserManagedAccessAllowed()) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
                attributes.put("authorization", new AuthorizationBean(session, user, uriInfo));
                break;
            case "MAILINGS":
                attributes.put("authorization", new AuthorizationBean(session, user, uriInfo));
        }

        return processTemplate(theme, templateName, attributes, locale);
    }

    private boolean checkPasswordUpdateSupported(boolean passwordUpdateSupported, UserModel user) {
        final String lowerEmail = user.getEmail().toLowerCase();
        if (lowerEmail.endsWith("@deltares.nl") || lowerEmail.endsWith("@deltares.org") || lowerEmail.endsWith("@deltares.com")) {
            return false;
        }
        return passwordUpdateSupported;
    }

    protected Response processTemplate(Theme theme, String templateName, Map<String, Object> attributes, Locale locale) {
        try {
            String result = freeMarker.processTemplate(attributes, templateName, theme);
            Response.ResponseBuilder builder = Response.status(status).type(MediaType.TEXT_HTML_UTF_8_TYPE).language(locale).entity(result);
            return builder.build();
        } catch (FreeMarkerException e) {
            logger.error("Failed to process template", e);
            return Response.serverError().build();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public AccountProvider setMailings(List<UserMailingRepresentation> userMailings, List<MailingRepresentation> mailings) {
        if (super.attributes == null) super.attributes = new HashMap<>();
        super.attributes.put("mailings" , new UserMailingsBean(userMailings, mailings));
        return this;
    }


}
