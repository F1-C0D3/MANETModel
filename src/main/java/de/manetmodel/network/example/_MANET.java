package de.manetmodel.network.example;

import java.util.function.Supplier;

import de.manetmodel.network.MANET;
import de.manetmodel.network.radio.IRadioModel;

public class _MANET extends MANET<_Node, _Link, _LinkQuality, _Flow>{

    public _MANET(
	    Supplier<_Node> vertexSupplier, 
	    Supplier<_Link> edgeSupplier,
	    Supplier<_LinkQuality> edgeWeightSupplier, 
	    Supplier<_Flow> flowSupplier,
	    IRadioModel radioModel) {
	
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier, radioModel, null);
    }
    
    public _MANET(_MANET manet) {
	super(manet);
    }
    
    public _MANET copy() {
	return new _MANET(this);
    } 
}
