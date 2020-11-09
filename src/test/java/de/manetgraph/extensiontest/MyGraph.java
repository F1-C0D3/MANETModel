package de.manetgraph.extensiontest;

import de.manetgraph.ManetEdge;
import de.manetgraph.ManetGraph;
import de.manetgraph.ManetVertex;

public class MyGraph extends ManetGraph{

	public MyGraph() {
		
		MyVertex a = new MyVertex(0,0);
		MyVertex b = new MyVertex(1,1);
		
		this.addVertex(a);
		this.addVertex(b);
		
		this.addEdge(a, b);
		
	}
	
	public class MyVertex extends ManetVertex{

		public MyVertex(double x, double y) {
			super(x, y);
		}	
	}
	
	public class MyEdge extends ManetEdge {
		
	}	
}


