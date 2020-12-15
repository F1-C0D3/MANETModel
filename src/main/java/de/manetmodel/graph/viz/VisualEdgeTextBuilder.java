package de.manetmodel.graph.viz;

import de.manetmodel.graph.Edge;

public abstract class VisualEdgeTextBuilder<E extends Edge> {
    
    public abstract String get(E edge);
}
