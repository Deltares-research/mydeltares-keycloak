package nl.deltares.keycloak.storage.rest.model;

import java.io.BufferedWriter;

public interface TextSerializer<C> extends Serializer<C> {

    void serialize(C content, BufferedWriter writer) throws Exception;
}
