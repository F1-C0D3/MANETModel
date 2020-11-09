package de.manetgraph;

import java.util.LinkedList;

public class Path extends LinkedList<IManetElement>{

	Cost cost = null;
	
	public Path(IManetVertex source) {
		this.add(source);
	}
	
	public Cost getCost() {
		return this.cost;
	}
	
	public boolean isCheaper(Path path) {		
		if(path != null) 
			return this.cost.isCheaper(path.getCost());		
		
		return false;
	}
	
	@Override
	public boolean add(IManetElement manetElement) {
		super.add(manetElement);	
		
		if(this.cost == null)
			this.cost = new Cost();
		
		cost.addDistance(manetElement.getWeight());
		
		return true;
	}		
		
	@Override
	public String toString() {
		
		String str = "";
				
		for(IManetElement manetElement: this) {	
			if (manetElement instanceof IManetVertex) {		
				str += String.format("[%s]", manetElement.toString());	
			}
			else if(manetElement instanceof IManetEdge) {	
				str += String.format("- %.2f -", manetElement.getWeight());		
			}
		}	
		
		return str;	
	}	
}
