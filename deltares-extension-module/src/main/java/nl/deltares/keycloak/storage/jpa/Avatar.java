package nl.deltares.keycloak.storage.jpa;

import javax.persistence.*;

@NamedQueries({
   @NamedQuery(name = "findByUser", query = "from Avatar where userId = :userId and realmId = :realmId")
})
@Entity
@Table(name = "USER_AVATAR")
public class Avatar {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REALM_ID", nullable = false)
    private String realmId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "AVATAR", nullable = false)
    private byte[] avatar;

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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}