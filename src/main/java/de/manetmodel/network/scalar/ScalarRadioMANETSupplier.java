package de.manetmodel.network.scalar;

import java.util.function.Supplier;

import de.jgraphlib.graph.suppliers.EdgeWeightSupplier;
import de.manetmodel.network.LinkQuality;

public class ScalarRadioMANETSupplier {

    public Supplier<ScalarRadioNode> getNodeSupplier() {
	return new ScalarRadioNodeSupplier();
    }

    public Supplier<ScalarRadioLink> getLinkSupplier() {
	return new ScalarRadioLinkSupplier();
    }

    public Supplier<ScalarLinkQuality> getLinkPropertySupplier() {
	return new ScalarLinkQualitySupplier();
    }

    public Supplier<ScalarRadioFlow> getFlowSupplier() {
	return new ScalarRadioFlowSupplier();
    }

    public static class ScalarRadioNodeSupplier implements Supplier<ScalarRadioNode> {
	@Override
	public ScalarRadioNode get() {
	    return new ScalarRadioNode();
	}
    }

    public static class ScalarRadioLinkSupplier implements Supplier<ScalarRadioLink> {
	@Override
	public ScalarRadioLink get() {
	    return new ScalarRadioLink();
	}
    }

    public static class ScalarLinkQualitySupplier extends EdgeWeightSupplier<ScalarLinkQuality> {
	@Override
	public ScalarLinkQuality get() {
	    return new ScalarLinkQuality();
	}
    }

    public static class ScalarRadioFlowSupplier implements Supplier<ScalarRadioFlow> {
	@Override
	public ScalarRadioFlow get() {
	    // TODO Auto-generated method stub
	    return new ScalarRadioFlow();
	}
    }
}