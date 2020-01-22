package nl.deltares.keycloak.storage.jpa;

import org.keycloak.models.jpa.entities.RealmEntity;
import org.keycloak.models.jpa.entities.UserEntity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "USER_AVATAR", schema = "keycloak", catalog = "")
public class UserAvatarEntity {
    private String id;
    private byte[] avatar;
    private String contentType;
    private RealmEntity realmByRealmId;
    private UserEntity userEntityByUserId;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CONTENT_TYPE")
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Basic
    @Column(name = "AVATAR")
    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAvatarEntity that = (UserAvatarEntity) o;
        return Objects.equals(id, that.id) &&
                Arrays.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
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
    public UserEntity getUserEntityByUserId() {
        return userEntityByUserId;
    }

    public void setUserEntityByUserId(UserEntity userEntityByUserId) {
        this.userEntityByUserId = userEntityByUserId;
    }
}
