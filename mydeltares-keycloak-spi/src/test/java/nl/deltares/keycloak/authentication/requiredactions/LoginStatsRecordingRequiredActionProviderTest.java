package nl.deltares.keycloak.authentication.requiredactions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.models.UserModel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static nl.deltares.keycloak.mocking.TestUtils.getRequiredActionContext;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTestCategory")
public class LoginStatsRecordingRequiredActionProviderTest {

    @Test
    public void testEvaluateTriggers(){

        LoginStatsRecordingRequiredActionProvider provider = new LoginStatsRecordingRequiredActionProvider();
        RequiredActionContext context = getRequiredActionContext("evaluateTriggers");

        //no values set
        provider.evaluateTriggers(context);

        UserModel user = context.getUser();

        long startTime = LocalDateTime.now(ZoneId.of("GMT")).toEpochSecond(ZoneOffset.UTC);

        final String clientId = context.getAuthenticationSession().getClient().getClientId();
        String firstLoginDate = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_FIRST_LOGIN_DATE + '.' + clientId);
        assertNotNull(firstLoginDate);
        LocalDateTime dateTime = LocalDateTime.parse(firstLoginDate);
        long timeStamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        assertEquals(startTime, timeStamp, 10);

        String loginCount = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_LOGIN_COUNT + '.' + clientId);
        assertNotNull(loginCount);
        assertEquals(1, Integer.parseInt(loginCount));

        String recentLoginDate = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_RECENT_LOGIN_DATE + '.' + clientId);
        assertNotNull(recentLoginDate);
        timeStamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        assertEquals(startTime, timeStamp, 10);
    }


}
