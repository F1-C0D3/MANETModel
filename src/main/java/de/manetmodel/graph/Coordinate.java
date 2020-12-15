package de.manetmodel.graph;

import java.text.DecimalFormat;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Coordinate")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinate {

    @XmlElement
    private double x;
    @XmlElement
    private double y;

    public Coordinate() {
    }

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
    
    @Override 
    public String toString() {
	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("(x: ").append(decimalFormat.format(x)).append(", y: ").append(decimalFormat.format(y)).append(")");
	return stringBuilder.toString();
    }
}
