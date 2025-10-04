package main;
import algorithms.AlgorithmType;
import graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) {
        List<List<Integer>> edges = new ArrayList<>();
        edges.add(Arrays.asList(0, 1));
        edges.add(Arrays.asList(0, 2));
        edges.add(Arrays.asList(1, 2));
        edges.add(Arrays.asList(1, 3));
        edges.add(Arrays.asList(1, 4));
        edges.add(Arrays.asList(2, 5));
        edges.add(Arrays.asList(2, 6));
        edges.add(Arrays.asList(3, 7));
        edges.add(Arrays.asList(4, 8));
        edges.add(Arrays.asList(5, 9));
        edges.add(Arrays.asList(6, 10));
        edges.add(Arrays.asList(7, 11));
        edges.add(Arrays.asList(8, 12));
        edges.add(Arrays.asList(9, 13));
        edges.add(Arrays.asList(10, 14));
        edges.add(Arrays.asList(11, 12));

        AlgorithmType algo =  AlgorithmType.BFS;

        Graph graph =  new Graph(15, edges, false, true);
        Master master = new Master(graph, algo);

        master.start();
        master.run();
        master.printResults();


    }
}
