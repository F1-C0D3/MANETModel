package de.manetmodel.app.treeparser;

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
	
	public boolean hasINT() {
		return INT != null;
	}
	
	public boolean hasDOUBLE() {
		return DOUBLE != null;
	}
	
	public boolean hasSTRING() {
		return STRING != null;
	}
	
	public boolean hasINT_TUPLE() {
		return INT_TUPLE != null;
	}
	
	public boolean hasDOUBLE_TUPLE() {
		return DOUBLE_TUPLE != null;
	}
}
