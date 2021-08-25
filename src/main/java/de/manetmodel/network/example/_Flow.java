package de.manetmodel.network.example;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.network.unit.DataRate;

public class _Flow extends Flow<_Node, _Link, _LinkQuality>{

    private static final long serialVersionUID = 1L;

    public _Flow(_Node source, _Node sink, DataRate dataRate) {
	super(source, sink, dataRate);
    }

    public _Flow() {
    }
    
}
