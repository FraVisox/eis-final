package it.unipd.dei.dbdc.DESERIALIZERS_FILE_PROPERTIES;

import java.io.IOException;
import java.util.List;

interface Deserializer<T> {
    List<T> deserialize(String filePath) throws IOException;
}

interface specificDeserializer<T> extends Deserializer<T>{
    String[] getFields();
    void setFields(String[] fields);
}

