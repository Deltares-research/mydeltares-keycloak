package nl.deltares.keycloak.authentication.forms;

import nl.deltares.keycloak.UnitTestCategory;
import nl.deltares.keycloak.mocking.MockClientConnection;
import nl.deltares.keycloak.mocking.MockKeycloakSession;
import nl.deltares.keycloak.mocking.MockRealmModel;
import nl.deltares.keycloak.mocking.MockValidationContext;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.keycloak.common.ClientConnection;
import org.keycloak.events.Details;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.DefaultKeycloakSessionFactory;
import org.keycloak.services.validation.Validation;

import javax.ws.rs.core.MultivaluedMap;
import java.net.URISyntaxException;

@Category(UnitTestCategory.class)
public class RegistrationUsernameTest {

    @Test
    public void testValidateNoUsername() throws URISyntaxException {

        RegistrationUsername registrationUsername = new RegistrationUsername();
        MockValidationContext context = getMockValidationContext();

        MockHttpRequest request = (MockHttpRequest) context.getHttpRequest();
        request.addFormHeader(Validation.FIELD_EMAIL, "test@deltares.nl");
        registrationUsername.validate(context);

        Assert.assertTrue(context.isSuccess());

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        Assert.assertEquals("test", formData.getFirst(Details.USERNAME));

    }

    @Test
    public void testValidateExistingUsername() throws URISyntaxException {

        RegistrationUsername registrationUsername = new RegistrationUsername();
        MockValidationContext context = getMockValidationContext();
        UserModel test = context.getSession().users().addUser(context.getRealm(), "test");

        MockHttpRequest request = (MockHttpRequest) context.getHttpRequest();
        request.addFormHeader(Validation.FIELD_EMAIL, "test@deltares.nl");
        registrationUsername.validate(context);
        Assert.assertTrue(context.isSuccess());
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        Assert.assertEquals("test_0", formData.getFirst(Details.USERNAME));

    }

    private MockValidationContext getMockValidationContext() throws URISyntaxException {
        MockHttpRequest request = MockHttpRequest.get("http://localhost:8080/test");
        MockValidationContext context = new MockValidationContext();
        context.setRequest(request);
        KeycloakSession session = new MockKeycloakSession(new DefaultKeycloakSessionFactory());
        MockRealmModel realm = new MockRealmModel();
        ClientConnection connection = new MockClientConnection();
        EventBuilder eventBuilder = new EventBuilder(realm, session, connection);
        context.setEventBuilder(eventBuilder);
        context.setSession(session);

        return context;
    }
}
