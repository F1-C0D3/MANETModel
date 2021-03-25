package de.results;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.network.unit.Time;
import de.results.CSVExporter.RecordType;

public class MANETResultRecorder<R extends RunResultParameter> {

    private String resultFileName;
    private List<Tuple<List<R>, Time>> resultRuns;

    public MANETResultRecorder(String resultFileName) {
	this.resultFileName = resultFileName;
	this.resultRuns = new ArrayList<Tuple<List<R>, Time>>();
    }

    public <N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> void recordRun(
	    MANET<N, L, W, F> manet, RunResultMapper<R> resultMapper, Time runDuration) {
	recordIndividualRun(manet, resultMapper, runDuration);

    }

    private <N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> void recordIndividualRun(
	    MANET<N, L, W, F> manet, RunResultMapper<R> resultMapper, Time runDuration) {
	CSVExporter exporter = new CSVExporter(resultFileName, RecordType.individualRun);
	List<R> individualRun = new ArrayList<R>();
	for (L l : manet.getEdges()) {
	    Tuple<N, N> sourceAndSink = manet.getVerticesOf(l);
	    R runResult = resultMapper.individualRunResultMapper(sourceAndSink.getFirst().getID(),
		    sourceAndSink.getSecond().getID(), l.getID(), l.getWeight());
	    individualRun.add(runResult);
	}

	resultRuns.add(new Tuple<List<R>, Time>(individualRun, runDuration));
	exporter.write(individualRun, resultMapper.getMappingStrategy(), resultMapper.getScenario(),
		new StringBuffer().append(resultRuns.size() - 1).toString());
    }

    public <K extends AverageResultParameter> void finish(AverageResultMapper<K> resultMapper) {
	this.recordAverageRun(resultMapper);
	CSVExporter exporter = new CSVExporter(resultFileName, RecordType.total);
	List<R> tmp = new ArrayList<R>();
	K mean = resultMapper.toMeanMapper(resultRuns);
	List<K> r = new ArrayList<K>();
	r.add(mean);
	exporter.write(r, resultMapper.getMappingStrategy(), resultMapper.getScenario(), "total");
    }

    private <K extends AverageResultParameter> void recordAverageRun(AverageResultMapper<K> resultMapper) {
	CSVExporter averageRunExporter = new CSVExporter(resultFileName, RecordType.averageRun);
	List<K> resultList = new ArrayList<K>();
	for (Tuple<List<R>, Time> run : resultRuns) {
	    K averageRunResult = resultMapper.averageRunResultMapper(run.getFirst(), run.getSecond());
	    resultList.add(averageRunResult);
	}

	averageRunExporter.write(resultList, resultMapper.getMappingStrategy(), resultMapper.getScenario(),
		new StringBuffer().append(resultRuns.size() - 1).append("_").append("average").toString());
    }

}
