package nl.deltares.keycloak.provider.user;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

/**
 * Event listener listens to Keycloak events. Configure the event listener in the keycloak admin console of the
 * liferay-portal realm under menu: Events -> Configs -> Event Listeners
 */
public class UserEventListenerProvider implements EventListenerProvider {
    private static final Logger logger = Logger.getLogger(UserEventListenerProvider.class);
    private final KeycloakSession session;
    private final RealmProvider model;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public UserEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public void onEvent(Event event) {
        //
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if (event.getOperationType() == OperationType.UPDATE) {
            final UserModel updatedUser = getUpdatedUser(event);
            if (updatedUser == null) return;
            if (updatedUser.isEnabled()) {
                updatedUser.removeAttribute("disabledTime");
            } else {
                removeUserFromGroups(updatedUser);
                updatedUser.setAttribute("disabledTime", Collections.singletonList(dateFormat.format(new Date(System.currentTimeMillis()))));
            }
        }
    }

    private void removeUserFromGroups(UserModel updatedUser) {

        logger.info("Removing user from groups: " + updatedUser.getEmail());
        //Remove user from all groups. This disables any SVN privileges
        final Set<GroupModel> groups = updatedUser.getGroups();
        for (GroupModel group : groups) {
            updatedUser.leaveGroup(group);
        }
    }

    private UserModel getUpdatedUser(AdminEvent event) {

        RealmModel realm = model.getRealm(event.getRealmId());
        String userId;
        String[] split = event.getResourcePath().split("/");
        if (split.length > 1) {
            userId = split[1];
        } else {
            userId = split[0];
        }
        return session.users().getUserById(userId, realm);
    }

    @Override
    public void close() {
//
    }
}
