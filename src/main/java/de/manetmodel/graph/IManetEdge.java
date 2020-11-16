package de.manetmodel.graph;

import java.awt.RenderingHints;
import java.util.Set;

public interface IManetEdge {

	int getID();
	Set<Integer> getOccupation();
	double getDistance();
}
