package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final WordNet wn;
    private final NGramMap ngm;

    public HyponymsHandler(WordNet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        if (words == null || words.isEmpty()) {
            return "[]";
        }

        int k = q.k();

        // k == 0 → old behavior
        if (k == 0) {
            Set<String> res = wn.getHyponymsForWords(words);
            List<String> out = new ArrayList<>(res);
            Collections.sort(out);
            return "[" + String.join(", ", out) + "]";
        }

        // k > 0 → score by NGram counts
        int start = q.startYear();
        int end = q.endYear();

        Set<String> hyps = wn.getHyponymsForWords(words);
        Map<String, Long> freq = new HashMap<>();

        for (String w : hyps) {
            long sum = ngm.countInRange(w, start, end);
            if (sum > 0) {
                freq.put(w, sum);
            }
        }

        // sort by count desc, then alphabetically
        List<String> sorted = new ArrayList<>(freq.keySet());
        sorted.sort((a, b) -> {
            long diff = freq.get(b) - freq.get(a);
            if (diff != 0) {
                return (diff > 0) ? 1 : -1;
            }

            return a.compareTo(b);
        });

        // take top k, then alphabetical
        List<String> top = sorted.subList(0, Math.min(k, sorted.size()));
        Collections.sort(top);
        return "[" + String.join(", ", top) + "]";
    }
}
