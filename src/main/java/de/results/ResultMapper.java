package de.results;

import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.scenarios.Scenario;

public abstract class ResultMapper<R> {
    protected Supplier<R> resultParameterSupplier;
    protected ColumnPositionMappingStrategy<R> mappingStrategy;
    Scenario scenario;

    public ResultMapper(Supplier<R> resultParameterSupplier, ColumnPositionMappingStrategy<R> mappingStrategy,
	    Scenario scenario) {
	this.resultParameterSupplier = resultParameterSupplier;
	this.mappingStrategy = mappingStrategy;
	this.scenario = scenario;
    }


    public Scenario getScenario() {
	return scenario;
    }
}
