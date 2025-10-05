import orchestration.Master;
import runners.AlgorithmType;
import graph.Graph;
import tests.BFSTest;
import tests.PageRankTest;


public class main {
    private static void runComparison(String label, Graph graph,  AlgorithmType algo) {
        System.out.println("===== " + label + " Graph =====");

        long start = System.nanoTime();
        BFSTest.runSimpleBfs(graph, 0);
        long end = System.nanoTime();
        System.out.printf("Simple BFS: %.3f ms%n", (end - start) / 1e6);

        start = System.nanoTime();
        Master master = new Master(graph, algo, 0);
        master.start();
        master.run();
        end = System.nanoTime();
        System.out.printf("Pregel BFS: %.3f ms%n%n", (end - start) / 1e6);
    }

    private static void runComparisonPageRank(Graph graph,  AlgorithmType algo) {
        System.out.println("===== Test on PageRank  =====");

        double start = System.nanoTime();
        Master master = new Master(graph, algo);
        master.start();
        master.run();
        double end = System.nanoTime();
        master.printResults();
        System.out.printf("Pregel PageRank: %.3f ms%n%n", (end - start) / 1e6);
    }

    public static void main(String[] args) {
        runComparison("Small", BFSTest.getSmallGraph(), AlgorithmType.BFS);
        runComparison("Medium", BFSTest.getMediumGraph(), AlgorithmType.BFS);
        runComparison("Large", BFSTest.getLargeGraph(), AlgorithmType.BFS);

        runComparisonPageRank(PageRankTest.getPageRankGraph(), AlgorithmType.PAGERANK);

    }
}
