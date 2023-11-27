package nl.deltares.keycloak.forms.common.model;

import org.keycloak.models.PasswordPolicy;

public class PasswordPolicyBean {
    
    private final int minLength;
    private final int minDigits;
    private final int minUpperCase;
    private final int minLowerCase;
    private final int minSpecialChars;

    public PasswordPolicyBean(PasswordPolicy passwordPolicy) {

        final Integer length = passwordPolicy.getPolicyConfig("length");
        minLength = length != null? length : 0;
        final Integer digit = passwordPolicy.getPolicyConfig("digits");
        minDigits = digit != null ? digit : 0;
        final Integer upperCase = passwordPolicy.getPolicyConfig("upperCase");
        minUpperCase = upperCase != null ? upperCase : 0;
        final Integer lowerCase = passwordPolicy.getPolicyConfig("lowerCase");
        minLowerCase = lowerCase != null ? lowerCase : 0;
        final Integer specialChars = passwordPolicy.getPolicyConfig("specialChars");
        minSpecialChars = specialChars != null ? specialChars : 0;

    }

    public int getMinLength() {
        return minLength;
    }

    public int getMinDigits() {
        return minDigits;
    }

    public int getMinUpperCase() {
        return minUpperCase;
    }

    public int getMinLowerCase() {
        return minLowerCase;
    }

    public int getMinSpecialChars() {
        return minSpecialChars;
    }
}
