package de.manetmodel.graph;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EdgeVertexMapping {
	
	@XmlElement(name="EdgeID")
	private Integer edgeID;
	@XmlElement(name="VertexID")
	private Integer vertexID;
	
	public EdgeVertexMapping() {}
	
	public EdgeVertexMapping(Integer edgeID, Integer vertexID) {
		this.edgeID = edgeID;
		this.vertexID = vertexID;
	}
	
	public Integer getEdgeID() {
		return this.edgeID;
	}
	
	public Integer getVertexID() {
		return this.vertexID;
	}
}
