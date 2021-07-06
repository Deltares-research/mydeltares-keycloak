package nl.deltares.keycloak.authentication.requiredactions;

import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.HttpRequest;
import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserModel;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

public class LoginStatsRecordingRequiredActionProvider  implements RequiredActionProvider, RequiredActionFactory {

    private static final Logger LOG = Logger.getLogger(LoginStatsRecordingRequiredActionProvider.class);

    static final String PROVIDER_ID = "login_stats_action";
    static final String RECORD_LOGIN_STATISTICS_ACTION = "Record Login Statistics Action";

    static final String LOGIN_LOGIN_COUNT = "login.login-count";
    static final String LOGIN_FIRST_LOGIN_DATE = "login.first-login-date";
    static final String LOGIN_RECENT_LOGIN_DATE = "login.recent-login-date";

    private static final String ONE = "1";

    private static final LoginStatsRecordingRequiredActionProvider INSTANCE = new LoginStatsRecordingRequiredActionProvider();

    @Override
    public void evaluateTriggers(RequiredActionContext context) {
        UserModel user = context.getUser();
        String referrer = getReferrer(context);
        try {
            recordFirstLogin(referrer, user);
        } catch (Exception ex) {
            LOG.warnv(ex,"Couldn't record first login <{0}>", this);
        }

        try {
            recordRecentLogin(referrer, user);
        } catch (Exception ex) {
            LOG.warnv(ex, "Couldn't record recent login <{0}>", this);
        }

        try {
            recordLoginCount(referrer, user);
        } catch (Exception ex) {
            LOG.warnv(ex, "Couldn't record login count <{0}>", this);
        }
    }

    private String getReferrer(RequiredActionContext context) {
        HttpRequest httpRequest = context.getHttpRequest();
        if (httpRequest == null) return null;
        String headerString = httpRequest.getHttpHeaders().getHeaderString("Referer");
        if (headerString == null) return null;
        int startIndex = headerString.indexOf("client_id");
        if (startIndex < 0) return null;
        String substring = headerString.substring(startIndex);
        String[] params = substring.split("&");
        for (String param : params) {
            if (param.startsWith("client_id")) {
                String[] keyValue = param.split("=");
                if (keyValue.length > 1)
                    return keyValue[1];
            }
        }
        return null;
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext requiredActionContext) {

    }

    @Override
    public void processAction(RequiredActionContext requiredActionContext) {

    }

    private void recordLoginCount(String referrer, UserModel user) {
        String key = referrer == null ? LOGIN_LOGIN_COUNT : LOGIN_LOGIN_COUNT + '.' + referrer;
        Stream<String> stream = user.getAttributeStream(key);
        final Optional<String> first = stream.findFirst();
        if (first.isEmpty()) {
            user.setAttribute(key, Collections.singletonList(ONE));
        } else {
            user.setAttribute(key, Collections.singletonList(String.valueOf(Long.parseLong(first.get()) + 1)));
        }

    }

    private void recordRecentLogin(String referrer, UserModel user) {
        String key = referrer == null ? LOGIN_RECENT_LOGIN_DATE : LOGIN_RECENT_LOGIN_DATE + '.' + referrer;
        user.setAttribute(key, Collections.singletonList(now(ZoneId.of("GMT")).toString()));
    }

    private void recordFirstLogin(String referrer, UserModel user) {
        String key = referrer == null ? LOGIN_FIRST_LOGIN_DATE : LOGIN_FIRST_LOGIN_DATE + '.' + referrer;
        Stream<String> stream = user.getAttributeStream(key);
        final Optional<String> first = stream.findFirst();
        if (first.isEmpty()) {
            user.setAttribute(key, Collections.singletonList(now(ZoneId.of("GMT")).toString()));
        }
    }

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return INSTANCE;
    }

    @Override
    public void init(Config.Scope scope) {
        LOG.infov("Creating IdM Keycloak extension <{0}>", this);
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        return RECORD_LOGIN_STATISTICS_ACTION;
    }

    @Override
    public void close() {

    }
}
