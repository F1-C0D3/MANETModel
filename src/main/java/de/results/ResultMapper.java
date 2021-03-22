package de.results;

import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.network.LinkQuality;

public class ResultMapper<W extends LinkQuality, R extends ResultParameter> {

    public Supplier<R> supplier;
    private Scenario scenario;

    public ResultMapper(Supplier<R> supplier, Scenario scenario) {
	this.supplier = supplier;
	this.scenario = scenario;
    }

    public ResultMapper(Supplier<R> supplier) {
	this.supplier = supplier;
    }

    public R toResultFormatR(int n1Id, int n2Id, int lId, W w) {
	R r = supplier.get();
	r.setlId(lId);
	r.setN1Id(n1Id);
	r.setN2Id(n2Id);
	return r;
    }

    public R toMean(List<List<R>> runs) {
	return null;
    }

    public void setScenario(Scenario scenario) {
	this.scenario = scenario;
    }

    public Scenario getScenario() {
	return scenario;
    }
}
