package main;

import communication.Message;
import graph.Graph;
import graph.VertexID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Master{
    static int numberOfWorkers = 5;
    Graph graph;
    List<Worker> workers;
    boolean isHalted = false;


    public Master(int n, List<List<Integer>> edgeList){
        // edgeList: [src, dst, weight]
        this.graph = new Graph(n, edgeList, false);
        this.createWorkers();
        this.partition();
    }

    public void createWorkers(){
        this.workers = new ArrayList<>();
        for (int i = 0; i < numberOfWorkers; i++){
            Worker t = new Worker(i);
            this.workers.add(t);
            t.start();
        }
    }

    public void partition(){
        for (VertexID id : this.graph.getVertexIdSet()){
            int mod = id.getIntValue() % Master.numberOfWorkers;
            Worker currentWorker =  this.workers.get(mod);
            currentWorker.assignVertex(this.graph.getVertex(id));
        }
    }

    public void startBFS(int srcVertex){
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

    public void setState(ControlSignal signal, WorkerState state) {
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

    public void printResults(int srcVertex){
        System.out.println("\nFinal distances from source " + srcVertex + ":");
        this.graph.getVertexValueSet().stream()
                .sorted(Comparator.comparingInt(v -> v.getId().getIntValue()))
                .forEach(v -> System.out.println("Vertex "
                        + v.getId().getIntValue()
                        + " -> distance " + (v.getDistance()!=Integer.MAX_VALUE ? v.getDistance():"INFINITY")));
    }


    public void runBFS(int srcVertex){
        this.startBFS(srcVertex);
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
        this.printResults(srcVertex);
        this.setState(ControlSignal.SHUTDOWN, WorkerState.SHUTDOWN);
    }


    public static void main(String[] args) {
        // TEST
        List<List<Integer>> edges = new ArrayList<>();
        edges.add(Arrays.asList(0, 1));
        edges.add(Arrays.asList(0, 2));
        edges.add(Arrays.asList(1, 3));
        edges.add(Arrays.asList(1, 4));
        edges.add(Arrays.asList(2, 5));
        edges.add(Arrays.asList(2, 6));
        edges.add(Arrays.asList(3, 7));
        edges.add(Arrays.asList(4, 8));
        edges.add(Arrays.asList(5, 9));
        edges.add(Arrays.asList(6, 10));
        edges.add(Arrays.asList(7, 11));
        edges.add(Arrays.asList(8, 12));
        edges.add(Arrays.asList(9, 13));
        edges.add(Arrays.asList(10, 14));
        edges.add(Arrays.asList(11, 12));

        Master master = new Master(15, edges);
        master.runBFS(3);

    }
}
