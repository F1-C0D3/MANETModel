package de.manetgraph;

public class Cost implements Comparable<Cost>{

	private double distance;
	
	public Cost(){
		this.distance = 0;
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
		
	public boolean isCheaper(Cost cost) {
		if(cost != null) 
			if(this.distance < cost.getDistance()) 
				return true;
		
		return false;
	}

	public int compareTo(Cost cost) {	
		return Double.compare(this.distance, cost.getDistance());
	}
	
	@Override 
	public String toString() {
		return String.format("%.2f", this.distance);
	}
	
}
