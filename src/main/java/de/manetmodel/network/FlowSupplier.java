package de.manetmodel.network;

import java.util.function.Supplier;

public class FlowSupplier<N extends Node, L extends Link<W>, W extends LinkQuality> implements Supplier<Flow<N, L, W>> {

    public FlowSupplier() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public Flow<N, L, W> get() {
	// TODO Auto-generated method stub
	return new Flow<N, L, W>();
    }

}
