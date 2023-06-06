package it.unipd.dei.dbdc.Search_terms;

import it.unipd.dei.dbdc.Deserialization.Deserializers.Article;

import java.util.*;
import java.util.concurrent.Semaphore;

public class MapArraySplitAnalyzerParallel implements Analyzer<Article> {

    @Override
    public ArrayList<MapEntrySI> mostPresent(List<Article> articles, int tot_words, HashMap<String, Integer> banned)
    {
        TreeMap<String, Integer> global_map = new TreeMap<>();
        Semaphore mutex = new Semaphore(1);
        AnalyzeArticleThread[] analyzers = new AnalyzeArticleThread[articles.size()];
        for (int i = 0; i < articles.size(); i++)
        {
            analyzers[i] = new AnalyzeArticleThread(articles.get(i), global_map, mutex);
            analyzers[i].start();
        }
        for (AnalyzeArticleThread a:
                analyzers) {
            try {
                a.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        ArrayList<MapEntrySI> max = new ArrayList<MapEntrySI>(tot_words);

        for (Map.Entry<String, Integer> el : global_map.entrySet()) {
            addOrdered(max, el, banned, tot_words);
        }
        return max;
    }

    private void addOrdered(ArrayList<MapEntrySI> vec, Map.Entry<String, Integer> entry, HashMap<String, Integer> bannedWords, int tot_words) {
        if (bannedWords.get(entry.getKey()) != null)
        {
            return;
        }
        int vector_size = vec.size();

        // TODO: gestisci meglio queste entry
        MapEntrySI el = new MapEntrySI(entry.getKey(), entry.getValue());

        // Devo aggiungerlo per forza, si tratta solo di capire in che posizione
        if (vector_size < tot_words) {
            int i = 1;

            // Finche' ci sono elementi e sono maggiori
            while (i < vector_size && vec.get(i - 1).isMajorThan(el)) {
                i++;
            }

            // Se sono arrivato alla fine del vettore
            if (i >= vector_size) {
                vec.add(el);
                return;
            }

            // Altrimenti rimpiazzo uno alla volta, con InsertionSort
            MapEntrySI old = vec.get(i - 1);
            vec.set(i - 1, el);
            i++;
            while (i < vector_size) {
                MapEntrySI new_old = vec.get(i);
                vec.set(i - 1, old);
                old = new_old;
                i++;
            }
            vec.add(old);

        }
        else // Altrimenti non e' detto che io la debba aggiungere
        {
            int i = tot_words-1;
            while (i >= 0 && el.isMajorThan(vec.get(i))) {
                if (i == tot_words-1) {
                    i--;
                    continue;
                }
                vec.set(i + 1, vec.get(i));
                i--;
            }
            if (i != tot_words-1) {
                vec.set(i + 1, el);
            }
        }
    }
}
