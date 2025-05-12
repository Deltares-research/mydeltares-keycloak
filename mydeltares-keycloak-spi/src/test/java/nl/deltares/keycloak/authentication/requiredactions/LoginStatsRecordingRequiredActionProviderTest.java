package nl.deltares.keycloak.authentication.requiredactions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.models.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static nl.deltares.keycloak.mocking.TestUtils.getRequiredActionContext;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTestCategory")
public class LoginStatsRecordingRequiredActionProviderTest {

    @Test
    public void testEvaluateTriggers() throws ParseException {

        LoginStatsRecordingRequiredActionProvider provider = new LoginStatsRecordingRequiredActionProvider();
        RequiredActionContext context = getRequiredActionContext("evaluateTriggers");

        //no values set
        provider.evaluateTriggers(context);

        UserModel user = context.getUser();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        long startTime = System.currentTimeMillis();

        final String clientId = context.getAuthenticationSession().getClient().getClientId();
        String firstLoginDate = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_FIRST_LOGIN_DATE + '.' + clientId);
        assertNotNull(firstLoginDate);
        Date dateTime = dateFormat.parse(firstLoginDate);
        long timeStamp = dateTime.getTime();
        assertEquals(startTime, timeStamp, 60000);

        String loginCount = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_LOGIN_COUNT + '.' + clientId);
        assertNotNull(loginCount);
        assertEquals(1, Integer.parseInt(loginCount));

        String recentLoginDate = user.getFirstAttribute(LoginStatsRecordingRequiredActionProvider.LOGIN_RECENT_LOGIN_DATE + '.' + clientId);
        assertNotNull(recentLoginDate);
        dateTime = dateFormat.parse(recentLoginDate);
        timeStamp = dateTime.getTime();
        assertEquals(startTime, timeStamp, 60000);
    }


}
