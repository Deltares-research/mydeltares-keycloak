package nl.deltares.keycloak.storage.rest.model;

import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.jpa.entities.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static nl.deltares.keycloak.storage.rest.ResourceUtils.getEntityManager;

/**
 * This task reads a file containing user e-mails and checks these against existing Keycloak users.
 * All e-mails that do not match a keycloak user are returned in a CSV.
 */
public class ExtractNonKeycloakUsers implements ExportCsvContent {

    private static final Logger logger = Logger.getLogger(ExtractNonKeycloakUsers.class);
    private final String[] headers = new String[]{"emails not in keycloak"};
    private final String[] values;
    private final RealmModel realm;
    private final KeycloakSession session;
    private TypedQuery<UserEntity> query;

    private final File input;
    private InputStream inputStream;
    private BufferedReader reader;
    private String separator = null;
    private boolean checkSeparator = true;
    private int emailColumn = -1;

    private long processedSize = 0;
    private long fileSize = 0;
    private KeycloakSession localSession;


    public ExtractNonKeycloakUsers(RealmModel realmModel, KeycloakSession session, File checkEmailsFile) {
        this.values = new String[headers.length];
        this.realm = realmModel;
        this.session = session;
        this.input = checkEmailsFile;
    }

    public void setMaxResults(int maxResults) {
    }

    @Override
    public String[] getHeaders() {
        return headers;
    }

    @Override
    public boolean hasHeader() {
        return true;
    }

    private void initialize() {

        localSession = session.getKeycloakSessionFactory().create();
        EntityManager entityManager = getEntityManager(localSession);
        query = entityManager.createQuery("SELECT u " +
                "FROM UserEntity AS u " +
                " WHERE u.realmId=:realmId AND u.email=:email", UserEntity.class);
        query.setParameter("realmId", realm.getId());
        query.setMaxResults(1);

        if (input == null || !input.exists()) {
            reader = null;
            return;
        }

        try {
            fileSize = Files.size(input.toPath());
        } catch (IOException e) {
            //
        }
        try {
            inputStream = new FileInputStream(input);
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            logger.warn(String.format("Error reading input file %s : %s", input.getName(), e.getMessage()));
        }
    }

    @Override
    public String getId() {
        return "extractNonExistingUsers";
    }

    @Override
    public String getName() {
        return "extractNonExistingUsers";
    }

    @Override
    public void close() {
        logger.info("Finished checking for non-existing users: " + getName());

        if (localSession != null) {
            localSession.close();
            localSession = null;
        }
        if (reader != null) {
            try {
                reader.close();
                inputStream.close();
            } catch (IOException e) {
                logger.warn("Error closing reader: " + e.getMessage());
            }
        }
        if (input != null){
            try {
                Files.deleteIfExists(input.toPath());
            } catch (IOException e) {
                logger.warn("Error removing input file: " + e.getMessage());
            }
        }

    }

    @Override
    public boolean hasNextRow() {

        if (query == null) {
            initialize();
            logger.info("Start checking for non-existing users: " + getName());
        }
        if (reader == null) {
            return false;
        }

        try {
            values[0] = findNextNonExistingEmail();
        } catch (IOException e) {
            logger.warn("Error reading input file: " + e.getMessage());
        }

        return values[0] != null;

    }

    private String findNextNonExistingEmail() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            processedSize += line.length();
            if (checkSeparator) separator = getSeparator(line);
            String email = getEmail(line);
            try {
                query.setParameter("email", email);
                final List<UserEntity> resultList = query.getResultList();
                if (resultList.size() == 0) return email;
            } catch (Exception e){
                logger.warnf("Error finding user for email %s : %s", email, e.getMessage());
            }
        }
        return null;
    }

    private String getSeparator(String line) {
        checkSeparator = false;
        final String regexpr = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        if (line.split("," + regexpr).length > 1) return "," + regexpr;
        else if (line.split(";" + regexpr).length > 1) return ";" + regexpr;
        else return null;
    }

    private int getEmailColumn(String[] columns) {

        for (int i = 0; i < columns.length; i++) {
            if (columns[i].contains("@")) return i;
        }
        return 0;
    }

    private String getEmail(String line) {
        if (separator == null) return line;
        final String[] values = line.split(separator);
        if (emailColumn == -1) emailColumn = getEmailColumn(values);
        return values[emailColumn];
    }

    @Override
    public String[] nextRow() {
        if (values[0] == null) {
            throw new IllegalStateException("First call 'hasNextRow'!");
        }
        return values;
    }

    @Override
    public int percentProcessed() {
        return (int) (processedSize / fileSize);
    }
}
