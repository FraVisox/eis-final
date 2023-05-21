package it.unipd.dei.dbdc;
import it.unipd.dei.dbdc.DownloadAPI.*;

import java.util.ArrayList;
import java.util.Scanner;

// L'unica classe che ha il main
public class UserInterface {
    public static void main(String[] args) {
        // Crea il caller tramite un Factory che crea tutte le istanze delle APICaller.
        APIFactory factory = APIFactory.getInstance();

        // Quale API vuoi chiamare? Dicendogli quali sono quelle disponibili
        Scanner sc = new Scanner(System.in);

        APICaller caller = null;
        while (true)
        {
            System.out.println("Inserire il nome della API che si vuole avere:\n" + factory.getAPIs());
            String name = sc.next();

            System.out.println("Inserire i parametri per la query, uno per ogni riga (inserire quit per terminare):\n" + factory.getAPIPossibleParams(name));
            ArrayList<QueryParam> queries = new ArrayList<>();
            while (sc.hasNextLine()) {
                String k = sc.next();
                if (k.equalsIgnoreCase("quit")) {
                    break;
                }
                String v = sc.next();
                queries.add(new QueryParam(k, v));
            }

            try {
                caller = factory.getAPICaller(name, queries);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Nome o parametri forniti errati, riprovare");
            }
        }
        // Passa il controllo al DownloadHandler, passandogli anche un'istanza della API da chiamare.
        DownloadHandler dw = new DownloadHandler(caller);
        String path_folder;
        try
        {
            path_folder = dw.download();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("Errore inatteso");
        }

        // Passa il controllo al SerializationHandler
    }
}
