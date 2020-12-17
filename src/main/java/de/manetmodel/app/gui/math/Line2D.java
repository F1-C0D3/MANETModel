package de.manetmodel.app.gui.math;

import java.text.DecimalFormat;

public class Line2D {

    private final double m, b;
    private Point2D p1, p2;

    public Line2D(double m, double b) {
	this.m = m;
	this.b = b;
    }

    public Line2D(double x1, double y1, double x2, double y2) {
	this.p1 = new Point2D(x1, y1);
	this.p2 = new Point2D(x2, y2);
	this.m = (y2 - y1) / (x2 - x1);
	this.b = y2 - m * x2;
    }

    public Point2D p1() {
	return this.p1;
    }

    public Point2D p2() {
	return this.p2;
    }

    public Double x1() {
	return this.p1.x();
    }

    public Double y1() {
	return this.p1.y();
    }

    public Double x2() {
	return this.p2.x();
    }

    public Double y2() {
	return this.p2.y();
    }

    public double getSlope() {
	return this.m;
    }

    public double getPerpendicularSlope() {
	if (this.m == 0)
	    return Double.POSITIVE_INFINITY;
	else
	    return (-1) / m;
    }

    public Point2D getIntersection(Line2D line) {
	double x = (this.b - line.b) / (this.m - line.m);
	double y = this.m * x + this.b;
	return new Point2D(x, y);
    }
    
    public double getLength() {
	return Math.sqrt(Math.pow(p2.x() - p1.x(), 2) + Math.pow(p2.y() - p1.y(), 2));
    }

    @Override
    public String toString() {
	DecimalFormat decimalFormat = new DecimalFormat("#.00");
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("1: ").append(p1.toString()).append(", 2: ").append(p2.toString());
	stringBuilder.append(", m: ").append(decimalFormat.format(m));
	return stringBuilder.toString();
    }
}
