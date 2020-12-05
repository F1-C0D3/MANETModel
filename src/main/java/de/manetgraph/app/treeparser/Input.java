package de.manetgraph.app.treeparser;

import de.manetmodel.util.Tuple;

public class Input {	
	ValueType valueType;
	
	public Integer INT;
	public Double DOUBLE;
	public String STRING;
	public Tuple<Integer,Integer> INT_TUPLE;		
	public Tuple<Double, Double> DOUBLE_TUPLE;
		
	public Input() {}	
	
	public Input(int INT) {
		this.INT = INT;
	}
}
