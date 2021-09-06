package de.manetmodel.radiomodel;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.units.DataRate;

public abstract class RadioModel<N extends Node,L extends Link<W>, W extends LinkQuality> {

    private DataRate transmissionBitrate;

    public RadioModel(DataRate transmissionBitrate) {
	this.transmissionBitrate = transmissionBitrate;
    }

    public void setLinkRadioParameters(L link, double linkDistance) {
	link.setTransmissionRate(transmissionBitrate);;
    }

    public abstract void setNodeRadioParameters(N node, double coverageRange);
}
