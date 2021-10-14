package de.manetmodel.results;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.scenarios.Scenario;

public abstract class ResultMapper {
    protected Scenario scenario;

    public ResultMapper(Scenario scenario ) {
	this.scenario = scenario;
    }


    public Scenario getScenario() {
	return scenario;
    }
}
