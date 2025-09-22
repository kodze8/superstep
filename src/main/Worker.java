package main;

import communication.Message;
import communication.MessageEmitter;
import graph.Vertex;
import graph.VertexID;

import java.util.*;



public class Worker extends Thread implements MessageEmitter {
    private final int id;

    private final HashMap<VertexID, Vertex> vertices;

    private Queue<Message> currentQueue;
    private volatile Queue<Message> nextQueue;
    private  volatile Queue<String> controlSignals;
    private  volatile WorkerState stateSignal;

    private volatile Queue<Message> msgForNeighbors;
    private volatile Boolean running;





    public Worker(int temp){
        this.running = true;
        this.vertices = new HashMap<>();
        this.controlSignals = new LinkedList<>();
        this.currentQueue = new LinkedList<>();
        this.nextQueue = new LinkedList<>();
        this.msgForNeighbors = new LinkedList<>();
        this.stateSignal = WorkerState.STARTED;
        this.id = temp;

    }
    public void assignVertex(Vertex vertex){
        this.vertices.put(vertex.getId(), vertex);

    }

    public boolean containsVertex(VertexID id){
        return this.vertices.containsKey(id);
    }

    public synchronized void acceptControlMessage(String message) {
        this.controlSignals.add(message);
    }
    private synchronized String getControlMessage() {
        return this.controlSignals.poll();
    }

    private synchronized boolean controlQueueIsEmpty() {
        return this.controlSignals.isEmpty();
    }

    public synchronized void addToNextQueue(Message message) {
        this.nextQueue.add(message);
    }

    public synchronized WorkerState getWorkerState(){
        return this.stateSignal;
    }
    private synchronized void setWorkerState(WorkerState state){
         this.stateSignal = state;
    }




    public int getWorkerId(){
        return this.id;
    }

    public synchronized void setNextToCurrent(){
        this.currentQueue = new LinkedList<>(this.nextQueue);
        this.nextQueue=  new LinkedList<>();
        this.msgForNeighbors = new LinkedList<>();
        //System.out.println("for worker "+this.id+" current queue looks like: "+this.currentQueue);
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
                String temp = this.getControlMessage();

                if (temp.equals("START")){
                    this.setWorkerState(WorkerState.STARTED);
                } else if (temp.equals("PROCESS")) {
                    while (!this.currentQueue.isEmpty()){
                        Message msg = this.currentQueue.poll();
                        this.processMsg(msg);
                    }
                    this.setWorkerState(WorkerState.FINISHED);
                }
            }
        }
    }

    public synchronized Queue<Message> getMessageForNeighbors(){
        return this.msgForNeighbors;
    }

    @Override
    public synchronized void emit(Message m) {
        synchronized(this) {
            this.msgForNeighbors.add(m);
        }
    }

    public void shutDown(){
        this.running = false;

    }
}
