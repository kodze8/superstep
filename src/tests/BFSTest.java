package tests;

import graph.Edge;
import graph.Graph;
import graph.VertexID;

import java.util.*;

public class BFSTest {
    public static Graph getTestGraph_bfs(){
        Object[] res = new Object[2];
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
        return new Graph(15, edges, false, false);
    }
    public static Graph getSmallGraph() {
        List<List<Integer>> edges = new ArrayList<>();
        edges.add(Arrays.asList(0, 1));
        edges.add(Arrays.asList(0, 2));
        edges.add(Arrays.asList(1, 3));
        edges.add(Arrays.asList(2, 4));
        return new Graph(5, edges, false, false);
    }

    public static Graph getMediumGraph() {
        List<List<Integer>> edges = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            edges.add(Arrays.asList(i, (i + 1) % 1000));
            if (i % 10 == 0) edges.add(Arrays.asList(i, (i + 50) % 1000));
        }
        return new Graph(1000, edges, false, false);
    }

    public static Graph getLargeGraph() {
        List<List<Integer>> edges = new ArrayList<>();
        Random rand = new Random(42);
        int n = 10000;
        for (int i = 0; i < n * 4; i++) { // average degree 4
            edges.add(Arrays.asList(rand.nextInt(n), rand.nextInt(n)));
        }
        return new Graph(n, edges, false, false);
    }
    public static void runSimpleBfs(Graph graph, int src_vertex ){
        VertexID src = null;
        for (VertexID id : graph.getVertexIdSet()){
            if (id.getIntValue() == src_vertex){
                src = id;
                break;
            }
        }
        LinkedList<VertexID> queue = new LinkedList<>();
        HashSet<VertexID> seen = new HashSet<>();

        queue.add(src);
        seen.add(src);
        int distance  = 0;
        while(!queue.isEmpty()){
            int cur_size = queue.size();
            for (int i=0; i<cur_size; i++) {
                VertexID current = queue.removeFirst();
                for (Edge edge : graph.getVertex(current).edges) {
                    if (!seen.contains(edge.getDst())) {
                        queue.add(edge.getDst());
                        seen.add(edge.getDst());

                    }

                }
            }
            distance++;
        }
    }
}
