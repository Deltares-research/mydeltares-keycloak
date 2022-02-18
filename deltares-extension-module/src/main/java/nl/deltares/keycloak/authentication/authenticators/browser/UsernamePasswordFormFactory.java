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

package nl.deltares.keycloak.authentication.authenticators.browser;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.console.ConsoleUsernamePasswordAuthenticator;
import org.keycloak.models.KeycloakSession;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class UsernamePasswordFormFactory extends org.keycloak.authentication.authenticators.browser.UsernamePasswordFormFactory {

    public static final String PROVIDER_ID = "deltares-auth-username-password-form";
    public static final UsernamePasswordForm SINGLETON = new UsernamePasswordForm();

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    public Authenticator createDisplay(KeycloakSession session, String displayType) {
        if (displayType == null) {
            return SINGLETON;
        } else {
            return !"console".equalsIgnoreCase(displayType) ? null : ConsoleUsernamePasswordAuthenticator.SINGLETON;
        }
    }

    @Override
    public String getDisplayType() {
        return "Deltares Username Password Form";
    }

    @Override
    public String getHelpText() {
        return "Validates a username and password from login form. Also checks for Deltares emails.";
    }

}
