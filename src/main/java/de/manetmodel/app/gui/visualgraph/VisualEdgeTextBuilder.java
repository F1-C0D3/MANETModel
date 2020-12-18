package de.manetmodel.app.gui.visualgraph;

import de.manetmodel.graph.Edge;

public abstract class VisualEdgeTextBuilder<E extends Edge> {
    
    public abstract String get(E edge);
}
