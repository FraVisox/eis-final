package it.unipd.dei.dbdc.serializers.interfaces;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unipd.dei.dbdc.analysis.interfaces.UnitOfSearch;

import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * This interface defines an object capable of serializing a list of {@link UnitOfSearch} into a file
 * The class implementing that interface should provide the serialization logic for a specific file format.
 *
 */
public interface Serializer {
    /**
     * Serializes a list of {@link UnitOfSearch} objects to the specified file.
     *
     * @param objects The list of objects to serialize
     * @param file The file in which the list to be serialized is saved
     * @throws IOException If an error occurs during the file writing operation. It can be thrown if the file is not accessible
     */
    void serialize(List<UnitOfSearch> objects, File file) throws IOException ;

}


