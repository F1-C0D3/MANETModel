package de.manetmodel.graph;

import de.manetmodel.util.Tuple;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EdgeAdjacency {

	@XmlElement
	private Integer vertexID1;
	@XmlElement
	private Integer vertexID2;
	
	public EdgeAdjacency() {}
	
	public EdgeAdjacency(Integer vertexID1, Integer vertexID2) {
		this.vertexID1 = vertexID1;
		this.vertexID2 = vertexID2;
	}
	
	public Tuple<Integer,Integer> getVertexIDs() {
		return new Tuple<Integer,Integer>(vertexID1, vertexID2);
	}
}
