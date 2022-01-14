package de.manetmodel.network.ideal;

import java.util.function.Supplier;

import de.jgraphlib.graph.suppliers.EdgeWeightSupplier;
import de.manetmodel.network.Flow;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.scalar.ScalarLinkQuality;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioNode;

public class IdealRadioMANETSupplier {

    public Supplier<IdealRadioNode> getNodeSupplier() {
	return new IdealRadioNodeSupplier();
    }

    public Supplier<IdealRadioLink> getLinkSupplier() {
	return new ScalarRadioLinkSupplier();
    }

    public Supplier<IdealLinkQuality> getLinkPropertySupplier() {
	return new ScalarLinkQualitySupplier();
    }

    public Supplier<IdealRadioFlow> getFlowSupplier() {
	return new ScalarRadioFlowSupplier();
    }

    public static class IdealRadioNodeSupplier implements Supplier<IdealRadioNode> {
	@Override
	public IdealRadioNode get() {
	    return new IdealRadioNode();
	}
    }

    public static class ScalarRadioLinkSupplier implements Supplier<IdealRadioLink> {
	@Override
	public IdealRadioLink get() {
	    return new IdealRadioLink();
	}
    }

    public static class ScalarLinkQualitySupplier extends EdgeWeightSupplier<IdealLinkQuality> {
	@Override
	public IdealLinkQuality get() {
	    return new IdealLinkQuality();
	}
    }

    public static class ScalarRadioFlowSupplier implements Supplier<IdealRadioFlow> {
	@Override
	public IdealRadioFlow get() {
	    // TODO Auto-generated method stub
	    return new IdealRadioFlow();
	}
    }
}