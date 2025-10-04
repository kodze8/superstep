package graph;

import java.util.*;

public class Graph {
    private int n;
    boolean directed;
    private final HashMap<VertexID, Vertex> vertexSet  = new HashMap<>();
    private final List<Edge> edgeSet  = new ArrayList<>();


    public Graph(int n, List<List<Integer>> edges, boolean weighted, boolean directed){
        // edgeList: [src, dst, weight]
        this.n = n;

        for (int i=0; i<n; i++){
            Vertex v = new Vertex(i);
            this.vertexSet.put(v.getId(), v);
        }

        for (List<Integer> e : edges) {

            VertexID node1ID = new VertexID(e.get(0));
            VertexID node2ID = new VertexID(e.get(1));
            int weight = (weighted ? e.get(2) : 1);

            vertexSet.get(node1ID).addOutgoingEdge();
            vertexSet.get(node2ID).addIncomingEdge();

            Edge edge1 = new Edge(node1ID, node2ID, weight);
            vertexSet.get(node1ID).setEdge(edge1);
            edgeSet.add(edge1);

            if (!directed) {
                Edge edge2 = new Edge(node2ID, node1ID, weight);
                vertexSet.get(node2ID).setEdge(edge2);
                edgeSet.add(edge2);
            }
        }
    }

    public Vertex getVertex(VertexID id){
        return this.vertexSet.get(id);
    }
    public Set<VertexID> getVertexIdSet(){
        return this.vertexSet.keySet();
    }
    public Set<Vertex> getVertexValueSet() {
        return new HashSet<>(this.vertexSet.values());
    }

    public int getN(){return this.n;}
}
