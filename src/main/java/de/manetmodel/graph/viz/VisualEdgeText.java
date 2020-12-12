package de.manetmodel.graph.viz;

import de.manetmodel.graph.Edge;

public abstract class VisualEdgeText<E extends Edge> {
    
    public abstract String get(E edge);
}
