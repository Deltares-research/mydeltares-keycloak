package nl.deltares.keycloak.storage.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nl.deltares.keycloak.storage.jpa.Mailing;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMailingRepresentation {

    protected String id;
    protected String mailingId;
    protected String userId;
    protected String realmId;
    protected String language;
    protected int delivery;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMailingId(String mailingId) {
        this.mailingId = mailingId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public String getMailingId() {
        return mailingId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRealmId() {
        return realmId;
    }

    public String getLanguage() {
        return language;
    }

    public int getDelivery() {
        return delivery;
    }

    public String getDeliveryTxt() {
        return Mailing.deliveries.get(delivery);
    }

//    public void setDeliveryTxt(String deliveryTxt) {
//        int delivery = Mailing.deliveries.indexOf(deliveryTxt);
//        this.delivery = delivery == -1 ? 0 : delivery;
//    }
}
