package de.manetmodel.graph;

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VertexAdjacency {
	
	@XmlElementWrapper(name="EdgeVertexMappings")
	@XmlElement(name="EdgeVertexMapping")
	private ArrayList<EdgeVertexMapping> edgeVertexMappings; 
	
	public VertexAdjacency() {
		this.edgeVertexMappings = new ArrayList<EdgeVertexMapping>();
	}	
	
	public void add(EdgeVertexMapping edgeVertexMapping) {
		this.edgeVertexMappings.add(edgeVertexMapping);
	}
	
	public ArrayList<EdgeVertexMapping> getEdgeVertexMappings() {
		return this.edgeVertexMappings;
	}
}
