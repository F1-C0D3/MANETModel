package de.results;

import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.network.LinkQuality;

public abstract class ParameterRecorder<W extends LinkQuality, R extends RunResult> {

    public Supplier<R> supplier;
    private Scenario scenario;

    public ParameterRecorder(Supplier<R> supplier, Scenario scenario) {
	this.supplier = supplier;
	this.scenario = scenario;
    }

    public ParameterRecorder(Supplier<R> supplier) {
	this.supplier = supplier;
    }

    public abstract R toResultFormatR(int n1Id, int n2Id, int lId, W w);

    public abstract R toMean(List<List<R>> runs);

    public void setScenario(Scenario scenario) {
	this.scenario = scenario;
    }

    public Scenario getScenario() {
	return scenario;
    }
}
