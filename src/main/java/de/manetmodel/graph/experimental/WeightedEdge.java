package de.manetmodel.graph.experimental;

public class WeightedEdge<W> {
	
	int ID;
	W weight;
	
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
}
