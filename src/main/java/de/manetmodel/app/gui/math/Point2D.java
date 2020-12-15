package de.manetmodel.app.gui.math;

import java.text.DecimalFormat;

public class Point2D {

    private final double x;
    private final double y;
    
    public Point2D(double x, double y) {
	this.x = x;
	this.y = y;
    }
    
    public Double x() {
	return this.x;
    }
    
    public Double y() {
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
