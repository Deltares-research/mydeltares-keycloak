package nl.deltares.keycloak.authentication.forms;

import nl.deltares.keycloak.UnitTestCategory;
import nl.deltares.keycloak.mocking.MockValidationContext;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.keycloak.events.Details;
import org.keycloak.models.UserModel;
import org.keycloak.services.validation.Validation;

import javax.ws.rs.core.MultivaluedMap;
import java.net.URISyntaxException;

import static nl.deltares.keycloak.mocking.TestUtils.getMockValidationContext;

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

}
