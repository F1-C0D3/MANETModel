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
	public boolean add(IManetElement acoElement) {
		super.add(acoElement);	
		
		if(this.cost == null)
			this.cost = new Cost();
		
		cost.addDistance(acoElement.getWeight());
		
		return true;
	}		
		
	@Override
	public String toString() {
		
		String str = "";
				
		for(IManetElement acoElement: this) {	
			if (acoElement instanceof IManetVertex) {		
				str += String.format("[%s]", acoElement.toString());	
			}
			else if(acoElement instanceof IManetEdge) {	
				str += String.format("- %.2f -", acoElement.getWeight());		
			}
		}	
		
		return str;	
	}	
}
