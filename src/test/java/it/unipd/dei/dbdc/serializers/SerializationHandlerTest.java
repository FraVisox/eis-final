package it.unipd.dei.dbdc.serializers;

import it.unipd.dei.dbdc.analysis.Article;
import it.unipd.dei.dbdc.analysis.interfaces.UnitOfSearch;
import it.unipd.dei.dbdc.deserialization.DeserializationHandler;
import it.unipd.dei.dbdc.deserialization.DeserializersContainer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class SerializationHandlerTest {
    private static final String serializers_properties = "serializers.properties";
    private static List<UnitOfSearch> articlesToSerialize() {
        List<UnitOfSearch> articles = new ArrayList<>();
        articles.add(new Article("ID 1", "URL 1", "Title 1", "Body 1", "Date 1", "SourceSet 1","Source 1"));
        articles.add(new Article("ID 1", "URL 1", "Title 1", "Body 1", "Date 1","SourceSet 1","Source 1"));
        articles.add(new Article("ID 1", "URL 1", "Title 1", "Body 1", "Date 1","SourceSet 1","Source 1"));

        return articles;
    }
    @Test
    public void serializeObjects() {
        try {

            SerializationHandler handler = new SerializationHandler(serializers_properties);

            File serializeFile = new File("src/test/resources/SerializationTest/handlerTest/Articles1.xml");
            handler.serializeObjects(articlesToSerialize(), serializeFile);

            File serializeFile2 = new File("src/test/resources/SerializationTest/handlerTest/Articles2.xml");
            handler.serializeObjects(null, serializeFile2);

            File serializeFile3 = new File("src/test/resources/SerializationTest/handlerTest/Articles2.html"); // TODO una volta eliminati csv e json provare con csv ad esempio ( da elimanre anceh nel file properties)
            IOException exception1 = assertThrows(IOException.class, () -> handler.serializeObjects(articlesToSerialize(), serializeFile3));
            System.out.println(exception1.getMessage());

            IOException exception2 = assertThrows(IOException.class, () -> handler.serializeObjects(null, null));
            System.out.println(exception2.getMessage());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}