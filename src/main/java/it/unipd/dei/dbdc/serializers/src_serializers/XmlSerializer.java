package it.unipd.dei.dbdc.serializers.src_serializers;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unipd.dei.dbdc.deserialization.interfaces.UnitOfSearch;
import it.unipd.dei.dbdc.serializers.interfaces.Serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Classe che implementa l'interfaccia "Serializer".
 * La classe serializza in un file ".xml" una lista di oggetti "UnitOfSearch" tramite la libreria Jackson.
 * @see UnitOfSearch
 */
public class XmlSerializer implements Serializer {

    /**
     * Serializza una lista di oggetti UnitOfSearch in un file JSON.
     *
     * @param objects   Lista di oggetti da serializzare.
     * @param filePath  Percorso del file JSON di output.
     * @throws IOException Se si verifica un errore durante la scrittura del file JSON.
     */
    @Override
    public void serialize(List<UnitOfSearch> objects, String filePath) throws IOException {

        try {
            // Create an instance of XmlMapper for the serialization
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable indented formatting

            // Transform objects into XML
            String xmlString = xmlMapper.writeValueAsString(objects);

            //System.out.println(xmlString); // da togliere


            // Create a new XML file (if it doesn't already exist) and write the XML content to the file
            File xmlOutput = new File(filePath);
            FileWriter fileWriter = new FileWriter(xmlOutput);
            fileWriter.write(xmlString);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
