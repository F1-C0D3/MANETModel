package de.example.radio;

import de.example.elements.IdealRadioLink;
import de.example.elements.IdealRadioNode;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;

public class IdealRadioModel implements IRadioModel<IdealRadioNode, IdealRadioLink, LinkQuality> {
    
    
    private DataRate transmissionBitrate;

    public IdealRadioModel(DataRate transmissionBitrate) {
	this.transmissionBitrate = transmissionBitrate;
    }

    @Override
    public void setLinkRadioParameters(IdealRadioLink link, double linkDistance) {
	link.setTransmissionRate(transmissionBitrate);

    }

    @Override
    public void setNodeRadioParameters(IdealRadioNode node, double coverageRange) {
	node.setTransmissionRange(coverageRange);

    }

}