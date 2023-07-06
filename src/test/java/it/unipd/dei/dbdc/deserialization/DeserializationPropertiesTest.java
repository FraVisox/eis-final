package it.unipd.dei.dbdc.deserialization;


import it.unipd.dei.dbdc.deserialization.interfaces.Deserializer;
import it.unipd.dei.dbdc.deserialization.src_deserializers.CsvArticleDeserializer;
import it.unipd.dei.dbdc.deserialization.src_deserializers.JsonArticleDeserializer;
import it.unipd.dei.dbdc.deserialization.src_deserializers.XmlArticleDeserializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeserializationPropertiesTest {
    private static final String deserializers_properties = "src/test/resources/DeserializationTest/properties/deserializers.properties";
    private static final String nonExistentFile_properties = "src/test/resources/DeserializationTest/properties/nonExistentFile.properties";

    private static final String false_deserializers_properties = "src/test/resources/DeserializationTest/properties/falseDeserializers.properties";
    private static final String empty_deserializers_properties = "src/test/resources/DeserializationTest/properties/emptyDeserializers.properties";

    @Test
    public void readDeserializersProperties() {


        Deserializer xmlSer = new JsonArticleDeserializer();

        assertDoesNotThrow(() -> {

            //Tests with default properties
            Map<String, Deserializer> defaultDeserializers = DeserializationProperties.readDeserializersProperties(null);
            assertEquals(3, defaultDeserializers.size());
            assertTrue( defaultDeserializers.get("csv") instanceof CsvArticleDeserializer);


            //Tests with a valid serializers.properties
            final Map<String, Deserializer> deserializers;
            deserializers = DeserializationProperties.readDeserializersProperties(deserializers_properties);
            assertEquals(3, deserializers.size());
            assertTrue(deserializers.get("json") instanceof JsonArticleDeserializer);

        });

        assertDoesNotThrow(() -> {

            // test with a non-existing properties file. it works because it takes the default one
            final Map<String, Deserializer> deserializers;
            deserializers = DeserializationProperties.readDeserializersProperties(nonExistentFile_properties);
            assertEquals(3, deserializers.size());
            assertTrue(deserializers.get("xml") instanceof XmlArticleDeserializer);


            // empty file properties.
            final Map<String, Deserializer> deserializers2;
            deserializers2 = DeserializationProperties.readDeserializersProperties(empty_deserializers_properties);
            assertEquals(0, deserializers2.size());

        });


        //Tests with invalid serializers.properties (wrong classes)
        IOException exception1 = assertThrows(IOException.class, () -> DeserializationProperties.readDeserializersProperties(false_deserializers_properties));
        System.out.println(exception1.getMessage());

    }
}