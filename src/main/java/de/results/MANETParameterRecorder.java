package de.results;

import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.network.LinkQuality;

public class MANETParameterRecorder<W extends LinkQuality, R extends MANETRunResult> extends ParameterRecorder<W, R> {

    public MANETParameterRecorder(Supplier<R> supplier, Scenario scenario) {
	super(supplier, scenario);
    }

    public MANETParameterRecorder(Supplier<R> supplier) {
	super(supplier);
    }

    @Override
    public R toResultFormatR(int n1Id, int n2Id, int lId, W w) {

	R r = supplier.get();
	r.setlId(lId);
	r.setN1Id(n1Id);
	r.setN2Id(n2Id);

	double transmissionrate = w.getTransmissionRate().get();
	double utilization = w.getUtilization().get();

	if (w.getIsActive()) {
	    r.setPathParticipant(true);
	    if (transmissionrate < utilization)
		r.setOverUtilization(utilization - transmissionrate);
	}
	r.setUtilization(w.getUtilization().get());
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