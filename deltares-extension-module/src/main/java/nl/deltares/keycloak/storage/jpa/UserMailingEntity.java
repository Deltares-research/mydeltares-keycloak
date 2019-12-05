package nl.deltares.keycloak.storage.jpa;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USER_MAILING", schema = "keycloak", catalog = "")
public class UserMailingEntity {
    private String id;
    private String language;
    private Byte delivery;
    private RealmEntity realmByRealmId;
    private UserEntityEntity userEntityByUserId;
    private MailingEntityEntity mailingEntityByMailingId;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LANGUAGE")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "DELIVERY")
    public Byte getDelivery() {
        return delivery;
    }

    public void setDelivery(Byte delivery) {
        this.delivery = delivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMailingEntity that = (UserMailingEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(language, that.language) &&
                Objects.equals(delivery, that.delivery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, language, delivery);
    }

    @ManyToOne
    @JoinColumn(name = "REALM_ID", referencedColumnName = "ID", nullable = false)
    public RealmEntity getRealmByRealmId() {
        return realmByRealmId;
    }

    public void setRealmByRealmId(RealmEntity realmByRealmId) {
        this.realmByRealmId = realmByRealmId;
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
    public UserEntityEntity getUserEntityByUserId() {
        return userEntityByUserId;
    }

    public void setUserEntityByUserId(UserEntityEntity userEntityByUserId) {
        this.userEntityByUserId = userEntityByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "MAILING_ID", referencedColumnName = "ID", nullable = false)
    public MailingEntityEntity getMailingEntityByMailingId() {
        return mailingEntityByMailingId;
    }

    public void setMailingEntityByMailingId(MailingEntityEntity mailingEntityByMailingId) {
        this.mailingEntityByMailingId = mailingEntityByMailingId;
    }
}
