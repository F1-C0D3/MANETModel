package de.manetmodel.network.example;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.network.unit.DataRate;

public class myFlow extends Flow<Node, Link<LinkQuality>, LinkQuality>{

    public myFlow(Node source, Node sink, DataRate dataRate) {
	super(source, sink, dataRate);
    }

    public myFlow() {
    }
    
}
