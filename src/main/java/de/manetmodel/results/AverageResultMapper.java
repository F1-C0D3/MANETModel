package de.manetmodel.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.unit.Time;
import de.manetmodel.scenarios.Scenario;

public abstract class AverageResultMapper<A extends AverageResultParameter, R extends RunResultParameter>
	extends ResultMapper<A> {

    protected ColumnPositionMappingStrategy<A> mappingStrategy;

    public AverageResultMapper(ColumnPositionMappingStrategy<A> mappingStrategy, Scenario scenario) {
	super(scenario);
	this.mappingStrategy = mappingStrategy;
    }

    public abstract A averageRunResultMapper(List<R> runParameters, Time duration);

    public abstract A toMeanMapper(List<Tuple<List<R>, Time>> runParameters);

}
