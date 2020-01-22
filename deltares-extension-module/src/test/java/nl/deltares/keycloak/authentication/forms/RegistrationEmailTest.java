package nl.deltares.keycloak.authentication.forms;

import nl.deltares.keycloak.mocking.MockClientConnection;
import nl.deltares.keycloak.mocking.MockRealmModel;
import nl.deltares.keycloak.mocking.MockValidationContext;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.junit.Assert;
import org.junit.Test;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.Errors;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;
import org.keycloak.services.validation.Validation;

import java.net.URISyntaxException;

public class RegistrationEmailTest {

    @Test
    public void testValidateDeltaresMail() throws URISyntaxException {

        RegistrationEmail registrationEmail = new RegistrationEmail();
        MockValidationContext context = getMockValidationContext();

        MockHttpRequest request = (MockHttpRequest) context.getHttpRequest();
        request.addFormHeader(Validation.FIELD_EMAIL, "test@deltares.nl");
        registrationEmail.validate(context);

        Assert.assertFalse(context.isSuccess());
        Assert.assertEquals(Errors.INVALID_REGISTRATION, context.getError());

    }

    @Test
    public void testValidateExternalMail() throws URISyntaxException {

        RegistrationEmail registrationEmail = new RegistrationEmail();
        MockValidationContext context = getMockValidationContext();

        MockHttpRequest request = (MockHttpRequest) context.getHttpRequest();
        request.addFormHeader(Validation.FIELD_EMAIL, "test@test.nl");
        registrationEmail.validate(context);

        Assert.assertTrue(context.isSuccess());
        Assert.assertNull(context.getError());

    }

    private MockValidationContext getMockValidationContext() throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.get("http://localhost:8080/test");
        MockValidationContext context = new MockValidationContext();
        context.setRequest(request);
        KeycloakSession session = new DefaultKeycloakSession(new DefaultKeycloakSessionFactory());
        MockRealmModel realm = new MockRealmModel();
        ClientConnection connection = new MockClientConnection();
        EventBuilder eventBuilder = new EventBuilder(realm, session, connection);
        context.setEventBuilder(eventBuilder);

        return context;
    }
}
