package nl.deltares.keycloak.forms.account.freemarker.model;

import org.keycloak.forms.account.freemarker.model.UrlBean;
import org.keycloak.models.RealmModel;
import org.keycloak.theme.Theme;

import java.net.URI;

public class ExtendedUrlBean extends UrlBean {

    private static String accountPattern = "^(.*)(/account/?)(\\?(.*))?$";

    public ExtendedUrlBean(RealmModel realm, Theme theme, URI baseURI, URI baseQueryURI, URI currentURI, String stateChecker) {
        super(realm, theme, baseURI, baseQueryURI, currentURI, stateChecker);
    }

    /**
     * Path to Avatar API endpoint
     *
     * @return
     */
    public String getAvatarUrl() {
        String accountUrl = super.getAccountUrl();
        return accountUrl.replaceFirst(accountPattern, "$1/avatar-provider");
    }

    /**
     * Path to Mailings page endpoint and path parameters
     *
     * @return
     */
    public String getMailingsUrl() {
        String accountUrl = super.getAccountUrl();
        return accountUrl.replaceFirst(accountPattern, "$1/user-mailings/mailings-page?$4");
    }

    /**
     * Path to UserMailings API endpoint
     *
     * @return
     */
    public String getUserMailingsUrl() {
        String accountUrl = super.getAccountUrl();
        return accountUrl.replaceFirst(accountPattern, "$1/user-mailings");
    }

    /**
     * @return
     */
    public String getPathParams() {
        String accountUrl = super.getAccountUrl();
        return accountUrl.replaceFirst(accountPattern, "$4");
    }
}
