package nl.deltares.keycloak.storage.jpa;

import javax.persistence.*;

@NamedQueries({
    @NamedQuery(name = "getAllByRealm", query = "from Mailing where realmId = :realmId order by name"),
    @NamedQuery(name = "findById", query = "from Mailing where id = :id and realmId = :realmId"),
    @NamedQuery(name = "findByName", query = "from Mailing where name = :name and realmId = :realmId")
})
@Entity
@Table(name = "MAILING_ENTITY")
public class Mailing {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REALM_ID", nullable = false)
    private String realmId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LANGUAGES", nullable = false)
    private String languages="en";

    @Column(name = "FREQUENCY", nullable = false)
    private int frequency=3;

    @Column(name = "DELIVERY", nullable = false)
    private int delivery=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getLanguages() {
        if (languages.length() == 2) return new String[]{languages};
        return languages.split(";");
    }

    public void setLanguages(String[] languages) {
        if (languages == null || languages.length == 0) throw new IllegalArgumentException("null or missing languages");

        if (languages.length == 1){
            this.languages = languages[0];
        } else {
            this.languages = String.join(";", languages);
        }
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        if (frequency < 0 || frequency > 3) throw new IllegalArgumentException("invalid frequency " + frequency);
        this.frequency = frequency;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        if (delivery < 0 || delivery > 2) throw new IllegalArgumentException("invalid delivery " + frequency);

        this.delivery = delivery;
    }
}