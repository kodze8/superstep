package main;
import algorithms.AlgorithmType;
import algorithms.AlgorithmsRunner;
import graph.Graph;
import graph.VertexID;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Master{
    static int numberOfWorkers = 5;
    Graph graph;
    List<Worker> workers;
    boolean isHalted = false;

    AlgorithmType algorithmType;
    private AlgorithmsRunner runner;


    public Master(Graph graph, AlgorithmType algorithmType){
        this.graph = graph;
        this.algorithmType = algorithmType;
        this.createWorkers();
        this.partition();
    }

    private void createWorkers(){
        this.workers = new ArrayList<>();
        for (int i = 0; i < numberOfWorkers; i++){
            Worker t = new Worker(i, this.graph.getN(), this.algorithmType.getComputationStrategy());
            this.workers.add(t);
            t.start(); 
        }
    }

    private void partition(){
        for (VertexID id : this.graph.getVertexIdSet()){
            int mod = id.getIntValue() % Master.numberOfWorkers;
            Worker currentWorker =  this.workers.get(mod);
            currentWorker.assignVertex(this.graph.getVertex(id));
        }
    }

    private void setState(ControlSignal signal, WorkerState state) {
        CountDownLatch latch = new CountDownLatch(workers.size());
        for (Worker worker : workers) {
            worker.acceptControlMessage(signal, latch);
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void start(){
        switch (this.algorithmType) {
            case PAGERANK ->
                    this.runner = this.algorithmType.createAlgorithmsRunner(this.graph, this.workers, -1);
            case BFS ->
                    this.runner = this.algorithmType.createAlgorithmsRunner(this.graph, this.workers, 0);
        }
        this.runner.start();
    }

    public void run(){
        int superStep = 1;
        Router router = new Router(this.workers);

        while (!this.isHalted){
            System.out.println("\n========== SuperStep " + superStep + " ==========");

            for (Worker worker: this.workers){
                worker.swapMessageQueues();
            }

            this.setState(ControlSignal.START_STEP, WorkerState.READY);
            System.out.println("-> All workers STARTED");

            this.setState(ControlSignal.PROCESS_MESSAGES, WorkerState.FINISHED);
            System.out.println("-> All workers FINISHED");


            boolean hasMessages = router.routeAll();
            this.isHalted = !hasMessages;

            superStep++;
        }
        this.setState(ControlSignal.SHUTDOWN, WorkerState.SHUTDOWN);
    }

    public void printResults(){
        this.runner.print();
    }

}
