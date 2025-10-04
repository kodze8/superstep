package graph;

public class VertexID {
    private final int vertexID;
    public VertexID(int vertexID){
        this.vertexID = vertexID;
    }
    public int getIntValue(){
        return this.vertexID;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VertexID other)) return false;
        return this.vertexID == other.vertexID;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(vertexID);
    }
}
