package de.manetmodel.network.scalar;

import de.manetmodel.network.Flow;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.units.DataRate;

public class ScalarRadioFlow extends Flow<ScalarRadioNode, ScalarRadioLink, ScalarLinkQuality> {

    private static final long serialVersionUID = 1L;

    public ScalarRadioFlow() {
    }

    public ScalarRadioFlow(ScalarRadioFlow flow) {
	super(flow);
    }

    public ScalarRadioFlow(ScalarRadioNode source, ScalarRadioNode sink, DataRate dataRate) {
	super(source, sink, dataRate);
    }

    public ScalarRadioFlow(int flowId, ScalarRadioNode source, ScalarRadioNode sink, DataRate dataRate) {
	super(flowId, source, sink, dataRate);
    }
}
