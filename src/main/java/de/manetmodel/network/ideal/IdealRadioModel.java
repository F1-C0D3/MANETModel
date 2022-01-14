package de.manetmodel.network.ideal;

import de.manetmodel.network.LinkQuality;
import de.manetmodel.radiomodel.RadioModel;
import de.manetmodel.units.DataRate;

public class IdealRadioModel extends RadioModel<IdealRadioNode, IdealRadioLink, IdealLinkQuality> {
       

    public IdealRadioModel(DataRate transmissionBitrate,double linkMinTransmissionRange,double linkMaxTransmissionRange) {
	super(transmissionBitrate,linkMinTransmissionRange,linkMaxTransmissionRange);	
    }


    public void setNodeRadioParameters(IdealRadioNode node, double coverageRange) {
	node.setTransmissionRange(coverageRange);
    }
}