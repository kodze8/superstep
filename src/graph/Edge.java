package graph;

public  class Edge{
    private final VertexID src;
    private final VertexID dst;
    private final int weight;
    public Edge(VertexID src, VertexID dst, int weight){
        this.src =  src;
        this.dst = dst;
        this.weight = weight;
    }
    public Edge(VertexID src, VertexID dst){
        this(src, dst, 1);
    }

    public int getWeight(){
        return this.weight;
    }

    public VertexID getDst(){
        return this.dst;
    }
    public VertexID getSrc(){
        return this.src;
    }
}
