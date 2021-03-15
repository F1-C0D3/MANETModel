package de.manetmodel.mobility;

import de.jgraphlib.graph.Position2D;

public class MovementPattern {

    private double speed;
    private Position2D absolutePostion;
    double absoluteAngle;

    public MovementPattern() {
    }

    public MovementPattern(double speed, Position2D position,double absoluteAngle) {
	this.speed = speed;
	this.absolutePostion = position;
	this.absoluteAngle = absoluteAngle;
    }

    public double getSpeed() {
	return speed;
    }

    public void setSpeed(double speed) {
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

}
