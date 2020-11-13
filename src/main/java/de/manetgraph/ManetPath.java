package de.manetgraph;

import java.util.LinkedList;

public class ManetPath extends LinkedList<IManetElement>{

	ManetCost cost = null;
	
	public ManetPath(IManetVertex source) {
		this.add(source);
	}
	
	public ManetCost getCost() {
		return this.cost;
	}
	
	public boolean isCheaper(ManetPath path) {		
		if(path != null) return this.cost.isCheaper(path.getCost());				
		return false;
	}
	
	@Override
	public boolean add(IManetElement manetElement) {
		super.add(manetElement);		
		if(this.cost == null) this.cost = new ManetCost();	
		cost.addDistance(manetElement.getDistance());
		return true;
	}		
		
	@Override
	public String toString() {		
		String str = "";				
		for(IManetElement manetElement: this) {	
			if (manetElement instanceof IManetVertex) 
				str += String.format("[%s]", manetElement.toString());	
			else if(manetElement instanceof IManetEdge) 	
				str += String.format("- %.2f -", manetElement.getDistance());		
		}		
		return str;	
	}	
}
