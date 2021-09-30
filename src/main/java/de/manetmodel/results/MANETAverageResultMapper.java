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
	super(scenario,mappingStrategy);
    }

    @Override
    public AverageResultParameter averageRunResultMapper(List<RunResultParameter> runs, Time duration) {
	AverageResultParameter averageRunParemeter = new AverageResultParameter();

	long overUtilization = 0l;
	long averageUtilization = 0l;
	int activeLinks = 0;
	double averageConnectivityStability = 0d;
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
	    averageRunActiveLinks += activeLinks / (double) run.getFirst().size();
	}
	averageRunActiveLinks = averageRunActiveLinks / (double) runs.size();

	averageRunParemeter.setOverUtilization(averageRunParemeter.getOverUtilization() / runs.size());
	averageRunParemeter.setUtilization(averageRunParemeter.getUtilization() / runs.size());
	averageRunParemeter.setConnectionStability(averageRunParemeter.getConnectionStability() / runs.size());
	averageRunParemeter.setSimulationTime(new Time(simulationTime.getMillis() / (long) runs.size()));
	averageRunParemeter.setActivePathParticipants(averageRunActiveLinks);

	return averageRunParemeter;
    }

    @Override
    public ColumnPositionMappingStrategy<AverageResultParameter> getMappingStrategy() {
	return this.mappingStrategy;
    }

    @Override
    public void setMappingStrategy(ColumnPositionMappingStrategy<AverageResultParameter> mappingStrategy) {
	this.mappingStrategy = mappingStrategy;
    }

}
