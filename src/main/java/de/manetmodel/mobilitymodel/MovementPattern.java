package de.manetmodel.mobilitymodel;

import de.jgraphlib.graph.elements.Position2D;
import de.manetmodel.units.Speed;

public class MovementPattern {

    private Speed speed;
    private Position2D absolutePostion;
    double absoluteAngle;

    public MovementPattern() {
    }

    public MovementPattern(Speed speed, Position2D position,double absoluteAngle) {
	this.speed = speed;
	this.absolutePostion = position;
	this.absoluteAngle = absoluteAngle;
    }

    public Speed getSpeed() {
	return speed;
    }

    public void setSpeed(Speed speed) {
	this.speed = speed;
    }

    public Position2D getPostion() {
	return absolutePostion;
    }

    public void setPostion(Position2D postion) {
	this.absolutePostion = postion;
    }

    public double getAngle() {
	return absoluteAngle;
    }

    public void setAngle(double angle) {
	this.absoluteAngle = angle;
    }
    
    public String toString() {
	return String.format("speed: %s, position: %s, angle: %.2f", speed.toString(), absolutePostion.toString(), absoluteAngle);
    }
}
