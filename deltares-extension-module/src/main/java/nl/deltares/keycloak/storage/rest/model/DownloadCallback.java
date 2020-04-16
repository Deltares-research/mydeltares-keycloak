package nl.deltares.keycloak.storage.rest.model;

/**
 * Callback interface to be used for download completion actions.
 */
public interface DownloadCallback {

    void downloadComplete();
}
