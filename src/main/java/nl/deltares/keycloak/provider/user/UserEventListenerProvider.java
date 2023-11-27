package nl.deltares.keycloak.provider.user;

import org.apache.commons.text.StringEscapeUtils;
import org.keycloak.events.EventType;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

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
        if (event.getType() != EventType.REGISTER) {
            if (event.getType() == EventType.UPDATE_PROFILE) {
                escapeHtmlProfile(event);
            }
        }  //escapeHtmlRegistration(event); does not work

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
        final Stream<GroupModel> groupsStream = updatedUser.getGroupsStream();
        groupsStream.forEach(updatedUser::leaveGroup);

    }

//    private void escapeHtmlRegistration(Event event) {
//
//        final Map<String, String> details = event.getDetails();
//
//        final UserModel updatedUser = session.users().getUserById(model.getRealm(event.getRealmId()), event.getUserId());
//        for (String key : details.keySet()) {
//            final String updatedValue = details.get(key);
//            switch (key){
//                case "first_name":
//                    updatedUser.setFirstName(StringEscapeUtils.escapeHtml4(updatedValue));
//                    break;
//                case "last_name":
//                    updatedUser.setLastName(StringEscapeUtils.escapeHtml4(updatedValue));
//                    break;
//            }
//        }
//    }

    private void escapeHtmlProfile(Event event) {

        final Map<String, String> details = event.getDetails();

        final UserModel updatedUser = session.users().getUserById(model.getRealm(event.getRealmId()), event.getUserId());

        for (String key : details.keySet()) {
            if (!key.startsWith("updated_")) continue;
            final String updatedValue = details.get(key);

            switch (key){
                case "updated_first_name":
                    updatedUser.setFirstName(StringEscapeUtils.escapeHtml4(updatedValue));
                    break;
                case "updated_last_name":
                    updatedUser.setLastName(StringEscapeUtils.escapeHtml4(updatedValue));
                    break;
                default:
                    final String attributeKey = key.substring("updated_".length());
                    final List<String> escapedValue = Collections.singletonList(StringEscapeUtils.escapeHtml4(updatedValue));
                    updatedUser.setAttribute(attributeKey, escapedValue);
                    break;
            }
        }

        if (updatedUser.getEmail() == null && details.containsKey("previous_email")){
            //apparently needs to be reset when changes take place
            updatedUser.setEmail(details.get("previous_email"));
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
        return session.users().getUserById(realm, userId);
    }

    @Override
    public void close() {
//
    }
}
