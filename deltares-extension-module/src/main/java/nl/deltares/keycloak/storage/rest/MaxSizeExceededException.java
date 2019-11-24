package nl.deltares.keycloak.storage.rest;

public class MaxSizeExceededException extends Exception {
    public MaxSizeExceededException(String message) {
        super(message);
    }
}
