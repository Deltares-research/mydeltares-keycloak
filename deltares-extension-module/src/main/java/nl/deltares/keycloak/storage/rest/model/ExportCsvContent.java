package nl.deltares.keycloak.storage.rest.model;

public interface ExportCsvContent {

    String getId();

    String getName();

    String[] getHeaders();

    boolean hasHeader();

    boolean hasNextRow();

    String[] nextRow();

    int totalExportedCount();

    void setMaxResults(int max_query_results);

    void close();
}
