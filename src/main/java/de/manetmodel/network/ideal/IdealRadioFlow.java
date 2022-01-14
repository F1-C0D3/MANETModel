package de.manetmodel.network.ideal;

import de.manetmodel.network.Flow;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.units.DataRate;

public class IdealRadioFlow extends Flow<IdealRadioNode, IdealRadioLink, IdealLinkQuality> {

    private static final long serialVersionUID = 1L;

    public IdealRadioFlow() {
    }

    public IdealRadioFlow(IdealRadioFlow flow) {
	super(flow);
    }

    public IdealRadioFlow(IdealRadioNode source, IdealRadioNode sink, DataRate dataRate) {
	super(source, sink, dataRate);
    }

    public IdealRadioFlow(int flowId, IdealRadioNode source, IdealRadioNode sink, DataRate dataRate) {
	super(flowId, source, sink, dataRate);
    }
}
