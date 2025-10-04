package graph;

import java.util.ArrayList;
import java.util.List;


public class Vertex {
    private final VertexID id;
    public final List<Edge> edges;
    private double value;
    private int incomingEdges = 0;
    private int outgoiongEdges = 0;


    public Vertex(int id){
        this.value = Integer.MAX_VALUE;
        this.id = new VertexID(id);
        this.edges = new ArrayList<>();
    }

    public void setEdge(Edge edge){
        this.edges.add(edge);
    }
    public VertexID getId(){
        return this.id;
    }
    public void addIncomingEdge(){
        this.incomingEdges++;
    }
    public void addOutgoingEdge(){
        this.outgoiongEdges++;
    }
    public int getOutgoiongEdges(){
        return this.outgoiongEdges;
    }

    public List<VertexID> getNeighbors(){
        List<VertexID> neighbors = new ArrayList<>();
        for (Edge edge: this.edges){
            neighbors.add(edge.getDst());
        }
        return neighbors;
    }
    public double getValue(){
        return this.value;
    }
    public void setValue(double value){
        this.value = value;
    }
}
