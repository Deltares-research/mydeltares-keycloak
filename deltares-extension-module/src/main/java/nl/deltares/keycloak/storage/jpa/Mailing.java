package nl.deltares.keycloak.storage.jpa;

import org.apache.log4j.lf5.util.ResourceUtils;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@NamedQueries({
    @NamedQuery(name = "getAllMailingsByRealm", query = "select m from Mailing m where m.realmId = :realmId order by m.name"),
    @NamedQuery(name = "findMailingByIdAndRealm", query = "select m from Mailing m where m.id = :id and m.realmId = :realmId"),
    @NamedQuery(name = "findMailingByNameAndRealm", query = "select m from Mailing m where m.name = :name and m.realmId = :realmId"),
    @NamedQuery(name= "searchForMailing", query="select m from Mailing m where m.realmId = :realmId and lower(m.name) like :search order by m.name")

})
@Entity
@Table(name = "MAILING_ENTITY")
public class Mailing {

    public static List<String> frequencies;
    public static List<String> deliveries;

    public static int getPreferredMailingDelivery() {return 0;}

    public static String getPreferredMailingDeliveryText() {return deliveries.get(getPreferredMailingDelivery());}


    static {
        frequencies = Arrays.asList("weekly", "monthly", "quarterly", "annually", "varying");
        deliveries = Arrays.asList("e-mail", "post", "both");
    }

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

    @Column(name = "CREATED_TIMESTAMP")
    protected Long createdTimestamp;

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
        if (frequency < 0 || frequency > 4) throw new IllegalArgumentException("invalid frequency " + frequency);
        this.frequency = frequency;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        if (delivery < 0 || delivery > 2) throw new IllegalArgumentException("invalid delivery " + frequency);

        this.delivery = delivery;
    }

    public boolean isValidDelivery(int delivery){
        if (this.delivery == delivery) return true; //same
        if (delivery < 0 || delivery > 2) return false; //out of bounds
        return this.delivery == deliveries.indexOf("both"); //is mailing = 'both' the user mailing can be all options
    }

    public boolean isValidLanguage(String language){
        return Arrays.asList(getLanguages()).contains(language);
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long timestamp) {
        createdTimestamp = timestamp;
    }

}