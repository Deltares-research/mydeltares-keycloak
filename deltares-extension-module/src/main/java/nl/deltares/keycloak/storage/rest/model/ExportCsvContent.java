package nl.deltares.keycloak.storage.rest.model;

public interface ExportCsvContent {
    String getHeader();

    boolean hasHeader();

    boolean hasNextLine();

    String nextLine();

    int totalExportedCount();
}
