package de.manetmodel.graph;

public class EdgeDistanceSupplier extends EdgeWeightSupplier<EdgeDistance> {

    @Override
    public EdgeDistance get() {
	return new EdgeDistance();	
    }
}
