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
            String line = content.nextLine();
            writer.append(line);
            writer.newLine();
        }
    }

}
