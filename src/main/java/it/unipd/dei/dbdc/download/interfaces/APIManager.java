package it.unipd.dei.dbdc.download.interfaces;

import it.unipd.dei.dbdc.download.QueryParam;

import java.io.IOException;
import java.util.List;

public interface APIManager {
    String getParams();

    String getName();

    void addParams(List<QueryParam> l) throws IllegalArgumentException;

    void callAPI(String path_folder) throws IllegalArgumentException, IOException;
}
