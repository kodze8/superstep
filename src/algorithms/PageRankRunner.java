package algorithms;

import communication.Message;
import graph.Graph;
import graph.Vertex;
import graph.VertexID;
import main.Worker;

import java.util.Comparator;
import java.util.List;

public class PageRankRunner implements AlgorithmsRunner{
    Graph graph;
    List<Worker> workers;
    public PageRankRunner(Graph graph, List<Worker> workers){
        this.graph = graph;
        this.workers = workers;
    }
    @Override
    public void start() {
        int n  = this.graph.getN();
        for (Vertex vertex: this.graph.getVertexValueSet()){
            vertex.setDistance(1.0/n);

            int outgoing = vertex.getOutgoiongEdges();

            for (VertexID neighbor: vertex.getNeighbors()){
                double share = vertex.getDistance()/outgoing;
                for (Worker worker: this.workers){
                    if (worker.containsVertex(neighbor)) {
                        worker.addToNextQueue(new Message(neighbor, share));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void print() {
        System.out.println("\nFinal ranks from each Vertex :");
        this.graph.getVertexValueSet().stream()
                .sorted(Comparator.comparingDouble(v ->  v.getDistance()))
                .forEach(v -> System.out.println("Vertex "
                        + v.getId().getIntValue()
                        + " -> rank " + (v.getDistance()!=Integer.MAX_VALUE ? v.getDistance():"INFINITY")));


    }
}
