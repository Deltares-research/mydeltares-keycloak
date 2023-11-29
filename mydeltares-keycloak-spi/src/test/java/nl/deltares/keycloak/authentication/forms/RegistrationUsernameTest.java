package nl.deltares.keycloak.authentication.forms;

import jakarta.ws.rs.core.MultivaluedMap;
import nl.deltares.keycloak.mocking.MockValidationContext;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.events.Details;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.UserModel;
import org.keycloak.services.validation.Validation;

import java.net.URISyntaxException;

import static nl.deltares.keycloak.mocking.TestUtils.getMockValidationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("UnitTestCategory")
public class RegistrationUsernameTest {

    @Test
    public void testValidateNoUsername() throws URISyntaxException {

        RegistrationUsername registrationUsername = new RegistrationUsername();
        MockValidationContext context = getMockValidationContext();

        HttpRequest request = context.getHttpRequest();
        request.getDecodedFormParameters().add(Validation.FIELD_EMAIL, "test@deltares.nl");
        registrationUsername.validate(context);

        assertTrue(context.isSuccess());

        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        assertEquals("test", formData.getFirst(Details.USERNAME));

    }

    @Test
    public void testValidateExistingUsername() throws URISyntaxException {

        RegistrationUsername registrationUsername = new RegistrationUsername();
        MockValidationContext context = getMockValidationContext();
        UserModel test = context.getSession().users().addUser(context.getRealm(), "test");

        HttpRequest request = context.getHttpRequest();
        request.getDecodedFormParameters().add(Validation.FIELD_EMAIL, "test@deltares.nl");
        registrationUsername.validate(context);
        assertTrue(context.isSuccess());
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        assertEquals("test_0", formData.getFirst(Details.USERNAME));

    }

}
