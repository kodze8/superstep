package graph;

import java.util.*;

public class Graph {
    int n;
    private final HashMap<VertexID, Vertex> vertexSet;
    private final List<Edge> edgeSet;

    public Graph(int n, List<List<Integer>> edges, boolean weighted){
        this.n = n;
        this.vertexSet = new HashMap<>();
        this.edgeSet = new ArrayList<>();


        for (int i=0; i<n; i++){
            Vertex v = new Vertex(i);
            this.vertexSet.put(v.getId(), v);
        }

        for (List<Integer> e : edges) {
            Edge edge;
            VertexID node1ID = new VertexID(e.get(0));
            VertexID node2ID = new VertexID(e.get(1));
            if (weighted) {
                edge = new Edge(node1ID, node2ID, e.get(2));
                vertexSet.get(node1ID).setEdge(node2ID, e.get(2));
                vertexSet.get(node2ID).setEdge(node1ID, e.get(2));
            }else {
                edge = new Edge(node1ID, node2ID);
                vertexSet.get(node1ID).setEdge(node2ID);
                vertexSet.get(node2ID).setEdge(node1ID);

            }
            this.edgeSet.add(edge);
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
}
