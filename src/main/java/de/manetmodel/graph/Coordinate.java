package de.manetmodel.graph;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Coordinate")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinate {
	
	@XmlElement
	private double x;
	@XmlElement
	private double y;
	
	public Coordinate() {}
	
	public Coordinate(double x, double y) {		
		this.x = x;
		this.y = y;		
	}
	
	public double x() {
		return this.x;
	}
	
	public double y() {
		return this.y;
	}
}
