package vertex_computation;

import communication.MessageEmitter;
import graph.Vertex;

import java.util.List;

public interface ComputationStrategy {

    // default because some parameters are optional per algorithm
    default void compute(Vertex vertex, List<Double> msgs, MessageEmitter emitter, int n) {
    }

}
