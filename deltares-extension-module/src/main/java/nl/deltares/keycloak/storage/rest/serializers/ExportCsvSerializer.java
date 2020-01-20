package nl.deltares.keycloak.storage.rest.serializers;

import nl.deltares.keycloak.storage.rest.model.ExportCsvContent;
import nl.deltares.keycloak.storage.rest.model.TextSerializer;
import org.jboss.logging.Logger;

import java.io.BufferedWriter;
import java.io.IOException;

public class ExportCsvSerializer implements TextSerializer<ExportCsvContent> {

    private static final Logger logger = Logger.getLogger(ExportCsvSerializer.class);

    @Override
    public void serialize(ExportCsvContent content, BufferedWriter writer) throws Exception {

        if (content.hasHeader()) {
            writer.append(content.getHeader());
            writer.newLine();
        }
        while (content.hasNextLine()){
            try {
                String line = content.nextLine();
                writer.append(line);
                writer.newLine();
            } catch (IOException e) {
                logger.warn("Error writing line: %s", e);
            }
        }
    }

}
