package it.unipd.dei.dbdc.DownloadAPI;

import java.nio.file.Path;
import java.util.List;

public interface APIAdapter {
    // Questi sono quelli che interagiscono con le librerie ed effettuano le chiamate
    Path sendRequest(String url, String paramString);
}
