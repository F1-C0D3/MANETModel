package de.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.unit.Time;
import de.manetmodel.scenarios.Scenario;

public abstract class AverageResultMapper<K extends ResultParameter> extends ResultMapper<K> {

    public AverageResultMapper(Supplier<K> resultParameterSupplier, ColumnPositionMappingStrategy<K> mappingStrategy,
	    Scenario scenario) {
	super(resultParameterSupplier, mappingStrategy, scenario);
	this.mappingStrategy = mappingStrategy;
	this.resultParameterSupplier = resultParameterSupplier;
    }

    public ColumnPositionMappingStrategy<K> getMappingStrategy() {
	return mappingStrategy;
    }

    public void setMappingStrategy(ColumnPositionMappingStrategy<K> mappingStrategy) {
	this.mappingStrategy = mappingStrategy;
    }

    public abstract <R extends RunResultParameter> K averageRunResultMapper(List<R> runParameters, Time duration);

    public abstract <R extends RunResultParameter> K toMeanMapper(List<Tuple<List<R>, Time>> runParameters);

}
