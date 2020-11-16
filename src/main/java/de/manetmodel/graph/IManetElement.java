package de.manetmodel.graph;

import java.util.ArrayList;
import java.util.Set;

public interface IManetElement {
	public double getDistance();
	public Set<Integer> getOccupation();
	public boolean equals(Object o);	
	public String toString();	
}
