package communication;
import graph.VertexID;

public class Message {
    VertexID address;
    int distance;
    String Message;
    public Message(VertexID from,int distance){
        this.address = from;
        this.distance = distance;
    }

    public VertexID getAddress(){
        return this.address;
    }

    public int getDistance(){
        return this.distance;
    }

    @Override
    public String toString() {
        return this.address.getIntValue() + " has distance " + this.distance;
    }
}
