package de.manetmodel.network.ideal;

import de.manetmodel.network.LinkQuality;
import de.manetmodel.radiomodel.RadioModel;
import de.manetmodel.units.DataRate;

public class IdealRadioModel extends RadioModel<IdealRadioNode, IdealRadioLink, LinkQuality> {
       
    private DataRate transmissionBitrate;

    public IdealRadioModel(DataRate transmissionBitrate) {
	super(transmissionBitrate);	
    }

    public void setLinkRadioParameters(IdealRadioLink link, double linkDistance) {
	link.setTransmissionRate(transmissionBitrate);
    }

    public void setNodeRadioParameters(IdealRadioNode node, double coverageRange) {
	node.setTransmissionRange(coverageRange);
    }
}