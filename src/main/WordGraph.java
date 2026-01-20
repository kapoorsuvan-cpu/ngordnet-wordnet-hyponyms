package main;

import java.util.*;

public class WordGraph {
    private final Map<Integer, Set<Integer>> adj = new HashMap<>();

    public void addEdge(int parent, int child) {
        adj.putIfAbsent(parent, new HashSet<>());
        adj.putIfAbsent(child, new HashSet<>());
        adj.get(parent).add(child);
    }

    public Set<Integer> getDescendants(int start) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            int current = stack.pop();
            for (int neighbor : adj.getOrDefault(current, Collections.emptySet())) {
                if (visited.add(neighbor)) stack.push(neighbor);
            }
        }
        return visited;
    }

    public Set<Integer> getDescendants(Collection<Integer> starts) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int s : starts) {
            if (visited.add(s)) stack.push(s);
        }

        while (!stack.isEmpty()) {
            int current = stack.pop();
            for (int neighbor : adj.getOrDefault(current, Collections.emptySet())) {
                if (visited.add(neighbor)) stack.push(neighbor);
            }
        }
        return visited;
    }
}
