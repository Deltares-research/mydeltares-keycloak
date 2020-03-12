package nl.deltares.keycloak.storage.jpa.model;

import java.io.File;
import java.io.IOException;

public interface DataRequest {

    enum STATUS {pending, running, terminated, expired, available}

    String getId();

    void dispose();

    void start();

    STATUS getStatus();

    File getDataFile();

    String getErrorMessage();

    String getStatusMessage();
}
