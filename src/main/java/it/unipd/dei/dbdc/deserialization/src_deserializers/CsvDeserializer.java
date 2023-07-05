package it.unipd.dei.dbdc.deserialization.src_deserializers;

import it.unipd.dei.dbdc.analysis.Article;
import it.unipd.dei.dbdc.analysis.interfaces.UnitOfSearch;
import it.unipd.dei.dbdc.deserialization.interfaces.DeserializerWithFields;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CsvDeserializer implements DeserializerWithFields {

    private String[] fields;

    public CsvDeserializer(){

        fields = new String[]{"Identifier", "URL", "Title", "Body", "Date", "Source Set", "Source"};
    }
    public int numberOfFields(){
        return fields.length;
    }
    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] newFields) {
        fields = newFields;
    }


    @Override
    public List<UnitOfSearch> deserialize(File csvFile) throws IOException {

        if(csvFile == null){
            throw new IllegalArgumentException("The CSV file cannot be null");
        }
        if (!csvFile.exists()) {
            throw new IllegalArgumentException("The CSV file does not exist");
        }

        List<UnitOfSearch> articles = new ArrayList<>();
        // Leggo gli header
        String[] header = readHeader(csvFile);

        if(areAllElementsArrayNull(header)){
            return articles;
        }

        try (Reader reader = new FileReader(csvFile)) {

            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(header)
                    .setSkipHeaderRecord(true)
                    .build();

            CSVParser parser = new CSVParser(reader, csvFormat);

            for (CSVRecord record : parser) {
                Article article = parseArticleRecord(record);
                articles.add(article);
            }
            parser.close();
        }
        return articles;
    }

    private Article parseArticleRecord(CSVRecord record){

        Class<Article> myClass = Article.class;
        String[] fieldsValues = new String[fields.length];

        for(int i=0; i < fieldsValues.length; i++){
            if(record.isSet(fields[i])){
                fieldsValues[i] = record.get(fields[i]);
            }
        }

        return new Article(fieldsValues);
    }

    private String[] readHeader(File csvFile) throws IOException {

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        try (Reader reader = new FileReader(csvFile); CSVParser parser = new CSVParser(reader, csvFormat)) {

            CSVRecord headerRecord = null;
            try {
                headerRecord = parser.iterator().next();
            } catch (NoSuchElementException e) {
                throw new IOException("Empty CSV file", e); // TODO nel json non do errore bisogna scegliere questa cosa
            }

            // Salva gli header delle colonne in un array
            String[] header = new String[headerRecord.size()];
            for (int i = 0; i < headerRecord.size(); i++) {
                header[i] = headerRecord.get(i).replace("\uFEFF", "");
            }

            // Imposta a null i valori non presenti nell'array di riferimento
            for (int i = 0; i < header.length; i++) {
                if (!contains(fields, header[i])) {
                    header[i] = null;
                }

            }

            return header;
        }
    }

    private boolean contains(String[] array, String value) {
        for (String element : array) {
            if (element != null && element.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private boolean areAllElementsArrayNull(Object[] array) {

        if (array == null) {
            return true;
        }

        for (Object element : array) {
            if (element != null) {
                return false;
            }
        }
        return true;
    }

}





























