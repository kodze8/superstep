package vertex_computation;

import communication.Message;
import communication.MessageEmitter;
import graph.Edge;
import graph.Vertex;

import java.util.List;

public class PageRankComputation implements ComputationStrategy{
    static final double EPSILON = 0.00001;
    private static final double DAMPING = 0.85;
    @Override
    public void compute(Vertex vertex, List<Double> data, MessageEmitter emitter, int n) {
        double sum = data.stream().mapToDouble(Double::doubleValue).sum();
        double newRank = (1 - DAMPING) / n + DAMPING * sum;
        if (Math.abs(newRank-vertex.getDistance()) > EPSILON){
            for (Edge e : vertex.edges) {
                double rankElement = newRank / vertex.getOutgoiongEdges();
                emitter.emit(new Message(e.getDst(), rankElement));
            }
        }
        vertex.setDistance(newRank);
    }

}
