package org.keycloak.authentication.requiredactions;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserModel;

import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;

public class LoginStatsRecordingRequiredActionProvider  implements RequiredActionProvider, RequiredActionFactory {

    private static final Logger LOG = Logger.getLogger(LoginStatsRecordingRequiredActionProvider.class);

    private static final String PROVIDER_ID = "login_stats_action";
    private static final String RECORD_LOGIN_STATISTICS_ACTION = "Record Login Statistics Action";

    private static final String LOGIN_LOGIN_COUNT = "login.login-count";
    private static final String LOGIN_FIRST_LOGIN_DATE = "login.first-login-date";
    private static final String LOGIN_RECENT_LOGIN_DATE = "login.recent-login-date";

    private static final String ONE = "1";

    private static final LoginStatsRecordingRequiredActionProvider INSTANCE = new LoginStatsRecordingRequiredActionProvider();

    @Override
    public void evaluateTriggers(RequiredActionContext context) {
        UserModel user = context.getUser();

        try {
            recordFirstLogin(user);
        } catch (Exception ex) {
            LOG.warnv(ex,"Couldn't record first login <{0}>", this);
        }

        try {
            recordRecentLogin(user);
        } catch (Exception ex) {
            LOG.warnv(ex, "Couldn't record recent login <{0}>", this);
        }

        try {
            recordLoginCount(user);
        } catch (Exception ex) {
            LOG.warnv(ex, "Couldn't record login count <{0}>", this);
        }
    }

    @Override
    public void requiredActionChallenge(RequiredActionContext requiredActionContext) {

    }

    @Override
    public void processAction(RequiredActionContext requiredActionContext) {

    }

    private void recordLoginCount(UserModel user) {

        List<String> list = user.getAttribute(LOGIN_LOGIN_COUNT);

        if (list == null || list.isEmpty()) {
            list = Collections.singletonList(ONE);
        } else {
            list = Collections.singletonList(String.valueOf(Long.parseLong(list.get(0)) + 1));
        }

        user.setAttribute(LOGIN_LOGIN_COUNT, list);
    }

    private void recordRecentLogin(UserModel user) {
        user.setAttribute(LOGIN_RECENT_LOGIN_DATE, Collections.singletonList(now().toString()));
    }

    private void recordFirstLogin(UserModel user) {

        List<String> list = user.getAttribute(LOGIN_FIRST_LOGIN_DATE);

        if (list == null || list.isEmpty()) {
            user.setAttribute(LOGIN_FIRST_LOGIN_DATE, Collections.singletonList(now().toString()));
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
