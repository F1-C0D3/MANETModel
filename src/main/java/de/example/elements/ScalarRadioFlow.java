package de.example.elements;

import de.manetmodel.network.Flow;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.unit.DataRate;

public class ScalarRadioFlow extends Flow<ScalarRadioNode, ScalarRadioLink, LinkQuality> {

    private static final long serialVersionUID = 1L;

    public ScalarRadioFlow() {
    }

    public ScalarRadioFlow(ScalarRadioNode source, ScalarRadioNode sink, DataRate dataRate) {
	super(source, sink, dataRate);
    }

    public ScalarRadioFlow(int flowId, ScalarRadioNode source, ScalarRadioNode sink, DataRate dataRate) {
	super(flowId, source, sink, dataRate);
    }
}
