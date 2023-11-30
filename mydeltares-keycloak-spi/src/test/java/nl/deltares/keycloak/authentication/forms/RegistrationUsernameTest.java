package nl.deltares.keycloak.authentication.forms;

import nl.deltares.keycloak.mocking.MockValidationContext;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.UserModel;
import org.keycloak.services.validation.Validation;
import org.keycloak.userprofile.UserProfile;

import java.net.URISyntaxException;

import static nl.deltares.keycloak.mocking.TestUtils.getMockValidationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("UnitTestCategory")
public class RegistrationUsernameTest {

    @Test
    public void testValidateNoUsername() throws URISyntaxException {

        RegistrationUserCreation registrationUsername = new RegistrationUserCreation();
        MockValidationContext context = getMockValidationContext();

        HttpRequest request = context.getHttpRequest();
        request.getDecodedFormParameters().add(Validation.FIELD_EMAIL, "test@domain.nl");
//        request.getDecodedFormParameters().add(UserModel.USERNAME, "userName");
        request.getDecodedFormParameters().add(UserModel.FIRST_NAME, "firstName");
        request.getDecodedFormParameters().add(UserModel.LAST_NAME, "lastName");

        registrationUsername.validate(context);

        assertTrue(context.isSuccess());
        final UserProfile profile = (UserProfile) context.getSession().getAttribute("UP_REGISTER");
        assertEquals("test", profile.getAttributes().getFirstValue(UserModel.USERNAME));

    }

    @Test
    public void testValidateExistingUsername() throws URISyntaxException {

        RegistrationUserCreation registrationUsername = new RegistrationUserCreation();
        MockValidationContext context = getMockValidationContext();
        context.getSession().users().addUser(context.getRealm(), "test");

        HttpRequest request = context.getHttpRequest();
        request.getDecodedFormParameters().add(Validation.FIELD_EMAIL, "test@domain.nl");
//        request.getDecodedFormParameters().add(UserModel.USERNAME, "userName");
        request.getDecodedFormParameters().add(UserModel.FIRST_NAME, "firstName");
        request.getDecodedFormParameters().add(UserModel.LAST_NAME, "lastName");
        registrationUsername.validate(context);
        assertTrue(context.isSuccess());
        final UserProfile profile = (UserProfile) context.getSession().getAttribute("UP_REGISTER");
        assertEquals("test_0", profile.getAttributes().getFirstValue(UserModel.USERNAME));

    }

}
