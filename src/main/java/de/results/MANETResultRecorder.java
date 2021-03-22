package de.results;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public class MANETResultRecorder<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>, R extends ResultParameter> {

    List<List<R>> resultRuns;
    RunResultMapper<W, R> runResultMapper;
    List<String> scalarIdentifiers;
    LinkedList<Double> scalarResults;
    ColumnPositionMappingStrategy<R> mappingStrategy;
    String resultFileName;

    public MANETResultRecorder(String resultFileName, RunResultMapper<W, R> runResultmapper,
	    ColumnPositionMappingStrategy<R> mappingStrategy) {
	this.resultRuns = new ArrayList<List<R>>();
	this.mappingStrategy = mappingStrategy;
	this.resultFileName = resultFileName;
    }

    public MANETResultRecorder(String resultFileName, ColumnPositionMappingStrategy<R> mappingStrategy) {
	this.resultRuns = new ArrayList<List<R>>();
	this.resultFileName = resultFileName;
	this.mappingStrategy = mappingStrategy;
    }

    public void setRunRecorder(RunResultMapper<W, R> runResultMapper) {
	this.runResultMapper = runResultMapper;
    }

    public void recordRun(MANET<N, L, W, F> manet, RunResultMapper<W, R> runResultMapper) {
	CSVExporter<R> exporter = new CSVExporter<R>(resultFileName);
	List<R> individualRun = new ArrayList<R>();
	for (L l : manet.getEdges()) {
	    Tuple<N, N> sourceAndSink = manet.getVerticesOf(l);
	    R runResult = runResultMapper.singleRunResultMapper(sourceAndSink.getFirst().getID(),
		    sourceAndSink.getSecond().getID(), l.getID(), l.getWeight());
	    individualRun.add(runResult);
	}
	resultRuns.add(individualRun);
	mappingStrategy.setColumnMapping("lId", "n1Id", "n2Id", "overUtilization", "utilization", "isPathParticipant",
		"connectionStability");
	exporter.write(individualRun, mappingStrategy, runResultMapper.getScenario(),
		new StringBuffer().append(resultRuns.size() - 1).toString());

    }

    public void finish() {

	CSVExporter<R> exporter = new CSVExporter<R>(resultFileName);
	R mean = this.runResultMapper.toMeanMapper(resultRuns);
	List<R> r = new ArrayList<R>();
	r.add(mean);
	mappingStrategy.setColumnMapping("overUtilization", "utilization", "connectionStability");
	exporter.write(r, mappingStrategy, runResultMapper.getScenario(), "average");
    }

//    private double maxDeviation(Double[] values) {
//	double current = 0d;
//	for (int i = 0; i < values.length; i++) {
//	    if (values[i] > current)
//		current = values[i];
//	}
//	return current;
//    }
//
//    private double minDeviation(Double[] values) {
//	double current = Double.POSITIVE_INFINITY;
//	for (int i = 0; i < values.length; i++) {
//	    if (values[i] < current)
//		current = values[i];
//	}
//	return current;
//    }

}
