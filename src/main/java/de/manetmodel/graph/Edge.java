package de.manetmodel.graph;

import java.util.Objects;

import de.manetmodel.util.Tuple;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Edge")
@XmlAccessorType(XmlAccessType.FIELD)
public class Edge implements IEdge, Comparable<Edge> {

    @XmlElement
    protected int ID;
    @XmlElement
    protected int vertexID1;
    @XmlElement
    protected int vertexID2;
    @XmlElement
    double distance;

    public void setID(int ID) {
	this.ID = ID;
    }

    public int getID() {
	return this.ID;
    }

    public void setVertexIDs(int vertexID1, int vertexID2) {
	this.vertexID1 = vertexID1;
	this.vertexID2 = vertexID2;
    }

    public Tuple<Integer, Integer> getVertexIDs() {
	return new Tuple<Integer, Integer>(this.vertexID1, vertexID2);
    }

    public double getDistance() {
	return this.distance;
    }

    public double setDistance(double distance) {
	return this.distance = distance;
    }

    public int compareTo(Edge edge) {
	return Double.compare(edge.getDistance(), this.getDistance());
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null)
	    return false;
	if (getClass() != o.getClass())
	    return false;
	Edge node = (Edge) o;
	return Objects.equals(this.ID, node.ID);
    }

    @Override
    public String toString() {
	return new StringBuffer("ID: ").append(this.ID).append(", vertexID: ").append(this.vertexID1)
		.append(", vertexID: ").append(this.vertexID2).append(", distance: ").append(this.distance).toString();
    }
}
