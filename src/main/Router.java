package main;

import communication.Message;

import java.util.List;

public class Router {
    private final List<Worker> workers;

    public Router(List<Worker> workers) {
        this.workers = workers;
    }

    public boolean routeAll() {
        boolean deliveredAny = false;

        for (Worker from: this.workers){
            for (Message msg: from.msgForNeighbors){
                System.out.println("  Worker-" + from.getWorkerId() + " sends msg: " + msg);

                for (Worker to: this.workers){
                    if (to.containsVertex(msg.getAddress())) {
                        to.addToNextQueue(msg);
                        deliveredAny = true;
                        break;
                    }
                }
            }
        }
        System.out.println(!deliveredAny ? "-> No new messages, halting." : "-> Messages exist, continuing.");
        return deliveredAny;
    }
}
