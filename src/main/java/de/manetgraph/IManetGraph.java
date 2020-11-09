package de.manetgraph;

import java.util.List;

import de.manetgraph.util.Tuple;

public interface IManetGraph<V extends IManetVertex, E extends IManetEdge> {
		
	V createVertex();

	void addVertices(List<V> vertices);	
	
	void addEdges(List<Tuple<V,V>> edges);
}
 