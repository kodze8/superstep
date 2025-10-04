package vertex_computation;

import communication.Message;
import communication.MessageEmitter;
import graph.Edge;
import graph.Vertex;
import graph.VertexID;

import java.util.List;

public class BFSComputation implements ComputationStrategy{
    @Override
    public void compute(Vertex vertex, List<Double> data, MessageEmitter emitter, int n) {
        for (Double distance: data) {
            if (distance < vertex.getDistance()) {
                vertex.setDistance(distance);

                for (Edge e : vertex.edges) {
                    double newDist = distance + e.getWeight();
                    emitter.emit(new Message(e.getDst(), newDist));
                }
            }
        }
    }

}
