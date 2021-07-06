package nl.deltares.keycloak.storage.jpa;

import org.keycloak.models.jpa.entities.RealmEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "MAILING_ENTITY", schema = "keycloak")
public class MailingEntityEntity {
    private String id;
    private String name;
    private String description;
    private String languages;
    private Byte frequency;
    private Byte delivery;
    private Long createdTimestamp;
    private RealmEntity realmByRealmId;
    private Collection<UserMailingEntity> userMailingsById;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "LANGUAGES")
    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    @Basic
    @Column(name = "FREQUENCY")
    public Byte getFrequency() {
        return frequency;
    }

    public void setFrequency(Byte frequency) {
        this.frequency = frequency;
    }

    @Basic
    @Column(name = "DELIVERY")
    public Byte getDelivery() {
        return delivery;
    }

    public void setDelivery(Byte delivery) {
        this.delivery = delivery;
    }

    @Basic
    @Column(name = "CREATED_TIMESTAMP")
    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailingEntityEntity that = (MailingEntityEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(languages, that.languages) &&
                Objects.equals(frequency, that.frequency) &&
                Objects.equals(delivery, that.delivery) &&
                Objects.equals(createdTimestamp, that.createdTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, languages, frequency, delivery, createdTimestamp);
    }

    @ManyToOne
    @JoinColumn(name = "REALM_ID", referencedColumnName = "ID", nullable = false)
    public RealmEntity getRealmByRealmId() {
        return realmByRealmId;
    }

    public void setRealmByRealmId(RealmEntity realmByRealmId) {
        this.realmByRealmId = realmByRealmId;
    }

    @OneToMany(mappedBy = "mailingEntityByMailingId")
    public Collection<UserMailingEntity> getUserMailingsById() {
        return userMailingsById;
    }

    public void setUserMailingsById(Collection<UserMailingEntity> userMailingsById) {
        this.userMailingsById = userMailingsById;
    }
}
