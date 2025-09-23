package main;

import communication.Message;
import communication.MessageEmitter;
import graph.Vertex;
import graph.VertexID;

import java.util.*;
import java.util.concurrent.CountDownLatch;


public class Worker extends Thread implements MessageEmitter {
    private final int id;
    private final HashMap<VertexID, Vertex> vertices;
    private Queue<Message> currentQueue;
    private volatile Queue<Message> nextQueue;
    private  volatile Queue<ControlSignal> controlSignals;
    private  volatile WorkerState stateSignal;
    private volatile Queue<Message> msgForNeighbors;
    private volatile Boolean running;
    private CountDownLatch currentLatch;



    public Worker(int temp){
        this.running = true;
        this.vertices = new HashMap<>();
        this.controlSignals = new LinkedList<>();
        this.currentQueue = new LinkedList<>();
        this.nextQueue = new LinkedList<>();
        this.msgForNeighbors = new LinkedList<>();
        this.stateSignal = WorkerState.READY;
        this.id = temp;
    }

    public void assignVertex(Vertex vertex){
        this.vertices.put(vertex.getId(), vertex);

    }

    public boolean containsVertex(VertexID id){
        return this.vertices.containsKey(id);
    }

    public int getWorkerId(){
        return this.id;
    }

    public void shutDown(){
        this.running = false;
    }

    public synchronized Queue<Message> getMessageForNeighbors(){
        return this.msgForNeighbors;
    }

    /**Master Control Queue Functionalities*/
    public synchronized void acceptControlMessage(ControlSignal signal, CountDownLatch latch) {
        this.controlSignals.add(signal);
        this.currentLatch = latch;
    }
    private synchronized ControlSignal getControlMessage() {
        return this.controlSignals.poll();
    }
    private synchronized boolean controlQueueIsEmpty() {
        return this.controlSignals.isEmpty();
    }

    /**Worker State*/
    public synchronized WorkerState getWorkerState(){
        return this.stateSignal;
    }
    private synchronized void setWorkerState(WorkerState state){
        this.stateSignal = state;
        if (currentLatch != null) currentLatch.countDown();
    }

    /**Next & Current Queue Functionalities*/
    public synchronized void addToNextQueue(Message message) {
        this.nextQueue.add(message);
    }
    public synchronized void swapMessageQueues(){
        this.currentQueue = new LinkedList<>(this.nextQueue);
        this.nextQueue=  new LinkedList<>();
        this.msgForNeighbors = new LinkedList<>();
    }

    public void processMsg(Message msg){
        VertexID address = msg.getAddress();
        if (this.vertices.containsKey(address)){
            Vertex vertex = vertices.get(address);
            vertex.compute(msg.getDistance(), this);
        }
    }

    @Override
    public void run() {
        while (this.running) {
            if (!this.controlQueueIsEmpty()){
                ControlSignal signal = this.getControlMessage();
                switch (signal) {
                    case START_STEP -> this.setWorkerState(WorkerState.READY);
                    case PROCESS_MESSAGES -> {
                        while (!this.currentQueue.isEmpty()) {
                            Message msg = this.currentQueue.poll();
                            System.out.println("  Worker-" + this.id + " processes msg: " + msg);
                            this.processMsg(msg);
                        }
                        this.setWorkerState(WorkerState.FINISHED);
                    }
                    case SHUTDOWN -> {
                        this.shutDown();
                        this.setWorkerState(WorkerState.SHUTDOWN);
                    }
                }
            }
        }
    }



    @Override
    public synchronized void emit(Message m) {
        synchronized(this) {
            this.msgForNeighbors.add(m);
        }
    }


}
