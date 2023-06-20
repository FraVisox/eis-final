package it.unipd.dei.dbdc.serializers;

import it.unipd.dei.dbdc.serializers.interfaces.Serializer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static it.unipd.dei.dbdc.serializers.SerializationProperties.readSerializersProperties;

public class SerializersContainer {

    private Map<String, Serializer> serializers;

    public SerializersContainer(String filePropertiesName) throws IOException {

        serializers = readSerializersProperties(filePropertiesName);

    }
    public Serializer getSerializer(String format){
        return serializers.get(format);
    }
    public Set<String> getFormats() {
        return serializers.keySet();
    }

}
