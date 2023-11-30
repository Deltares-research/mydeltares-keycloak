package nl.deltares.keycloak.authentication.forms;

import nl.deltares.keycloak.mocking.MockClientConnection;
import nl.deltares.keycloak.mocking.MockKeycloakSession;
import nl.deltares.keycloak.mocking.MockRealmModel;
import nl.deltares.keycloak.mocking.MockValidationContext;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.Errors;
import org.keycloak.events.EventBuilder;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.DefaultKeycloakContext;
import org.keycloak.services.DefaultKeycloakSessionFactory;
import org.keycloak.services.HttpRequestImpl;
import org.keycloak.services.validation.Validation;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTestCategory")
public class RegistrationEmailTest {

    @Test
    public void testValidateDeltaresMail() throws URISyntaxException {

        RegistrationUserCreation registrationEmail = new RegistrationUserCreation();
        MockValidationContext context = getMockValidationContext();

        HttpRequest request = context.getHttpRequest();
        request.getDecodedFormParameters().add(Validation.FIELD_EMAIL, "test@deltares.nl");
        registrationEmail.validate(context);

        assertFalse(context.isSuccess());
        assertEquals(Errors.INVALID_REGISTRATION, context.getError());

    }

    @Test
    public void testValidateExternalMail() throws URISyntaxException {

        RegistrationUserCreation registrationEmail = new RegistrationUserCreation();
        MockValidationContext context = getMockValidationContext();

        HttpRequest request = context.getHttpRequest();
        request.getDecodedFormParameters().add(Validation.FIELD_EMAIL, "test@test.nl");

        request.getDecodedFormParameters().add(UserModel.USERNAME, "userName");
        request.getDecodedFormParameters().add(UserModel.FIRST_NAME, "firstName");
        request.getDecodedFormParameters().add(UserModel.LAST_NAME, "lastName");

        registrationEmail.validate(context);

        assertTrue(context.isSuccess());
        assertNull(context.getError());

    }

    private MockValidationContext getMockValidationContext() throws URISyntaxException {

        final MockHttpRequest delegate = MockHttpRequest.get("http://localhost:8080/test");
        delegate.addFormHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpRequest request = new HttpRequestImpl(delegate);
        MockValidationContext context = new MockValidationContext();
        context.setRequest(request);

        KeycloakSession session = new MockKeycloakSession(new DefaultKeycloakSessionFactory());
        context.setSession(session);

        final DefaultKeycloakContext sessionContext = (DefaultKeycloakContext) session.getContext();
        MockRealmModel realm = new MockRealmModel();
        sessionContext.setRealm(realm);
        context.setRealm(realm);

        ClientConnection connection = new MockClientConnection();
        EventBuilder eventBuilder = new EventBuilder(realm, session, connection);
        context.setEventBuilder(eventBuilder);

        return context;
    }
}
