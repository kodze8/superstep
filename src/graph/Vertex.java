package graph;

import communication.Message;
import communication.MessageEmitter;

import java.util.ArrayList;
import java.util.List;


public class Vertex {
    private final VertexID id;
    public final List<Edge> edges;
    private double distance;

    private int incomingEdges = 0;
    private int outgoiongEdges = 0;


    public Vertex(int id){
        this.distance = Integer.MAX_VALUE;
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


//    public void computeBFS(List<Double> data, MessageEmitter emitter) {
//        for (Double distance: data) {
//            if (distance < this.distance) {
//                this.distance = distance;
//
//                for (Edge e : this.edges) {
//                    double newDist = distance + e.getWeight();
//                    emitter.emit(new Message(e.getDst(), newDist));
//                }
//            }
//        }
//    }

//    public void computePageRank(List<Double> data, MessageEmitter emitter, int n){
//        double sum = data.stream().mapToDouble(Double::doubleValue).sum();
//
//        double newRank = (1 - DAMPING) / n + DAMPING * sum;
//
////        System.out.println(this.id.getIntValue()+" sum is "+newRank);
//        if (Math.abs(newRank-this.distance) > EPSILON){
//            for (Edge e : this.edges) {
//                double rankElement =newRank / this.outgoiongEdges;
//                emitter.emit(new Message(e.getDst(), rankElement));
//            }
//
//        }
//        this.distance = newRank;
//    }

    public double getDistance(){
        return this.distance;

    }
    public void setDistance(double distance){
        this.distance = distance;

    }

}
