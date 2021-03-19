package de.results;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public class ResultRecorder<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>, R extends RunResult> {

    List<List<R>> resultRuns;
    ParameterRecorder<W, R> runRecorder;
    List<String> scalarIdentifiers;
    LinkedList<Double> scalarResults;
    CSVExporter<R> exporter;
    ColumnPositionMappingStrategy<R> mappingStrategy;

    public ResultRecorder(String  resultFileName, ParameterRecorder<W, R> runRecorder,
	    ColumnPositionMappingStrategy<R> mappingStrategy) {
	this.runRecorder = runRecorder;
	this.resultRuns = new ArrayList<List<R>>();
	this.exporter = new CSVExporter<R>(resultFileName);
	this.mappingStrategy = mappingStrategy;
    }

    public void recordRun(MANET<N, L, W, F> manet) {
	List<R> individualRun = new ArrayList<R>();
	for (L l : manet.getEdges()) {
	    Tuple<N, N> sourceAndSink = manet.getVerticesOf(l);
	    R runResult = runRecorder.toResultFormatR(sourceAndSink.getFirst().getID(),
		    sourceAndSink.getSecond().getID(), l.getID(), l.getWeight());
	    individualRun.add(runResult);
	}
	resultRuns.add(individualRun);
	mappingStrategy.setColumnMapping("lId", "n1Id", "n2Id", "overUtilization", "utilization", "isPathParticipant",
		"connectionStability");
	exporter.write(individualRun, mappingStrategy, runRecorder.getScenario(),
		new StringBuffer().append(resultRuns.size() - 1).toString());

    }

    public void finish() {
	R mean = runRecorder.toMean(resultRuns);
	List<R> r = new ArrayList<R>();
	r.add(mean);
	mappingStrategy.setColumnMapping("overUtilization", "utilization", "connectionStability");
	exporter.write(r, mappingStrategy, runRecorder.getScenario(), "average");
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
