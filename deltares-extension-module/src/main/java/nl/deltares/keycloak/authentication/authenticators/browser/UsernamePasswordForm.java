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

import nl.deltares.keycloak.services.messages.Messages;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.FlowStatus;
import org.keycloak.events.Errors;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class UsernamePasswordForm extends org.keycloak.authentication.authenticators.browser.UsernamePasswordForm {


    @Override
    public void testInvalidUser(AuthenticationFlowContext context, UserModel user) {
        super.testInvalidUser(context, user);
        if (user.getEmail().endsWith("@deltares.nl")) {
            context.getEvent().error(Errors.USER_NOT_FOUND);
            Response challengeResponse = challenge(context, Messages.DELTARES_EMAIL);
            context.failureChallenge(AuthenticationFlowError.INVALID_USER, challengeResponse);
        }
    }

    @Override
    public boolean validateUser(AuthenticationFlowContext context, MultivaluedMap<String, String> inputData) {
        if (context.getStatus() == FlowStatus.FAILURE_CHALLENGE) return false;
        return super.validateUser(context, inputData);
    }

    @Override
    public boolean validatePassword(AuthenticationFlowContext context, UserModel user, MultivaluedMap<String, String> inputData) {
        if (context.getStatus() == FlowStatus.FAILURE_CHALLENGE) return false;
        return super.validatePassword(context, user, inputData);
    }
}
