package de.manetmodel.results;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.scenarios.Scenario;

public abstract class ResultMapper<M extends ResultParameter> {
    protected Scenario scenario;

    public ResultMapper(Scenario scenario) {
	this.scenario = scenario;
    }

    public abstract ColumnPositionMappingStrategy<M> getMappingStrategy();

    public abstract void setMappingStrategy(ColumnPositionMappingStrategy<M> mappingStrategy);

    public Scenario getScenario() {
	return scenario;
    }
}
