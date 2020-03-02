package nl.deltares.keycloak.forms.account.freemarker.model;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.rest.MailingRepresentation;
import nl.deltares.keycloak.storage.rest.ResourceUtils;
import nl.deltares.keycloak.storage.rest.UserMailingRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UserMailingsBean {

    private final List<UserMailingBean> beans;

    public UserMailingsBean(List<UserMailingRepresentation> userMailings, List<MailingRepresentation> mailings) {

        beans = new LinkedList<>();
        for (MailingRepresentation mailing : mailings) {
            beans.add(new UserMailingBean(getUserMailing(mailing.getId(), userMailings), mailing));
        }
    }

    private UserMailingRepresentation getUserMailing(String mailingId, List<UserMailingRepresentation> userMailings) {
        for (UserMailingRepresentation userMailing : userMailings) {
            if (userMailing.getMailingId().equals(mailingId)) return userMailing;
        }
        return null;
    }

    public List<UserMailingBean> getMailings() {
        return beans;
    }

    public static class UserMailingBean {
        private UserMailingRepresentation userMailing;
        private MailingRepresentation mailing;

        public UserMailingBean(UserMailingRepresentation userMailing, MailingRepresentation mailing) {
            this.userMailing = userMailing;
            this.mailing = mailing;
        }

        public String getId(){
            return userMailing == null ? "" : userMailing.getId();
        }

        public String getMailingId(){
            return mailing.getId();
        }

        public String getName(){
            return mailing.getName();
        }

        public String getDescription(){
            return mailing.getDescription();
        }

        public boolean isEnabled(){
            return userMailing != null;
        }

        public String getLanguage(){
            return userMailing == null ? "en" : userMailing.getLanguage();
        }

        public String getDelivery(){
            return userMailing == null ? Mailing.getPreferredMailingDeliveryText() : userMailing.getDeliveryTxt();
        }

        public String getFrequency(){
            return mailing.getFrequencyTxt();
        }

        public List<String> getSupportedDeliveries(){

            ArrayList<String> supported = new ArrayList<>();
            List<String> supportedDeliveries = mailing.getSupportedDeliveries();
            int delivery = mailing.getDelivery();
            if (delivery == 0){
                supported.add(supportedDeliveries.get(0));
            } else if(delivery == 1){
                supported.add(supportedDeliveries.get(1));
            } else {
                supported.addAll(supportedDeliveries);
            }
            return supported;
        }

        public List<String> getSupportedLanguages(){
            return Arrays.asList(mailing.getLanguages());
        }
    }
}
