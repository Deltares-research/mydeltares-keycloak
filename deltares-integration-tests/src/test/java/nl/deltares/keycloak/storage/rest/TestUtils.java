package nl.deltares.keycloak.storage.rest;


import org.keycloak.representations.idm.UserRepresentation;

public class TestUtils {

    public static UserRepresentation newUserRepresentation(String email){

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(email);
        userRepresentation.setUsername(email.substring(0, email.indexOf('@')));
        userRepresentation.setFirstName("Test");
        userRepresentation.setLastName("Test");
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }
}
