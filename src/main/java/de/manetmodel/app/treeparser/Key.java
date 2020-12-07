package de.manetmodel.app.treeparser;

public class Key extends Element{ 
	public final String string;
	
	public Key(String string) {
		this.string = string;
	}
			
	@Override
	public String toString() {
		return this.string;
	}
}
