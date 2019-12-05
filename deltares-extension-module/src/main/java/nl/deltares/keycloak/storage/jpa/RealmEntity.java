package nl.deltares.keycloak.storage.jpa;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "REALM", schema = "keycloak", catalog = "")
public class RealmEntity {
    private String id;
    private Integer accessCodeLifespan;
    private Integer userActionLifespan;
    private Integer accessTokenLifespan;
    private String accountTheme;
    private String adminTheme;
    private String emailTheme;
    private Boolean enabled;
    private Boolean eventsEnabled;
    private Long eventsExpiration;
    private String loginTheme;
    private String name;
    private Integer notBefore;
    private String passwordPolicy;
    private Boolean registrationAllowed;
    private Boolean rememberMe;
    private Boolean resetPasswordAllowed;
    private Boolean social;
    private String sslRequired;
    private Integer ssoIdleTimeout;
    private Integer ssoMaxLifespan;
    private Boolean updateProfileOnSocLogin;
    private Boolean verifyEmail;
    private Integer loginLifespan;
    private Boolean internationalizationEnabled;
    private String defaultLocale;
    private Boolean regEmailAsUsername;
    private Boolean adminEventsEnabled;
    private Boolean adminEventsDetailsEnabled;
    private Boolean editUsernameAllowed;
    private Integer otpPolicyCounter;
    private Integer otpPolicyWindow;
    private Integer otpPolicyPeriod;
    private Integer otpPolicyDigits;
    private String otpPolicyAlg;
    private String otpPolicyType;
    private String browserFlow;
    private String registrationFlow;
    private String directGrantFlow;
    private String resetCredentialsFlow;
    private String clientAuthFlow;
    private Integer offlineSessionIdleTimeout;
    private Boolean revokeRefreshToken;
    private Integer accessTokenLifeImplicit;
    private Boolean loginWithEmailAllowed;
    private Boolean duplicateEmailsAllowed;
    private String dockerAuthFlow;
    private Integer refreshTokenMaxReuse;
    private Boolean allowUserManagedAccess;
    private Integer ssoMaxLifespanRememberMe;
    private Integer ssoIdleTimeoutRememberMe;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACCESS_CODE_LIFESPAN")
    public Integer getAccessCodeLifespan() {
        return accessCodeLifespan;
    }

    public void setAccessCodeLifespan(Integer accessCodeLifespan) {
        this.accessCodeLifespan = accessCodeLifespan;
    }

    @Basic
    @Column(name = "USER_ACTION_LIFESPAN")
    public Integer getUserActionLifespan() {
        return userActionLifespan;
    }

    public void setUserActionLifespan(Integer userActionLifespan) {
        this.userActionLifespan = userActionLifespan;
    }

    @Basic
    @Column(name = "ACCESS_TOKEN_LIFESPAN")
    public Integer getAccessTokenLifespan() {
        return accessTokenLifespan;
    }

    public void setAccessTokenLifespan(Integer accessTokenLifespan) {
        this.accessTokenLifespan = accessTokenLifespan;
    }

    @Basic
    @Column(name = "ACCOUNT_THEME")
    public String getAccountTheme() {
        return accountTheme;
    }

    public void setAccountTheme(String accountTheme) {
        this.accountTheme = accountTheme;
    }

    @Basic
    @Column(name = "ADMIN_THEME")
    public String getAdminTheme() {
        return adminTheme;
    }

    public void setAdminTheme(String adminTheme) {
        this.adminTheme = adminTheme;
    }

    @Basic
    @Column(name = "EMAIL_THEME")
    public String getEmailTheme() {
        return emailTheme;
    }

