package de.manetmodel.network;

import de.manetmodel.network.unit.DataRate;

public class myFlow extends Flow<Node, Link<LinkQuality>, LinkQuality>{

    public myFlow(Node source, Node sink, DataRate dataRate) {
	super(source, sink, dataRate);
    }

    public myFlow() {
    }
    
}
