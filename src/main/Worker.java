package main;

import communication.Message;
import communication.MessageEmitter;
import graph.Vertex;
import graph.VertexID;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


public class Worker extends Thread implements MessageEmitter {
    private final int id;
    private volatile Boolean running = true;
    private final HashMap<VertexID, Vertex> vertices = new HashMap<>();
    private Queue<Message> currentQueue = new ConcurrentLinkedQueue<>();
    private Queue<Message> nextQueue = new ConcurrentLinkedQueue<>();
    public Queue<Message> msgForNeighbors = new ConcurrentLinkedQueue<>();
    private Queue<ControlSignal> controlSignals;
    private volatile WorkerState stateSignal;
    private CountDownLatch currentLatch;

    public Worker(int id){
        this.controlSignals = new LinkedList<>();
        this.stateSignal = WorkerState.READY;
        this.id = id;
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
    private synchronized void setWorkerState(WorkerState state){
        this.stateSignal = state;
        if (currentLatch != null) currentLatch.countDown();
    }

    /**Next & Current Queue Functionalities*/
    public void addToNextQueue(Message message) {
        this.nextQueue.add(message);
    }
    public void swapMessageQueues(){
        Queue<Message> temp = this.currentQueue;
        this.currentQueue = this.nextQueue;
        this.nextQueue = temp;
        nextQueue.clear();
        this.msgForNeighbors = new ConcurrentLinkedQueue<>();
    }

    public void processMsg(Message msg){
        VertexID address = msg.getAddress();
        Vertex vertex = vertices.get(address);
        vertex.compute(msg.getDistance(), this);
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
