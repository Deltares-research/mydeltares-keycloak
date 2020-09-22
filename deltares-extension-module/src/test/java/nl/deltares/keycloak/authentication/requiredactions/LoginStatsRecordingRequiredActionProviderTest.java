package nl.deltares.keycloak.authentication.requiredactions;

import nl.deltares.keycloak.UnitTestCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.models.UserModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static nl.deltares.keycloak.mocking.TestUtils.getRequiredActionContext;

@Category(UnitTestCategory.class)
public class LoginStatsRecordingRequiredActionProviderTest {

    @Test
    public void testEvaluateTriggers(){

        LoginStatsRecordingRequiredActionProvider provider = new LoginStatsRecordingRequiredActionProvider();
        RequiredActionContext context = getRequiredActionContext("evaluateTriggers");

        //no values set
        provider.evaluateTriggers(context);

        UserModel user = context.getUser();

        long startTime = LocalDateTime.now(ZoneId.of("GMT")).toEpochSecond(ZoneOffset.UTC);
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


}
