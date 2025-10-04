package runners;

import messaging.Message;
import graph.Graph;
import graph.VertexID;
import orchestration.Worker;

import java.util.Comparator;
import java.util.List;

public class BFSRunner implements AlgorithmsRunner {
    Graph graph;
    List<Worker> workers;
    int srcVertex;
    public BFSRunner( Graph graph, List<Worker> workers, int srcVertex){
        this.graph = graph;
        this.workers = workers;
        this.srcVertex = srcVertex;
    }

    @Override
    public void start() {
        VertexID src = null;
        for (VertexID id : this.graph.getVertexIdSet()){
            if (id.getIntValue() == srcVertex){
                src = id;
                break;
            }
        }
        if (src ==null){
            System.out.println("Source vertex is not part of the Graph.");
            return;
        }

        for (Worker worker: this.workers){
            if (worker.containsVertex(src)) {
                worker.addToNextQueue(new Message(src, 0));
                break;
            }
        }
    }

    @Override
    public void print() {
        System.out.println("\nFinal distances from source " + srcVertex + ":");
        this.graph.getVertexValueSet().stream()
                .sorted(Comparator.comparingInt(v -> v.getId().getIntValue()))
                .forEach(v -> System.out.println("Vertex "
                        + v.getId().getIntValue()
                        + " -> distance " + (v.getValue()!=Integer.MAX_VALUE ? v.getValue():"INFINITY")));

    }
}
