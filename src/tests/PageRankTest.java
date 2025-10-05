package tests;

import graph.Graph;

import java.util.*;

public class PageRankTest {




    public static Graph getPageRankGraph() {
        List<List<Integer>> edges = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            edges.add(Arrays.asList(i, (i + 1) % 1000));
            if (i % 10 == 0) edges.add(Arrays.asList(i, (i + 50) % 1000));
        }
        return new Graph(1000, edges, false, true);
    }
}
