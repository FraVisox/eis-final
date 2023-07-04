package it.unipd.dei.dbdc.deserialization.src_deserializers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unipd.dei.dbdc.analysis.Article;
import it.unipd.dei.dbdc.deserialization.interfaces.Deserializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unipd.dei.dbdc.analysis.interfaces.UnitOfSearch;
import it.unipd.dei.dbdc.download.src_api_managers.TheGuardianAPI.GuardianAPIManager;

// ricodati che questa funziona solo con xml costruiti da noi ( comunque xml semplici )
public class XmlDeserializer implements Deserializer {

    @Override
    public List<UnitOfSearch> deserialize(File xmlFile) throws IOException {

        if(xmlFile == null){
            throw new IllegalArgumentException("The xmlFile file cannot be null");
        }
        if (!xmlFile.exists()) {
            throw new IllegalArgumentException("The XML file does not exist");
        }

        XmlMapper xmlMapper = new XmlMapper();
        List<Article> articles = xmlMapper.readValue(xmlFile, new TypeReference<List<Article>>() {}); // FIXME: funziona, ma non ha senso
        return new ArrayList<>(articles);


    }

}
