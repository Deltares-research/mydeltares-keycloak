package nl.deltares.keycloak.authentication.requiredactions;

import nl.deltares.keycloak.UnitTestCategory;
import nl.deltares.keycloak.mocking.MockRealmModel;
import nl.deltares.keycloak.mocking.MockRequiredActionContext;
import nl.deltares.keycloak.mocking.MockUserModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.services.DefaultKeycloakSessionFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Category(UnitTestCategory.class)
public class LoginStatsRecordingRequiredActionProviderTest {

    @Test
    public void testEvaluateTriggers(){

        LoginStatsRecordingRequiredActionProvider provider = new LoginStatsRecordingRequiredActionProvider();
        RequiredActionContext context = getMockingContext("evaluateTriggers");

        //no values set
        provider.evaluateTriggers(context);

        UserModel user = context.getUser();

        long startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String firstLoginDate = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_FIRST_LOGIN_DATE);
        Assert.assertNotNull(firstLoginDate);
        LocalDateTime dateTime = LocalDateTime.parse(firstLoginDate);
        long timeStamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        Assert.assertEquals(startTime, timeStamp, 10);

        String loginCount = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_LOGIN_COUNT);
        Assert.assertNotNull(loginCount);
        Assert.assertTrue(Integer.parseInt(loginCount) == 1);

        String recentLoginDate = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_RECENT_LOGIN_DATE);
        Assert.assertNotNull(recentLoginDate);
        timeStamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        Assert.assertEquals(startTime, timeStamp, 10);
    }

    private RequiredActionContext getMockingContext(String id) {

        KeycloakSession session = new DefaultKeycloakSession(new DefaultKeycloakSessionFactory());
        MockRealmModel realm = new MockRealmModel();
        MockUserModel userModel = new MockUserModel(session, realm, id);
        MockRequiredActionContext context = new MockRequiredActionContext();
        context.setUserModel(userModel);
        return context;
    }


}
