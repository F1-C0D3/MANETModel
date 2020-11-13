package de.manetgraph;

public interface IManetVertex extends IManetElement{

	int getID();
	void setID(int ID);		
	double x();
	double y();	
	Coordinate getPostion();
	void setPosition(double x, double y);	
	
}
