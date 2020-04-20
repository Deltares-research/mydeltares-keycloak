package nl.deltares.keycloak.provider.user;

import nl.deltares.keycloak.IntegrationTestCategory;
import nl.deltares.keycloak.KeycloakTestServer;
import nl.deltares.keycloak.UnitTestSuite;
import nl.deltares.keycloak.mocking.MockValidationContext;
import nl.deltares.keycloak.storage.rest.*;
import nl.deltares.keycloak.utils.KeycloakUtilsImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.resource.RealmResourceProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static nl.deltares.keycloak.mocking.TestUtils.getMockValidationContext;

@Category(UnitTestSuite.class)
public class UserEventListenerProviderTest {

    @Test
    /*
     * Test that when users are created they are automatically assigned mailings.
     */
    public void testHandleEvent() throws URISyntaxException {


        MockValidationContext context = getMockValidationContext();

        MailingResourceProviderFactory mailingFactory = new MailingResourceProviderFactory();
        MailingResourceProvider mailingProvider = (MailingResourceProvider) mailingFactory.create(context.getSession());
        MailingResource mailingResource = (MailingResource) mailingProvider.getResource();
        MailingAdminResource adminMailingResource = mailingResource.admin();
        MailingRepresentation mailing = new MailingRepresentation();
        mailing.setDelivery(0);
        mailing.setLanguages(new String[]{"en"});
        mailing.setFrequency(0);
        mailing.setName("testHandleEvent");
        adminMailingResource.createMailing(mailing);




    }

}
