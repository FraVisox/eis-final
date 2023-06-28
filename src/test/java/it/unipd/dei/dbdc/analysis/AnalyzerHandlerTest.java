package it.unipd.dei.dbdc.analysis;

import it.unipd.dei.dbdc.analysis.interfaces.Analyzer;
import it.unipd.dei.dbdc.analysis.interfaces.OutPrinter;
import it.unipd.dei.dbdc.analysis.interfaces.UnitOfSearch;
import it.unipd.dei.dbdc.tools.PathTools;
import it.unipd.dei.dbdc.tools.PathManagerTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
public class AnalyzerHandlerTest {

    public static final String resources_url = PathManagerTest.resources_folder+"analysis/";

    private static int default_stop_words = 0;

    //Throws IOException se non c'è il default properties o se è scorretto o se non riesce ad aprire o scrivere il file di output
    public static String analyze(String filePropertiesName, List<UnitOfSearch> articles, int tot_count, boolean stop_words) throws IOException, IllegalArgumentException {

        Object[] list = AnalyzeProperties.readProperties(filePropertiesName);
        Analyzer analyzer = (Analyzer) list[0];
        OutPrinter printer = (OutPrinter) list[1];
        Set<String> banned = null;
        if (stop_words)
        {
            banned = bannedArray();
        }
        List<OrderedEntryStringInt> max = analyzer.mostPresent(articles, tot_count, banned);

        String outFile = PathTools.getOutFile();
        printer.outFile(max);
        return outFile;
    }

    private static Set<String> bannedArray()
    {
        HashSet<String> banned = null;
        try (InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(PathTools.getBannedWordsFile()))
        {
            if (file == null)
            {
                throw new IOException();
            }
            banned = new HashSet<>(default_stop_words);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                banned.add(scanner.next());
            }
        }
        catch (IOException e) {
            System.out.println("The stop-words won't be used because there was an error in the reading of the file.");
        }
        return banned;
    }

    private static void outFile(ArrayList<OrderedEntryStringInt> max, String outFilePath) throws IOException {
        if (max == null)
        {
            throw new IllegalArgumentException("Vector of most important words is null");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilePath))) {
            for (int i = 0; i <max.size(); i++) {
                OrderedEntryStringInt el = max.get(i);
                writer.write(el.getKey() + " " + el.getValue());
                if (i != max.size()-1) {
                    writer.newLine();
                }
            }
        }
    }
}
