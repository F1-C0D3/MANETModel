package de.manetmodel.graph.viz;

import java.text.DecimalFormat;

import de.manetmodel.graph.Edge;

public class VisualEdgeDistanceTextBuilder<E extends Edge> extends VisualEdgeTextBuilder<E> {

    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    
    public String get(E edge) {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(decimalFormat.format(edge.getDistance()));
	return stringBuilder.toString();
    }
}
