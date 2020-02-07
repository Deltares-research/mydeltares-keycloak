package nl.deltares.keycloak.provider.user;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.rest.*;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;


public class UserEventListenerProvider implements EventListenerProvider {
    private static final Logger logger = Logger.getLogger(UserEventListenerProvider.class);
    private final KeycloakSession session;
    private final RealmProvider model;

    public UserEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {
        if (event.getType() != EventType.REGISTER) return;
        handleEvent(event.getRealmId(), event.getUserId());
    }

    private void handleEvent(String realmId, String userId) {
        if (realmId == null || userId == null) return;
        RealmModel realm = model.getRealm(realmId);
        UserModel newUser = session.users().getUserById(userId, realm);

        List<Mailing> mailingsByRealm = MailingAdminResource.getMailingsByRealm(session, realm.getId());
        mailingsByRealm.forEach(mailing -> {
            UserMailing userMailing = getUserMailing(mailing, newUser);
            if (userMailing != null) getEntityManager(session).persist(userMailing);

        });
    }

    private UserMailing getUserMailing(Mailing mailing, UserModel newUser) {

        if (UserMailingResource.getUserMailing(session, mailing.getRealmId(), newUser.getId(), mailing.getId()) != null) {
            logger.warn(String.format("UserMailing already exists! mailing=%s, user email=%s", mailing.getName(), newUser.getEmail()));
            return null;
        }

        UserMailing representation = new UserMailing();
        representation.setId(KeycloakModelUtils.generateId());
        representation.setUserId(newUser.getId());
        representation.setMailingId(mailing.getId());
        representation.setDelivery(mailing.getDelivery());
        representation.setRealmId(mailing.getRealmId());
        representation.setLanguage(mailing.getLanguages()[0]);
        return representation;
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if (event.getOperationType() != OperationType.CREATE) return;
        String[] split = event.getResourcePath().split("/");
        if (split.length > 1) {
            handleEvent(event.getRealmId(), split[1]);
        } else {
            handleEvent(event.getRealmId(), split[0]);
        }
    }

    @Override
    public void close() {

    }
}
