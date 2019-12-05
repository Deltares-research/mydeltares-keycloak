package nl.deltares.keycloak.storage.jpa;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USER_ENTITY", schema = "keycloak", catalog = "")
public class UserEntityEntity {
    private String id;
    private String email;
    private String emailConstraint;
    private Boolean emailVerified;
    private Boolean enabled;
    private String federationLink;
    private String firstName;
    private String lastName;
    private String realmId;
    private String username;
    private Long createdTimestamp;
    private String serviceAccountClientLink;
    private Integer notBefore;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "EMAIL_CONSTRAINT")
    public String getEmailConstraint() {
        return emailConstraint;
    }

    public void setEmailConstraint(String emailConstraint) {
        this.emailConstraint = emailConstraint;
    }

    @Basic
    @Column(name = "EMAIL_VERIFIED")
    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Basic
    @Column(name = "ENABLED")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name = "FEDERATION_LINK")
    public String getFederationLink() {
        return federationLink;
    }

    public void setFederationLink(String federationLink) {
        this.federationLink = federationLink;
    }

    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "REALM_ID")
    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    @Basic
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "CREATED_TIMESTAMP")
    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Basic
    @Column(name = "SERVICE_ACCOUNT_CLIENT_LINK")
    public String getServiceAccountClientLink() {
        return serviceAccountClientLink;
    }

    public void setServiceAccountClientLink(String serviceAccountClientLink) {
        this.serviceAccountClientLink = serviceAccountClientLink;
    }

    @Basic
    @Column(name = "NOT_BEFORE")
    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntityEntity that = (UserEntityEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(emailConstraint, that.emailConstraint) &&
                Objects.equals(emailVerified, that.emailVerified) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(federationLink, that.federationLink) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(realmId, that.realmId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(createdTimestamp, that.createdTimestamp) &&
                Objects.equals(serviceAccountClientLink, that.serviceAccountClientLink) &&
                Objects.equals(notBefore, that.notBefore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, emailConstraint, emailVerified, enabled, federationLink, firstName, lastName, realmId, username, createdTimestamp, serviceAccountClientLink, notBefore);
    }
}
