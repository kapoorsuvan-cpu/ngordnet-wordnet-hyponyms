package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordNet {
    private final WordGraph graph;
    private final Map<Integer, Set<String>> idToWords;
    private final Map<String, Set<Integer>> wordToIds;

    public WordNet(String synsetsFile, String hyponymsFile) {
        graph = new WordGraph();
        idToWords = new HashMap<>();
        wordToIds = new HashMap<>();
        loadSynsets(synsetsFile);
        loadHyponyms(hyponymsFile);
    }

    private void loadSynsets(String synsetsFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(synsetsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                int id = Integer.parseInt(parts[0]);
                String[] words = parts[1].split(" ");

                Set<String> wordSet = new HashSet<>();
                for (String w : words) {
                    wordSet.add(w);
                    wordToIds.computeIfAbsent(w, k -> new HashSet<>()).add(id);
                }
                idToWords.put(id, wordSet);
                graph.addEdge(id, id);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadHyponyms(String hyponymsFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(hyponymsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int parent = Integer.parseInt(parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    graph.addEdge(parent, Integer.parseInt(parts[i]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Integer> getIdsForWord(String word) {
        return wordToIds.getOrDefault(word, Collections.emptySet());
    }

    public Set<String> getWordsForId(int id) {
        return idToWords.getOrDefault(id, Collections.emptySet());
    }

    public Set<String> getHyponymsForWord(String word) {
        Set<Integer> ids = getIdsForWord(word);
        Set<Integer> descendants = graph.getDescendants(ids);
        Set<String> result = new HashSet<>();
        for (int id : descendants) {
            result.addAll(getWordsForId(id));
        }
        return result;
    }

    /** hyponyms common to all words */
    public Set<String> getHyponymsForWords(List<String> words) {
        if (words == null || words.isEmpty()) return Collections.emptySet();
        Iterator<String> it = words.iterator();
        Set<String> acc = new HashSet<>(getHyponymsForWord(it.next()));
        while (it.hasNext()) {
            acc.retainAll(getHyponymsForWord(it.next()));
            if (acc.isEmpty()) break;
        }
        return acc;
    }

    public WordGraph getGraph() {
        return graph;
    }
}
