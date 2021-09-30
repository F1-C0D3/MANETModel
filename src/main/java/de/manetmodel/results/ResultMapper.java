package de.manetmodel.results;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.scenarios.Scenario;

public abstract class ResultMapper<M extends ResultParameter> {
    protected Scenario scenario;
    protected ColumnPositionMappingStrategy<M> mappingStrategy;

    public ResultMapper(Scenario scenario,ColumnPositionMappingStrategy<M> mappingStrategy ) {
	this.scenario = scenario;
	this.mappingStrategy = mappingStrategy;
    }

    public  ColumnPositionMappingStrategy<M> getMappingStrategy(){
	return this.mappingStrategy;
    }

    public void setMappingStrategy(ColumnPositionMappingStrategy<M> mappingStrategy) {
	this.mappingStrategy=mappingStrategy;
    }

    public Scenario getScenario() {
	return scenario;
    }
}
