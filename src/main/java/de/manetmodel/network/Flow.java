package de.manetmodel.network;

import de.manetmodel.graph.ManetPath;
import de.manetmodel.util.Tuple;

public class Flow<N extends Node, L extends Link> extends ManetPath<N, L>{

	public Flow(N source) {
		super(source);
	}

}
