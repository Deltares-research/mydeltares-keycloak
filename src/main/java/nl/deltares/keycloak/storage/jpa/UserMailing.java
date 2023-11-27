package nl.deltares.keycloak.storage.jpa;

import jakarta.persistence.*;

@NamedQueries({
        @NamedQuery(name = "findUserMailingByUserAndRealm", query = "select m from UserMailing m where m.userId = :userId and m.realmId = :realmId"),
        @NamedQuery(name = "allUserMailingsByMailingAndRealm", query = "select m from UserMailing m where m.mailingId = :mailingId and m.realmId = :realmId"),
        @NamedQuery(name = "getUserMailing", query = "select m from UserMailing m where m.mailingId = :mailingId and m.realmId = :realmId and m.userId = :userId"),
        @NamedQuery(name = "getUserMailingById", query = "select m from UserMailing m where m.id = :id")
})
@Entity
@Table(name = "USER_MAILING")
public class UserMailing {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REALM_ID", nullable = false)
    private String realmId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "MAILING_ID", nullable = false)
    private String mailingId;

    @Column(name = "LANGUAGE", nullable = false)
    private String language="en";

    @Column(name = "DELIVERY", nullable = false)
    private int delivery=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (language == null) throw new IllegalArgumentException("missing language");
        if (language.length() > 2) throw new IllegalArgumentException("invalid language. expected two character country code. found " + language);
       this.language = language;

    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        if (delivery < 0 || delivery > 2) throw new IllegalArgumentException("invalid delivery " + delivery);
        this.delivery = delivery;
    }

    public String getMailingId() {
        return mailingId;
    }

    public void setMailingId(String mailingId) {
        this.mailingId = mailingId;
    }
}