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

import nl.deltares.keycloak.forms.account.freemarker.model.UserMailingsBean;
import nl.deltares.keycloak.storage.rest.MailingRepresentation;
import nl.deltares.keycloak.storage.rest.UserMailingRepresentation;
import org.jboss.logging.Logger;
import org.keycloak.forms.account.AccountProvider;
import org.keycloak.forms.account.freemarker.model.*;
import org.keycloak.models.KeycloakSession;
import org.keycloak.theme.BrowserSecurityHeaderSetup;
import org.keycloak.theme.FreeMarkerException;
import org.keycloak.theme.FreeMarkerUtil;
import org.keycloak.theme.Theme;
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

    public Response createCustomResponse(String templateName) {
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
        for (Map.Entry<String, List<String>> e : uriInfo.getQueryParameters().entrySet()) {
            baseUriBuilder.queryParam(e.getKey(), e.getValue().toArray());
        }
        URI baseQueryUri = baseUriBuilder.build();

        if (stateChecker != null) {
            attributes.put("stateChecker", stateChecker);
        }

        handleMessages(locale, messagesBundle, attributes);

        if (referrer != null) {
            attributes.put("referrer", new ReferrerBean(referrer));
        }

        if (realm != null) {
            attributes.put("realm", new RealmBean(realm));
        }

        attributes.put("url", new UrlBean(realm, theme, baseUri, baseQueryUri, uriInfo.getRequestUri(), stateChecker));

        if (realm.isInternationalizationEnabled()) {
            UriBuilder b = UriBuilder.fromUri(baseQueryUri).path(uriInfo.getPath());
            attributes.put("locale", new LocaleBean(realm, locale, b, messagesBundle));
        }

        attributes.put("features", new FeaturesBean(identityProviderEnabled, eventsEnabled, passwordUpdateSupported, authorizationSupported));
        attributes.put("account", new AccountBean(user, profileFormData));

        attributes.put("authorization", new AuthorizationBean(session, user, uriInfo));


        return processTemplate(theme, attributes, locale, templateName);
    }

    protected Response processTemplate(Theme theme, Map<String, Object> attributes, Locale locale, String templateName) {
        try {
            String result = freeMarker.processTemplate(attributes, templateName, theme);
            Response.ResponseBuilder builder = Response.status(status).type(MediaType.TEXT_HTML_UTF_8_TYPE).language(locale).entity(result);
            BrowserSecurityHeaderSetup.headers(builder, realm);
            return builder.build();
        } catch (FreeMarkerException e) {
            logger.error("Failed to process template", e);
            return Response.serverError().build();
        }
    }

    public AccountProvider setMailings(List<UserMailingRepresentation> userMailings, List<MailingRepresentation> mailings) {
        if (super.attributes == null) super.attributes = new HashMap<>();
        super.attributes.put("mailings" , new UserMailingsBean(userMailings, mailings));
        return this;
    }
}
