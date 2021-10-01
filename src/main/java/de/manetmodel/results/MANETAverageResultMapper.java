package de.manetmodel.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.util.Tuple;
import de.manetmodel.scenarios.Scenario;
import de.manetmodel.units.Time;

public class MANETAverageResultMapper extends AverageResultMapper<AverageResultParameter, RunResultParameter> {

    public MANETAverageResultMapper(ColumnPositionMappingStrategy<AverageResultParameter> mappingStrategy,
	    Scenario scenario) {
	super(scenario, mappingStrategy);
    }

    @Override
    public AverageResultParameter averageRunResultMapper(List<RunResultParameter> runParameters, Time duration) {
	AverageResultParameter averageRunParemeter = new AverageResultParameter();

	long overUtilization = 0l;
	long averageUtilization = 0l;
	int activeLinks = 0;
	double averageConnectivityStability = 0d;
	for (RunResultParameter runParameter : runParameters) {

	    if (runParameter.isPathParticipant()) {

		if (runParameter.getOverUtilization() != 0l) {
		    overUtilization += runParameter.getOverUtilization();
		}

		if (runParameter.getConnectionStability() != 0) {
		    averageConnectivityStability += runParameter.getConnectionStability();
		    activeLinks++;
		}
	    }

	    if (runParameter.getUtilization() != 0l) {
		averageUtilization += runParameter.getUtilization();
	    }

	}
	averageRunParemeter.setOverUtilization(overUtilization);
	averageRunParemeter.setUtilization(averageUtilization);
	averageRunParemeter.setConnectionStability(averageConnectivityStability / activeLinks);
	averageRunParemeter.setActivePathParticipants(activeLinks);
	averageRunParemeter.setSimulationTime(duration);

	return averageRunParemeter;

    }

    @Override
    public AverageResultParameter toMeanMapper(List<Tuple<List<RunResultParameter>, Time>> runs) {
	AverageResultParameter averageRunParemeter = new AverageResultParameter();

	Time simulationTime = new Time();
	double averageRunActiveLinks = 0;

	double runConnectionStability = 0;
	for (Tuple<List<RunResultParameter>, Time> run : runs) {
	    int activeLinks = 0;
	    for (RunResultParameter runResultParameter : run.getFirst()) {
		if (runResultParameter.isPathParticipant()) {
		    averageRunParemeter.setOverUtilization(
			    averageRunParemeter.getOverUtilization() + runResultParameter.getOverUtilization());
		    averageRunParemeter.setConnectionStability(
			    averageRunParemeter.getConnectionStability() + runResultParameter.getConnectionStability());
		    activeLinks++;
		}
		averageRunParemeter
			.setUtilization(averageRunParemeter.getUtilization() + runResultParameter.getUtilization());

	    }
	    simulationTime.set(simulationTime.getMillis() + run.getSecond().getMillis());
	    averageRunActiveLinks += activeLinks;
	    runConnectionStability += averageRunParemeter.getConnectionStability() / activeLinks;
	}

	averageRunParemeter.setOverUtilization(averageRunParemeter.getOverUtilization() / runs.size());
	averageRunParemeter.setUtilization(averageRunParemeter.getUtilization() / runs.size());
	averageRunParemeter.setConnectionStability(runConnectionStability / runs.size());
	averageRunParemeter.setSimulationTime(new Time(simulationTime.getMillis() / (long) runs.size()));
	averageRunParemeter.setActivePathParticipants(averageRunActiveLinks / (double) runs.size());

	return averageRunParemeter;
    }

}
