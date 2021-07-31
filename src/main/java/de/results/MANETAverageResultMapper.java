package de.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.Time;

public class MANETAverageResultMapper<K extends AverageResultParameter> extends AverageResultMapper<K> {

    public MANETAverageResultMapper(Supplier<K> averageResultParameterSupplier,
	    ColumnPositionMappingStrategy<K> mappingStrategy, Scenario scenario) {
	super(averageResultParameterSupplier, mappingStrategy, scenario);
    }

    @Override
    public <R extends RunResultParameter> K averageRunResultMapper(List<R> runs, Time duration) {
	K r = resultParameterSupplier.get();

	long overUtilization = 0l;
	int numOverUtilization = 0;
	long averageUtilization = 0l;
	int activeLinks = 0;
	int averageConnectivityStability = 0;
	for (RunResultParameter run : runs) {

	    if (run.isPathParticipant()) {

		if (run.getOverUtilization() != 0l) {
		    overUtilization += run.getOverUtilization();
		}

		if (run.getConnectionStability() != 0) {
		    averageConnectivityStability += run.getConnectionStability();
		    activeLinks++;
		}
	    }

	    if (run.getUtilization() != 0l) {
		averageUtilization += run.getUtilization();
	    }

	}
	r.setOverUtilization(overUtilization);
	r.setUtilization(averageUtilization);
	r.setConnectionStability(averageConnectivityStability / activeLinks);
	r.setActivePathParticipants(activeLinks / runs.size());
	r.setSimulationTime(duration);

	return r;

    }

    @Override
    public <R extends RunResultParameter> K toMeanMapper(List<Tuple<List<R>, Time>> runs) {
	K result = resultParameterSupplier.get();

	Time simulationTime = new Time();
	double averageRunActiveLinks = 0;

	for (Tuple<List<R>, Time> run : runs) {
	    int activeLinks = 0;
	    for (R r : run.getFirst()) {
		if (r.isPathParticipant()) {
		    result.setOverUtilization(result.getOverUtilization() + r.getOverUtilization());
		    result.setConnectionStability(result.getConnectionStability() + r.getConnectionStability());
		    activeLinks++;
		}
		result.setUtilization(result.getUtilization() + r.getUtilization());

	    }
	    simulationTime.set(simulationTime.getMillis() + run.getSecond().getMillis());
	    averageRunActiveLinks += activeLinks / (double) run.getFirst().size();
	}
	averageRunActiveLinks = averageRunActiveLinks / (double) runs.size();

	result.setOverUtilization(result.getOverUtilization() / runs.size());
	result.setUtilization(result.getUtilization() / runs.size());
	result.setConnectionStability(result.getConnectionStability() / runs.size());
	result.setSimulationTime(new Time(simulationTime.getMillis() / (long) runs.size()));
	result.setActivePathParticipants(averageRunActiveLinks);

	return result;
    }

}
