package de.manetmodel.graph;

public class WeightedEdge<W> {

    private int ID;
    private W weight;

    public WeightedEdge() {}

    public WeightedEdge(W weight) {
	this.weight = weight;
    }

    public void setID(int ID) {
	this.ID = ID;
    }

    public int getID() {
	return this.ID;
    }

    public void setWeight(W weight) {
	this.weight = weight;
    }

    public W getWeight() {
	return this.weight;
    }

    public boolean equals(WeightedEdge<W> edge) {
	return edge.getID() == this.ID;
    }
}
