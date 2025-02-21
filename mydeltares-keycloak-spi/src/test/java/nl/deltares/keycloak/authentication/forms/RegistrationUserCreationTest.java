package nl.deltares.keycloak.authentication.forms;

import jakarta.ws.rs.core.MultivaluedMap;
import nl.deltares.keycloak.mocking.MockValidationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.events.Details;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.UserModel;

import java.util.Map;


@Tag("UnitTestCategory")
public class RegistrationUserCreationTest {

    @Test
    void extractUserNameWithMinimumLength() {

        RegistrationUserCreation registrationUserCreation = new RegistrationUserCreation();

        ValidationContext context = new MockValidationContext();
        MultivaluedMap<String, String> decodedFormParameters = context.getHttpRequest().getDecodedFormParameters();
        decodedFormParameters.putSingle(Details.EMAIL, "aa@hello.com");
        decodedFormParameters.putSingle(UserModel.FIRST_NAME, "Hello");
        decodedFormParameters.putSingle(UserModel.LAST_NAME, "World");
        registrationUserCreation.validate(context);

        EventBuilder event = context.getEvent();
        Map<String, String> details = event.getEvent().getDetails();
        Assertions.assertEquals("aa@hello.com", details.get(Details.EMAIL));
        Assertions.assertEquals("00aa", details.get(UserModel.USERNAME));
    }
}
