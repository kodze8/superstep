package runners;

import graph.Graph;
import orchestration.Worker;
import computations.BFSComputation;
import computations.ComputationStrategy;
import computations.PageRankComputation;

import java.util.List;

public enum AlgorithmType {
    BFS {
        @Override
        public ComputationStrategy getComputationStrategy() {
            return new BFSComputation();
        }

        @Override
        public AlgorithmsRunner createAlgorithmsRunner(Graph graph, List< Worker > workers, int srcVertex) {
            return new BFSRunner( graph,  workers, srcVertex);
        }
    }, PAGERANK {
        @Override
        public ComputationStrategy getComputationStrategy() {
            return new PageRankComputation();
        }

        @Override
        public AlgorithmsRunner createAlgorithmsRunner(Graph graph, List<Worker> workers, int srcVertex) {
            return new PageRankRunner(graph, workers);
        }
    };

    public abstract ComputationStrategy getComputationStrategy();
    public abstract AlgorithmsRunner createAlgorithmsRunner(Graph graph, List< Worker > workers, int srcVertex);
}

