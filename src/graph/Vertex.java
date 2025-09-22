package graph;

import communication.Message;
import communication.MessageEmitter;

import java.util.ArrayList;
import java.util.List;


public class Vertex {
    private final VertexID id;
    private final List<Edge> edges;
    private int distance;


    public Vertex(int id){
        this.distance = Integer.MAX_VALUE;
        this.id = new VertexID(id);
        this.edges = new ArrayList<>();
    }

    public void setEdge(VertexID dst, int weight){
        this.edges.add(new Edge(this.id, dst, weight));
    }
    public void setEdge(VertexID dst){
        this.setEdge(dst, 1);
    }

    public VertexID getId(){
        return this.id;
    }


    public void compute(int distance, MessageEmitter emitter) {

        if (distance < this.distance) {
            this.distance = distance;

            for (Edge e : this.edges) {
                int newDist = distance + e.getWeight();
                emitter.emit(new Message(e.getDst(), newDist));
            }
        }
    }

    public int getDistance(){
        return this.distance;

    }
}
