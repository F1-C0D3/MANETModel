package de.manetmodel.network.ideal;

import de.manetmodel.network.LinkQuality;
import de.manetmodel.radiomodel.RadioModel;
import de.manetmodel.units.DataRate;

public class IdealRadioModel extends RadioModel<IdealRadioNode, IdealRadioLink, LinkQuality> {
       

    public IdealRadioModel(DataRate transmissionBitrate,double linkMaxTransmissionRange) {
	super(transmissionBitrate,linkMaxTransmissionRange);	
    }


    public void setNodeRadioParameters(IdealRadioNode node, double coverageRange) {
	node.setTransmissionRange(coverageRange);
    }
}