package de.manetmodel.graph;

import java.text.DecimalFormat;

public class EdgeDistance extends EdgeWeight {

    private final double distance;
    
    
    public EdgeDistance(double distance) {
	this.distance = distance;
    }
    
    public double getDistance() {
	return this.distance;
    }
    
    @Override
    public String toString() {
	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	return decimalFormat.format(distance).toString();
    }
}
