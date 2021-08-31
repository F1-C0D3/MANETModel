package de.manetmodel.network.radio;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.network.unit.DataRate;

public class RadioModel<N extends Node,L extends Link<W>, W extends LinkQuality> implements IRadioModel<N, L, W> {

    private DataRate transmissionBitrate;

    public RadioModel(DataRate transmissionBitrate) {
	this.transmissionBitrate = transmissionBitrate;
    }

    @Override
    public void setLinkRadioParameters(L link, double linkDistance) {
	link.setTransmissionRate(transmissionBitrate);;
	
    }

    @Override
    public void setNodeRadioParameters(N node, double coverageRange) {
	// TODO Auto-generated method stub
	
    }



}
