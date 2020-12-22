package de.manetmodel.network;

import java.util.ArrayList;

public class Cost implements Comparable<Cost> {

    private double distance;
    private ArrayList<Integer> occupation;

    public Cost() {
	this.distance = 0;
	this.occupation = new ArrayList<Integer>();
    }

    public void addDistance(double distance) {
	this.distance += distance;
    }

    public void setDistance(double distance) {
	this.distance = distance;
    }

    public double getDistance() {
	return this.distance;
    }

    public void addOccupation(int edgeID) {
	if (!occupation.contains(edgeID))
	    this.occupation.add(edgeID);
    }

    public void setOccupation(ArrayList<Integer> occupation) {
	this.occupation = occupation;
    }

    public ArrayList<Integer> getOccupation() {
	return this.occupation;
    }

    public boolean isCheaper(Cost cost) {
	if (cost != null)
	    if (this.distance < cost.getDistance())
		return true;
	return false;
    }

    @Override
    public int compareTo(Cost cost) {
	return Double.compare(this.distance, cost.getDistance());
    }

    @Override
    public String toString() {
	return String.format("%.2f", this.distance);
    }
}
