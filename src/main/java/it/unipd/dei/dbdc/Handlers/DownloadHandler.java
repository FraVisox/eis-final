package it.unipd.dei.dbdc.Handlers;

import it.unipd.dei.dbdc.ConsoleTextColors;
import it.unipd.dei.dbdc.DownloadAPI.APIProperties;
import it.unipd.dei.dbdc.Interfaces.DownloadAPI.APIManager;
import it.unipd.dei.dbdc.DownloadAPI.InteractiveSelectAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class  DownloadHandler {

    public static String download(String folder_path, String download_props, String api_props) throws IOException {

        APIManager manager = null;
        if (api_props != null) {
            try {
                manager = APIProperties.readAPIProperties(api_props, download_props);
            }
            catch (IOException | IllegalArgumentException e)
            {
                ConsoleTextColors.printlnInfo("Selecting the API interactively...");
            }
        }

        boolean finished = false;
        String file_path = "";

        while(!finished) {

            if (manager == null) {
                manager = selectInteractive(download_props);
            }

            ConsoleTextColors.printlnInfo("API selected correctly...");

            // Cerca di chiamare la API
            try {
                ConsoleTextColors.printlnProcess("Calling the API...");
                // Il nuovo folder
                String new_path_folder = folder_path + manager.getClass().toString();

                // Elimina il folder, se era gia' presente.
                if (!deleteFilesInDir(new File(new_path_folder))) {
                    // Se non era presente, lo crea
                    Files.createDirectories(Paths.get(new_path_folder));
                }
                long start = System.currentTimeMillis();
                manager.callAPI(folder_path);
                long end = System.currentTimeMillis();
                System.out.println(ConsoleTextColors.YELLOW + "Per download: "+(end-start)+ConsoleTextColors.RESET);

                finished = true;
            }
            catch (IOException | IllegalArgumentException e) {
                ConsoleTextColors.printlnError("Errore nella chiamata all'API");
                e.printStackTrace();
                // To ask another time for the API interactively
                manager = null;
            }
        }
        ConsoleTextColors.printlnProcess( "You can find the downloaded files in the format in which they were download in "+file_path);
        return file_path;
    }

    private static APIManager selectInteractive(String download_props) throws IOException
    {
        try(Scanner in = new Scanner(System.in)) {
            InteractiveSelectAPI interact = new InteractiveSelectAPI(download_props, in);
            while (true) {

                String api_name = interact.askAPIName();

                APIManager manager = interact.askParams(api_name);
                if (manager != null) {
                    in.close();
                    return manager;
                }
            }
        }
    }

    // TODO: giusto?
    private static boolean deleteFilesInDir(File dir)
    {
        File[] contents = dir.listFiles();
        if (contents == null) {
            return false;
        }
        for (File f : contents) {
            deleteDir(f);
        }
        return true;
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

}
