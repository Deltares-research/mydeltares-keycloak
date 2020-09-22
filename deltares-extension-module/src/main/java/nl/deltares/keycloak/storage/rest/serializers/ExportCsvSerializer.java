package nl.deltares.keycloak.storage.rest.serializers;

import nl.deltares.keycloak.storage.rest.model.ExportCsvContent;
import nl.deltares.keycloak.storage.rest.model.TextSerializer;

import java.io.BufferedWriter;

public class ExportCsvSerializer implements TextSerializer<ExportCsvContent> {

    private String separator = ";";

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public void serialize(ExportCsvContent content, BufferedWriter writer) throws Exception {

        if (content.hasHeader()) {
            writer.append(String.join(separator, content.getHeaders()));
            writer.newLine();
        }
        while (content.hasNextRow()){
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException("interrupted csv export");
            }
            String[] values = content.nextRow();
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    values[i] = addQuotesIfRequired(values[i]);
                }
                writer.append(String.join(separator, values));
                writer.newLine();
            }
        }
    }

    private String addQuotesIfRequired(String value) {
        if (value.indexOf(separator) > 0){
            return '\"' + value + '\"';
        }
        return value;
    }

}
