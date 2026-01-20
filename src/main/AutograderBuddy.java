package main;

import browser.NgordnetQueryHandler;

public class AutograderBuddy {
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordHistoryFile, String yearHistoryFile,
            String synsetFile, String hyponymFile) {

        // CORRECT: word first, then year
        NGramMap ngm = new NGramMap(wordHistoryFile, yearHistoryFile);
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        return new HyponymsHandler(wn, ngm);
    }
}



