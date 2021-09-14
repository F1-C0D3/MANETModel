package de.manetmodel.radiomodel;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.units.DataRate;

public abstract class RadioModel<N extends Node,L extends Link<W>, W extends LinkQuality> {

    protected DataRate transmissionBitrate;
    protected final double linkMaxTransmissionRange;

    public RadioModel(DataRate transmissionBitrate, double linkMaxTransmissionRange) {
	this.linkMaxTransmissionRange = linkMaxTransmissionRange;
	this.transmissionBitrate = transmissionBitrate;
    }
    
    public double getLinkMaxTransmissionRange() {
	return linkMaxTransmissionRange;
    }

    public void setLinkRadioParameters(L link, double linkDistance) {
	link.setTransmissionRate(transmissionBitrate);;
    }

    public abstract void setNodeRadioParameters(N node, double coverageRange);
}
