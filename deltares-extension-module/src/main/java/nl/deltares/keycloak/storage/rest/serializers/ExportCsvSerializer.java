package nl.deltares.keycloak.storage.rest.serializers;

import nl.deltares.keycloak.storage.rest.model.ExportCsvContent;
import nl.deltares.keycloak.storage.rest.model.TextSerializer;

import java.io.BufferedWriter;

public class ExportCsvSerializer implements TextSerializer<ExportCsvContent> {

    @Override
    public void serialize(ExportCsvContent content, BufferedWriter writer) throws Exception {

        if (content.hasHeader()) {
            writer.append(content.getHeader());
            writer.newLine();
        }
        while (content.hasNextLine()){
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("interrupted csv export");
            }
            String line = content.nextLine();
            if (line != null) {
                writer.append(line);
                writer.newLine();
            }
        }
    }

}
