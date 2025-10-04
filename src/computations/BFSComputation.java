package computations;

import messaging.Message;
import messaging.MessageEmitter;
import graph.Edge;
import graph.Vertex;

import java.util.List;

public class BFSComputation implements ComputationStrategy{
    @Override
    public void compute(Vertex vertex, List<Double> data, MessageEmitter emitter, int n) {
        for (Double distance: data) {
            if (distance < vertex.getValue()) {
                vertex.setValue(distance);

                for (Edge e : vertex.edges) {
                    double newDist = distance + e.getWeight();
                    emitter.emit(new Message(e.getDst(), newDist));
                }
            }
        }
    }

}
