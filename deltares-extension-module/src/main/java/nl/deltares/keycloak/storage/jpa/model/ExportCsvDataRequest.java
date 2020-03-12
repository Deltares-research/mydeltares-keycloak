package nl.deltares.keycloak.storage.jpa.model;

import nl.deltares.keycloak.storage.jpa.Mailing;
import nl.deltares.keycloak.storage.jpa.UserMailing;
import nl.deltares.keycloak.storage.rest.model.ExportCsvContent;
import nl.deltares.keycloak.storage.rest.model.ExportUserMailings;
import nl.deltares.keycloak.storage.rest.model.TextSerializer;
import nl.deltares.keycloak.storage.rest.serializers.ExportCsvSerializer;
import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserProvider;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static nl.deltares.keycloak.storage.jpa.model.DataRequest.STATUS.*;
import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

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

    private File exportFile;
    private String errorMessage;
    private Thread thread;
    private ExportCsvContent content;

    public ExportCsvDataRequest(Mailing mailing, KeycloakSession session, RealmModel realm, Properties properties) throws IOException {
        this.mailing = mailing;
        this.session = session;
        this.realm = realm;
        String max_age = properties.getProperty("max_age");
        this.maxAge = max_age == null ? TimeUnit.HOURS.toMillis(1) : Long.parseLong(max_age);
        String max_results = properties.getProperty("max_query_results");
        this.maxResults = max_age == null ? 500 : Integer.parseInt(max_results);
        this.csvSeparator = properties.getProperty("csv_separator", ";");

        this.exportFile = getExportFile("csv");
        if (exportFile.exists()) {
            BasicFileAttributes attributes = Files.readAttributes(exportFile.toPath(), BasicFileAttributes.class);
            this.creationTime = attributes.creationTime().toMillis();
            status = isExpired() ? expired : available;
        }

    }

    private File getExportDir() throws IOException {
        String property = System.getProperty("jboss.server.temp.dir");
        if (property == null){
            throw new IOException("Missing system property: jboss.server.temp.dir");
        }
        File tempDir = new File(property, "deltares");
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

    }

    @Override
    public void start() {

        if (getStatus() == available) return;

        if (thread != null) throw new IllegalStateException("Thread already started!");

        thread = new Thread(() -> {
            //todo Test if interrupting this thread is enough to stop the export
            status = running;
            try {
                File tempFile = getExportFile("tmp");
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
        }, mailing.getName());

        thread.start();
        //wait for export to get started. quick exports can return values
        try {
            thread.join(10000);
        } catch (InterruptedException e) {
            //
        }
    }

    private ExportCsvContent getExportContent(KeycloakSession session) {
        EntityManager entityManager = getEntityManager(session);
        TypedQuery<UserMailing> query = entityManager.createNamedQuery("allUserMailingsByMailingAndRealm", UserMailing.class);
        query.setParameter("realmId", realm.getId());
        query.setParameter("mailingId", mailing.getId());
        UserProvider userProvider = session.userStorageManager();
        ExportUserMailings content = new ExportUserMailings(userProvider, realm, query, mailing.getName());
        content.setMaxResults(maxResults);
        content.setSeparator(csvSeparator);

        return content;
    }

    private File getExportFile(String extension) throws IOException {
        File exportDir = getExportDir();
        return new File(exportDir, mailing.getName() +  '.' + extension);
    }

    @Override
    public File getDataFile() {
        if (status != available) throw new IllegalStateException("Data not available!");
        return exportFile;
    }

    @Override
    public STATUS getStatus() {
        if (status == available) {
            if (!exportFile.exists()) {
                status = expired;
            } else if (isExpired()) {
                status = expired;
            }
        }
        return status;
    }

    private boolean isExpired(){
        return System.currentTimeMillis() - creationTime > maxAge;
    }

    public String getErrorMessage(){
        if (errorMessage == null && status == terminated){
            return "terminated";
        }
        return errorMessage;
    }

    public String getStatusMessage() {
        int count = 0;
        if (content != null) count = content.totalExportedCount();
        return String.format("Downloading %s " +
                "status: %s, " +
                "processed: %d" , mailing.getName(), status, count);
    }

}
