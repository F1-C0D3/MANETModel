package de.results;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public class ResultRecorder<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>, R extends RunResult> {

    List<List<R>> resultRuns;
    ParameterRecorder<L, W, R> runRecorder;
    List<String> scalarIdentifiers;
    LinkedList<Double> scalarResults;
    CSVExporter<R> exporter;

    public ResultRecorder(Class<? extends MANET<N, L, W, F>> resultClass, ParameterRecorder<L, W, R> runRecorder) {
	this.runRecorder = runRecorder;
	this.resultRuns = new ArrayList<List<R>>();
	exporter = new CSVExporter(resultClass.getSimpleName());
    }

    public void recordRun(MANET<N, L, W, F> manet) {
	List<R> individualRun = new ArrayList<R>();
	for (L l : manet.getEdges()) {
	    runRecorder.toResultFormatR(l);
	    individualRun.add(runRecorder.toResultFormatR(l));
	}
	resultRuns.add(individualRun);
	exporter.write(new StringBuffer().append(resultRuns.size() - 1).toString(), individualRun);

    }

    public void finish() {
	R mean = runRecorder.toMean(resultRuns);
	List<R> r = new ArrayList<R>();
	r.add(mean);
	exporter.write("average", r);
    }

    private double maxDeviation(Double[] values) {
	double current = 0d;
	for (int i = 0; i < values.length; i++) {
	    if (values[i] > current)
		current = values[i];
	}
	return current;
    }

    private double minDeviation(Double[] values) {
	double current = Double.POSITIVE_INFINITY;
	for (int i = 0; i < values.length; i++) {
	    if (values[i] < current)
		current = values[i];
	}
	return current;
    }
}
