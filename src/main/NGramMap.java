package main;

import edu.berkeley.eecs.inst.cs61b.ngrams.StaffNGramMap;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NGramMap extends StaffNGramMap {

    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /** CORRECT ORDER: (wordHistoryFilename, yearHistoryFilename) */
    public NGramMap(String wordHistoryFilename, String yearHistoryFilename) {
        super(wordHistoryFilename, yearHistoryFilename);
    }

    public TreeMap<Integer, Double> countHistory(String word, int startYear, int endYear) {
        return super.countHistory(word, startYear, endYear);
    }

    public TreeMap<Integer, Double> countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    public long countInRange(String word, int start, int end) {
        if (start > end) {
            return 0L;
        }
        TreeMap<Integer, Double> ts = countHistory(word, start, end);
        if (ts == null || ts.isEmpty()) {
            return 0L;
        }
        NavigableMap<Integer, Double> sub = ts.subMap(start, true, end, true);
        long sum = 0L;
        for (Map.Entry<Integer, Double> e : sub.entrySet()) {
            Double v = e.getValue();
            if (v != null) {
                sum += v.longValue();
            }
        }
        return sum;
    }
}
