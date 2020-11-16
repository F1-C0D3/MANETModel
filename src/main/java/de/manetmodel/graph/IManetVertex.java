package de.manetmodel.graph;

public interface IManetVertex {
	
	int getID();
	double x();
	double y();	
	void setID(int ID);
	void setPosition(double x, double y);
	Coordinate getPostion();
	
}
