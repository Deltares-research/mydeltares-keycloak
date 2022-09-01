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
package nl.deltares.keycloak.forms.login.freemarker;

import nl.deltares.keycloak.forms.common.model.PasswordPolicyBean;
import org.jboss.logging.Logger;
import org.keycloak.forms.login.LoginFormsPages;
import org.keycloak.models.KeycloakSession;
import org.keycloak.theme.FreeMarkerException;
import org.keycloak.theme.FreeMarkerUtil;
import org.keycloak.theme.Theme;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class FreeMarkerLoginFormsProvider extends org.keycloak.forms.login.freemarker.FreeMarkerLoginFormsProvider {


    private static final Logger logger = Logger.getLogger(FreeMarkerLoginFormsProvider.class);

    public FreeMarkerLoginFormsProvider(KeycloakSession session, FreeMarkerUtil freeMarker) {
        super(session, freeMarker);
    }

    @Override
    protected void createCommonAttributes(Theme theme, Locale locale, Properties messagesBundle, UriBuilder baseUriBuilder, LoginFormsPages page) {

        if (realm != null) {

            this.attributes.put("ppolicy", new PasswordPolicyBean(realm.getPasswordPolicy()));
        }
        super.createCommonAttributes(theme, locale, messagesBundle, baseUriBuilder, page);
    }

    @Override
    protected Response processTemplate(Theme theme, String templateName, Locale locale) {
        try {
            String result = this.freeMarker.processTemplate(this.attributes, templateName, theme);
            MediaType mediaType = this.contentType == null ? org.keycloak.utils.MediaType.TEXT_HTML_UTF_8_TYPE : this.contentType;
            Response.ResponseBuilder builder = Response.status(this.status == null ? Response.Status.OK : this.status).type(mediaType).language(locale).entity(result);
            Iterator var7 = this.httpResponseHeaders.entrySet().iterator();

            while(var7.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var7.next();
                builder.header((String)entry.getKey(), entry.getValue());
            }

            return builder.build();
        } catch (FreeMarkerException var9) {
            logger.error("Failed to process template", var9);
            return Response.serverError().build();
        }
    }
}
