package nl.deltares.keycloak.storage.jpa.model;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.rest.model.ExportCsvContent;
import nl.deltares.keycloak.storage.rest.model.ExportUserMailings;
import nl.deltares.keycloak.storage.rest.model.TextSerializer;
import nl.deltares.keycloak.storage.rest.serializers.ExportCsvSerializer;
import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static nl.deltares.keycloak.storage.jpa.model.DataRequest.STATUS.*;

public class ExportCsvDataRequest implements DataRequest {

    private static final Logger logger = Logger.getLogger(ExportCsvDataRequest.class);

    private DataRequest.STATUS status = pending;
    private long creationTime;

    private final Mailing mailing;
    private final KeycloakSession session;
    private final RealmModel realm;

    private final long maxAge;
    private final int maxResults;
    private final String csvSeparator;
    private final String csvPrefix;

    private File exportFile;
    private File tempDir;

    private String errorMessage;
    private Thread thread;
    private ExportCsvContent content;
    private DataRequestManager manager;

    public ExportCsvDataRequest(Mailing mailing, KeycloakSession session, RealmModel realm, Properties properties) throws IOException {
        this.mailing = mailing;
        this.session = session;
        this.realm = realm;
        String max_age = properties.getProperty("max_age_hours", "1");
        this.maxAge = TimeUnit.HOURS.toMillis(Integer.parseInt(max_age));
        String max_results = properties.getProperty("max_query_results" ,"500");
        this.maxResults = Integer.parseInt(max_results);
        this.csvSeparator = properties.getProperty("csv_separator", ";");
        this.csvPrefix = System.getProperty("csv_prefix");
        this.exportFile = getExportFile(csvPrefix, "csv");
        if (exportFile.exists()) {
            BasicFileAttributes attributes = Files.readAttributes(exportFile.toPath(), BasicFileAttributes.class);
            this.creationTime = attributes.creationTime().toMillis();
            if (!isExpired()) status = available;
        }

    }

    private File getExportDir() throws IOException {
        if (tempDir != null) return tempDir;
        String property = System.getProperty("jboss.server.temp.dir");
        if (property == null){
            throw new IOException("Missing system property: jboss.server.temp.dir");
        }
        tempDir = new File(property, "deltares");
        if (!tempDir.exists()) Files.createDirectory(tempDir.toPath());
        return tempDir;
    }

    @Override
    public String getId() {
        return mailing.getId();
    }

    @Override
    public void dispose() {
        status = terminated;
        if (thread != null){
            thread.interrupt();
            try {
                thread.join(5000);
            } catch (InterruptedException e) {
                //
            }
        }

        if (exportFile != null && exportFile.exists()) {
            try {
                Files.deleteIfExists(exportFile.toPath());
            } catch (IOException e) {
                logger.errorf("Cannot delete file %s: %s", exportFile.getAbsolutePath(), e.getMessage());
            }
        }
        if (manager != null) manager.fireStateChanged(this);
    }

    @Override
    public void start() {

        if (getStatus() == available) return;

        if (thread != null) throw new IllegalStateException("Thread already started!");

        status = running;
        if (manager != null) manager.fireStateChanged(this);
        thread = new Thread(() -> {

            try {
                File tempFile = getExportFile(csvPrefix, "tmp");
                if (tempFile.exists()) Files.deleteIfExists(tempFile.toPath());

                //Create local session because the servlet session will close after call to endpoint is completed
                KeycloakSession localSession = session.getKeycloakSessionFactory().create();
                TextSerializer<ExportCsvContent> serializer = new ExportCsvSerializer();
                content = getExportContent(localSession);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))){
                    serializer.serialize(content, writer);
                    writer.flush();
                    status = available;
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    logger.warn("Error serializing csv content: %s", e);
                    status = terminated;
                } finally {
                    localSession.close();
                }
                if (status == available) {
                    if (exportFile.exists()) Files.deleteIfExists(exportFile.toPath());
                    Files.move(tempFile.toPath(), exportFile.toPath());
                    creationTime = System.currentTimeMillis();
                }
            } catch (IOException e) {
                errorMessage = e.getMessage();
                status = terminated;
            }
            if (manager != null) manager.fireStateChanged(this);

        }, mailing.getName());

        thread.start();
        //wait for export to get started. quick exports can return values
        try {
            thread.join(5000);
        } catch (InterruptedException e) {
            //
        }
    }

    private ExportCsvContent getExportContent(KeycloakSession session) {
        ExportUserMailings content = new ExportUserMailings(realm, session, mailing);
        content.setMaxResults(maxResults);
        content.setSeparator(csvSeparator);
        return content;
    }

    private File getExportFile(String prefix, String extension) throws IOException {
        if (prefix == null) {
            return new File(getExportDir(), mailing.getName() + '.' + extension);
        } else {
            return new File(getExportDir(), prefix +  '_' +  mailing.getName() + '.' + extension);
        }
    }

    @Override
    public File getDataFile() {
        if (status != available) throw new IllegalStateException("Data not available! Check if state is 'available'!");
        return exportFile;
    }

    @Override
    public synchronized STATUS getStatus() {
        if (status == available && isExpired()) {
            status = expired;
        }
        return status;
    }

    private boolean isExpired(){
        return System.currentTimeMillis() - creationTime > maxAge;
    }

    @Override
    public String getErrorMessage(){
        if (errorMessage == null && status == terminated){
            return "terminated";
        }
        return errorMessage;
    }

    @Override
    public String getStatusMessage() {
        int count = 0;
        if (content != null) count = content.totalExportedCount();
        return String.format("Downloading %s " +
                "status: %s, " +
                "processed: %d" , mailing.getName(), status, count);
    }

    @Override
    public void setDataRequestManager(DataRequestManager manager) {
        this.manager = manager;
    }
}
