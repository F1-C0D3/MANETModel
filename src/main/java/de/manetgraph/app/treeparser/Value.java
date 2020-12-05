package de.manetgraph.app.treeparser;

import de.manetgraph.app.treeparser.Element;
import de.manetgraph.app.treeparser.ValueType;

public class Value extends Element{
	private ValueType type;
				
	public Value(ValueType type) {
		this.type = type;
	}
	
	public ValueType getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return this.type.toString();
	}
}
