package nl.deltares.keycloak.storage.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MailingRepresentation {

    protected String id;
    protected String realmId;
    protected String name;
    protected String description;
    protected String[] languages = new String[]{"en"};
    protected int frequency = 3;
    protected int delivery = 0;
    protected Long createdTimestamp;
    private Map<String, Boolean> access;
    private static List<String> frequencies;
    private static List<String> deliveries;

    static {
        frequencies = Arrays.asList("weekly", "montly", "anually", "other");
        deliveries = Arrays.asList("email", "post", "both");
    }

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
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getFrequencyTxt() {
        return getSupportedFrequencies().get(frequency);
    }

    public void setFrequencyTxt(String frequencies) {
        int frequency = this.frequencies.indexOf(frequencies);
        this.frequency = frequency == -1 ? 3 : frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getDelivery() {
        return delivery;
    }

    public String getDeliveryTxt() {
        return getSupportedDeliveries().get(delivery);
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void getCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Map<String, Boolean> getAccess() {
        return access;
    }

    public void setAccess(Map<String, Boolean> access) {
        this.access = access;
    }

    public List<String> getSupportedFrequencies() {
        return new ArrayList<>(frequencies);
    }

    public List<String> getSupportedDeliveries() {
        return new ArrayList<>(deliveries);
    }

}

