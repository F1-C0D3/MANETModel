package de.results;

import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;

public class MANETParameterRecorder<L extends Link<W>, W extends LinkQuality, R extends MANETRunResult>
	extends ParameterRecorder<L, W, R> {

    public MANETParameterRecorder(Supplier<R> supplier) {
	super(supplier);
    }

    @Override
    public R toResultFormatR(L l) {

	R r = supplier.get();
	r.setId(l.getID());

	double transmissionrate = l.getWeight().getTransmissionRate().get();
	double utilization = l.getWeight().getUtilization().get();

	if (transmissionrate < utilization)
	    r.setOverUtilization(utilization - transmissionrate);

	r.setUtilization(l.getWeight().getUtilization().get());
	return r;
    }

    public R toMean(List<List<R>> runs) {

	R result = supplier.get();

	for (List<R> run : runs) {
	    for (R r : run) {
		result.setOverUtilization(result.getOverUtilization() + r.getOverUtilization());
		result.setUtilization(result.getUtilization() + r.getUtilization());
	    }
	}

	result.setOverUtilization(result.getOverUtilization() / runs.size());
	result.setUtilization(result.getUtilization() / runs.size());

	return result;
    }
}