    public void setEmailTheme(String emailTheme) {
        this.emailTheme = emailTheme;
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
    @Column(name = "EVENTS_ENABLED")
    public Boolean getEventsEnabled() {
        return eventsEnabled;
    }

    public void setEventsEnabled(Boolean eventsEnabled) {
        this.eventsEnabled = eventsEnabled;
    }

    @Basic
    @Column(name = "EVENTS_EXPIRATION")
    public Long getEventsExpiration() {
        return eventsExpiration;
    }

    public void setEventsExpiration(Long eventsExpiration) {
        this.eventsExpiration = eventsExpiration;
    }

    @Basic
    @Column(name = "LOGIN_THEME")
    public String getLoginTheme() {
        return loginTheme;
    }

    public void setLoginTheme(String loginTheme) {
        this.loginTheme = loginTheme;
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
    @Column(name = "NOT_BEFORE")
    public Integer getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Integer notBefore) {
        this.notBefore = notBefore;
    }

    @Basic
    @Column(name = "PASSWORD_POLICY")
    public String getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(String passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }

    @Basic
    @Column(name = "REGISTRATION_ALLOWED")
    public Boolean getRegistrationAllowed() {
        return registrationAllowed;
    }

    public void setRegistrationAllowed(Boolean registrationAllowed) {
        this.registrationAllowed = registrationAllowed;
    }

    @Basic
    @Column(name = "REMEMBER_ME")
    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Basic
    @Column(name = "RESET_PASSWORD_ALLOWED")
    public Boolean getResetPasswordAllowed() {
        return resetPasswordAllowed;
    }

    public void setResetPasswordAllowed(Boolean resetPasswordAllowed) {
        this.resetPasswordAllowed = resetPasswordAllowed;
    }

    @Basic
    @Column(name = "SOCIAL")
    public Boolean getSocial() {
        return social;
    }

    public void setSocial(Boolean social) {
        this.social = social;
    }

    @Basic
    @Column(name = "SSL_REQUIRED")
    public String getSslRequired() {
        return sslRequired;
    }

    public void setSslRequired(String sslRequired) {
        this.sslRequired = sslRequired;
    }

    @Basic
    @Column(name = "SSO_IDLE_TIMEOUT")
    public Integer getSsoIdleTimeout() {
        return ssoIdleTimeout;
    }

    public void setSsoIdleTimeout(Integer ssoIdleTimeout) {
        this.ssoIdleTimeout = ssoIdleTimeout;
    }

    @Basic
    @Column(name = "SSO_MAX_LIFESPAN")
    public Integer getSsoMaxLifespan() {
        return ssoMaxLifespan;
    }

    public void setSsoMaxLifespan(Integer ssoMaxLifespan) {
        this.ssoMaxLifespan = ssoMaxLifespan;
    }

    @Basic
    @Column(name = "UPDATE_PROFILE_ON_SOC_LOGIN")
    public Boolean getUpdateProfileOnSocLogin() {
        return updateProfileOnSocLogin;
    }

    public void setUpdateProfileOnSocLogin(Boolean updateProfileOnSocLogin) {
        this.updateProfileOnSocLogin = updateProfileOnSocLogin;
    }

    @Basic
    @Column(name = "VERIFY_EMAIL")
    public Boolean getVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(Boolean verifyEmail) {
        this.verifyEmail = verifyEmail;
    }

    @Basic
    @Column(name = "LOGIN_LIFESPAN")
    public Integer getLoginLifespan() {
        return loginLifespan;
    }

    public void setLoginLifespan(Integer loginLifespan) {
        this.loginLifespan = loginLifespan;
    }

    @Basic
    @Column(name = "INTERNATIONALIZATION_ENABLED")
    public Boolean getInternationalizationEnabled() {
        return internationalizationEnabled;
    }

    public void setInternationalizationEnabled(Boolean internationalizationEnabled) {
        this.internationalizationEnabled = internationalizationEnabled;
    }

    @Basic
    @Column(name = "DEFAULT_LOCALE")
    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Basic
    @Column(name = "REG_EMAIL_AS_USERNAME")
    public Boolean getRegEmailAsUsername() {
        return regEmailAsUsername;
    }

    public void setRegEmailAsUsername(Boolean regEmailAsUsername) {
        this.regEmailAsUsername = regEmailAsUsername;
    }

    @Basic
    @Column(name = "ADMIN_EVENTS_ENABLED")
    public Boolean getAdminEventsEnabled() {
        return adminEventsEnabled;
    }

    public void setAdminEventsEnabled(Boolean adminEventsEnabled) {
        this.adminEventsEnabled = adminEventsEnabled;
    }

    @Basic
    @Column(name = "ADMIN_EVENTS_DETAILS_ENABLED")
    public Boolean getAdminEventsDetailsEnabled() {
        return adminEventsDetailsEnabled;
    }

    public void setAdminEventsDetailsEnabled(Boolean adminEventsDetailsEnabled) {
        this.adminEventsDetailsEnabled = adminEventsDetailsEnabled;
    }

    @Basic
    @Column(name = "EDIT_USERNAME_ALLOWED")
    public Boolean getEditUsernameAllowed() {
        return editUsernameAllowed;
    }

    public void setEditUsernameAllowed(Boolean editUsernameAllowed) {
        this.editUsernameAllowed = editUsernameAllowed;
    }

    @Basic
    @Column(name = "OTP_POLICY_COUNTER")
    public Integer getOtpPolicyCounter() {
        return otpPolicyCounter;
    }

    public void setOtpPolicyCounter(Integer otpPolicyCounter) {
        this.otpPolicyCounter = otpPolicyCounter;
    }

    @Basic
    @Column(name = "OTP_POLICY_WINDOW")
    public Integer getOtpPolicyWindow() {
        return otpPolicyWindow;
    }

    public void setOtpPolicyWindow(Integer otpPolicyWindow) {
        this.otpPolicyWindow = otpPolicyWindow;
    }

    @Basic
    @Column(name = "OTP_POLICY_PERIOD")
    public Integer getOtpPolicyPeriod() {
        return otpPolicyPeriod;
    }

    public void setOtpPolicyPeriod(Integer otpPolicyPeriod) {
        this.otpPolicyPeriod = otpPolicyPeriod;
    }

    @Basic
    @Column(name = "OTP_POLICY_DIGITS")
    public Integer getOtpPolicyDigits() {
        return otpPolicyDigits;
    }

    public void setOtpPolicyDigits(Integer otpPolicyDigits) {
        this.otpPolicyDigits = otpPolicyDigits;
    }

    @Basic
    @Column(name = "OTP_POLICY_ALG")
    public String getOtpPolicyAlg() {
        return otpPolicyAlg;
    }

    public void setOtpPolicyAlg(String otpPolicyAlg) {
        this.otpPolicyAlg = otpPolicyAlg;
    }

    @Basic
    @Column(name = "OTP_POLICY_TYPE")
    public String getOtpPolicyType() {
        return otpPolicyType;
    }

    public void setOtpPolicyType(String otpPolicyType) {
        this.otpPolicyType = otpPolicyType;
    }

    @Basic
    @Column(name = "BROWSER_FLOW")
    public String getBrowserFlow() {
        return browserFlow;
    }

    public void setBrowserFlow(String browserFlow) {
        this.browserFlow = browserFlow;
    }

    @Basic
    @Column(name = "REGISTRATION_FLOW")
    public String getRegistrationFlow() {
        return registrationFlow;
    }

    public void setRegistrationFlow(String registrationFlow) {
        this.registrationFlow = registrationFlow;
    }

    @Basic
    @Column(name = "DIRECT_GRANT_FLOW")
    public String getDirectGrantFlow() {
        return directGrantFlow;
    }

    public void setDirectGrantFlow(String directGrantFlow) {
        this.directGrantFlow = directGrantFlow;
    }

    @Basic
    @Column(name = "RESET_CREDENTIALS_FLOW")
    public String getResetCredentialsFlow() {
        return resetCredentialsFlow;
    }

    public void setResetCredentialsFlow(String resetCredentialsFlow) {
        this.resetCredentialsFlow = resetCredentialsFlow;
    }

    @Basic
    @Column(name = "CLIENT_AUTH_FLOW")
    public String getClientAuthFlow() {
        return clientAuthFlow;
    }

    public void setClientAuthFlow(String clientAuthFlow) {
        this.clientAuthFlow = clientAuthFlow;
    }

    @Basic
    @Column(name = "OFFLINE_SESSION_IDLE_TIMEOUT")
    public Integer getOfflineSessionIdleTimeout() {
        return offlineSessionIdleTimeout;
    }

    public void setOfflineSessionIdleTimeout(Integer offlineSessionIdleTimeout) {
        this.offlineSessionIdleTimeout = offlineSessionIdleTimeout;
    }

    @Basic
    @Column(name = "REVOKE_REFRESH_TOKEN")
    public Boolean getRevokeRefreshToken() {
        return revokeRefreshToken;
    }

    public void setRevokeRefreshToken(Boolean revokeRefreshToken) {
        this.revokeRefreshToken = revokeRefreshToken;
    }

    @Basic
    @Column(name = "ACCESS_TOKEN_LIFE_IMPLICIT")
    public Integer getAccessTokenLifeImplicit() {
        return accessTokenLifeImplicit;
    }

    public void setAccessTokenLifeImplicit(Integer accessTokenLifeImplicit) {
        this.accessTokenLifeImplicit = accessTokenLifeImplicit;
    }

    @Basic
    @Column(name = "LOGIN_WITH_EMAIL_ALLOWED")
    public Boolean getLoginWithEmailAllowed() {
        return loginWithEmailAllowed;
    }

    public void setLoginWithEmailAllowed(Boolean loginWithEmailAllowed) {
        this.loginWithEmailAllowed = loginWithEmailAllowed;
    }

    @Basic
    @Column(name = "DUPLICATE_EMAILS_ALLOWED")
    public Boolean getDuplicateEmailsAllowed() {
        return duplicateEmailsAllowed;
    }

    public void setDuplicateEmailsAllowed(Boolean duplicateEmailsAllowed) {
        this.duplicateEmailsAllowed = duplicateEmailsAllowed;
    }

    @Basic
    @Column(name = "DOCKER_AUTH_FLOW")
    public String getDockerAuthFlow() {
        return dockerAuthFlow;
    }

    public void setDockerAuthFlow(String dockerAuthFlow) {
        this.dockerAuthFlow = dockerAuthFlow;
    }

    @Basic
    @Column(name = "REFRESH_TOKEN_MAX_REUSE")
    public Integer getRefreshTokenMaxReuse() {
        return refreshTokenMaxReuse;
    }

    public void setRefreshTokenMaxReuse(Integer refreshTokenMaxReuse) {
        this.refreshTokenMaxReuse = refreshTokenMaxReuse;
    }

    @Basic
    @Column(name = "ALLOW_USER_MANAGED_ACCESS")
    public Boolean getAllowUserManagedAccess() {
        return allowUserManagedAccess;
    }

    public void setAllowUserManagedAccess(Boolean allowUserManagedAccess) {
        this.allowUserManagedAccess = allowUserManagedAccess;
    }

    @Basic
    @Column(name = "SSO_MAX_LIFESPAN_REMEMBER_ME")
    public Integer getSsoMaxLifespanRememberMe() {
        return ssoMaxLifespanRememberMe;
    }

    public void setSsoMaxLifespanRememberMe(Integer ssoMaxLifespanRememberMe) {
        this.ssoMaxLifespanRememberMe = ssoMaxLifespanRememberMe;
    }

    @Basic
    @Column(name = "SSO_IDLE_TIMEOUT_REMEMBER_ME")
    public Integer getSsoIdleTimeoutRememberMe() {
        return ssoIdleTimeoutRememberMe;
    }

    public void setSsoIdleTimeoutRememberMe(Integer ssoIdleTimeoutRememberMe) {
        this.ssoIdleTimeoutRememberMe = ssoIdleTimeoutRememberMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealmEntity that = (RealmEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accessCodeLifespan, that.accessCodeLifespan) &&
                Objects.equals(userActionLifespan, that.userActionLifespan) &&
                Objects.equals(accessTokenLifespan, that.accessTokenLifespan) &&
                Objects.equals(accountTheme, that.accountTheme) &&
                Objects.equals(adminTheme, that.adminTheme) &&
                Objects.equals(emailTheme, that.emailTheme) &&
                Objects.equals(enabled, that.enabled) &&
                Objects.equals(eventsEnabled, that.eventsEnabled) &&
                Objects.equals(eventsExpiration, that.eventsExpiration) &&
                Objects.equals(loginTheme, that.loginTheme) &&
                Objects.equals(name, that.name) &&
                Objects.equals(notBefore, that.notBefore) &&
                Objects.equals(passwordPolicy, that.passwordPolicy) &&
                Objects.equals(registrationAllowed, that.registrationAllowed) &&
                Objects.equals(rememberMe, that.rememberMe) &&
                Objects.equals(resetPasswordAllowed, that.resetPasswordAllowed) &&
                Objects.equals(social, that.social) &&
                Objects.equals(sslRequired, that.sslRequired) &&
                Objects.equals(ssoIdleTimeout, that.ssoIdleTimeout) &&
                Objects.equals(ssoMaxLifespan, that.ssoMaxLifespan) &&
                Objects.equals(updateProfileOnSocLogin, that.updateProfileOnSocLogin) &&
                Objects.equals(verifyEmail, that.verifyEmail) &&
                Objects.equals(loginLifespan, that.loginLifespan) &&
                Objects.equals(internationalizationEnabled, that.internationalizationEnabled) &&
                Objects.equals(defaultLocale, that.defaultLocale) &&
                Objects.equals(regEmailAsUsername, that.regEmailAsUsername) &&
                Objects.equals(adminEventsEnabled, that.adminEventsEnabled) &&
                Objects.equals(adminEventsDetailsEnabled, that.adminEventsDetailsEnabled) &&
                Objects.equals(editUsernameAllowed, that.editUsernameAllowed) &&
                Objects.equals(otpPolicyCounter, that.otpPolicyCounter) &&
                Objects.equals(otpPolicyWindow, that.otpPolicyWindow) &&
                Objects.equals(otpPolicyPeriod, that.otpPolicyPeriod) &&
                Objects.equals(otpPolicyDigits, that.otpPolicyDigits) &&
                Objects.equals(otpPolicyAlg, that.otpPolicyAlg) &&
                Objects.equals(otpPolicyType, that.otpPolicyType) &&
                Objects.equals(browserFlow, that.browserFlow) &&
                Objects.equals(registrationFlow, that.registrationFlow) &&
                Objects.equals(directGrantFlow, that.directGrantFlow) &&
                Objects.equals(resetCredentialsFlow, that.resetCredentialsFlow) &&
                Objects.equals(clientAuthFlow, that.clientAuthFlow) &&
                Objects.equals(offlineSessionIdleTimeout, that.offlineSessionIdleTimeout) &&
                Objects.equals(revokeRefreshToken, that.revokeRefreshToken) &&
                Objects.equals(accessTokenLifeImplicit, that.accessTokenLifeImplicit) &&
                Objects.equals(loginWithEmailAllowed, that.loginWithEmailAllowed) &&
                Objects.equals(duplicateEmailsAllowed, that.duplicateEmailsAllowed) &&
                Objects.equals(dockerAuthFlow, that.dockerAuthFlow) &&
                Objects.equals(refreshTokenMaxReuse, that.refreshTokenMaxReuse) &&
                Objects.equals(allowUserManagedAccess, that.allowUserManagedAccess) &&
                Objects.equals(ssoMaxLifespanRememberMe, that.ssoMaxLifespanRememberMe) &&
                Objects.equals(ssoIdleTimeoutRememberMe, that.ssoIdleTimeoutRememberMe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessCodeLifespan, userActionLifespan, accessTokenLifespan, accountTheme, adminTheme, emailTheme, enabled, eventsEnabled, eventsExpiration, loginTheme, name, notBefore, passwordPolicy, registrationAllowed, rememberMe, resetPasswordAllowed, social, sslRequired, ssoIdleTimeout, ssoMaxLifespan, updateProfileOnSocLogin, verifyEmail, loginLifespan, internationalizationEnabled, defaultLocale, regEmailAsUsername, adminEventsEnabled, adminEventsDetailsEnabled, editUsernameAllowed, otpPolicyCounter, otpPolicyWindow, otpPolicyPeriod, otpPolicyDigits, otpPolicyAlg, otpPolicyType, browserFlow, registrationFlow, directGrantFlow, resetCredentialsFlow, clientAuthFlow, offlineSessionIdleTimeout, revokeRefreshToken, accessTokenLifeImplicit, loginWithEmailAllowed, duplicateEmailsAllowed, dockerAuthFlow, refreshTokenMaxReuse, allowUserManagedAccess, ssoMaxLifespanRememberMe, ssoIdleTimeoutRememberMe);
    }
}
