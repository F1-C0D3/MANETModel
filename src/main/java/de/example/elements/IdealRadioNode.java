package de.example.elements;

import de.manetmodel.network.Node;

public class IdealRadioNode extends Node {

    private double transmissionRange;

    public double getTransmissionRange() {
	return transmissionRange;
    }

    public void setTransmissionRange(double transmissionRange) {
	this.transmissionRange = transmissionRange;
    }

}
