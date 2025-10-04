package messaging;
import graph.VertexID;

public class Message{
    VertexID address;
    double value;
    String Message;
    public Message(VertexID from, double value){
        this.address = from;
        this.value = value;
    }

    public VertexID getAddress(){
        return this.address;
    }

    public double getValue(){
        return this.value;
    }

    @Override
    public String toString() {
        return "Vertex-"+this.address.getIntValue() + " has distance " + this.value;
    }
}
