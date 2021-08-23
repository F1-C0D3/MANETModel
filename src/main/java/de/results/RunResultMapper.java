package de.results;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.scenarios.Scenario;

public abstract class RunResultMapper<R extends ResultParameter> extends ResultMapper<R> {
    protected Supplier<R> resultParameterSupplier;
    ColumnPositionMappingStrategy<R> mappingStrategy;

    public RunResultMapper(Supplier<R> resultParameterSupplier, ColumnPositionMappingStrategy<R> mappingStrategy,
	    Scenario scenario) {
	super(resultParameterSupplier, mappingStrategy, scenario);
	this.mappingStrategy = mappingStrategy;
	this.resultParameterSupplier = resultParameterSupplier;
    }

    public ColumnPositionMappingStrategy<R> getMappingStrategy() {
	return mappingStrategy;
    }

    public void setMappingStrategy(ColumnPositionMappingStrategy<R> mappingStrategy) {
	this.mappingStrategy = mappingStrategy;
    }

    public abstract <W extends LinkQuality, N extends Node> R individualRunResultMapper(N source, N sink, int lId, W w);

}
