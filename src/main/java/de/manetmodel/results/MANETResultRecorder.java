package de.manetmodel.results;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.util.Tuple;
import de.manetmodel.elements.Link;
import de.manetmodel.elements.LinkQuality;
import de.manetmodel.elements.Node;
import de.manetmodel.network.MANET;
import de.manetmodel.network.unit.Time;
import de.manetmodel.results.CSVExporter.RecordType;

public class MANETResultRecorder<W extends LinkQuality, R extends RunResultParameter,A extends AverageResultParameter> {

    private String resultFileName;
    private List<Tuple<List<R>, Time>> resultRuns;

    public MANETResultRecorder(String resultFileName) {
	this.resultFileName = resultFileName;
	this.resultRuns = new ArrayList<Tuple<List<R>, Time>>();
    }

    public  void recordRun(
	    MANET<W> manet, RunResultMapper<W,R> resultMapper, Time runDuration) {
	recordIndividualRun(manet, resultMapper, runDuration);

    }

    private <N extends Node,L extends Link<W>, W extends LinkQuality,F extends Flow<N,L,W>> void recordIndividualRun(
	    MANET<N,L,W,F> manet, RunResultMapper<W,R> resultMapper, Time runDuration) {
	CSVExporter exporter = new CSVExporter(resultFileName, RecordType.individualRun);
	List<R> individualRun = new ArrayList<R>();
	for (Link<W> link : manet.getEdges()) {
	    Tuple<Node, Node> sourceAndSink = manet.getVerticesOf(link);
	    R runResult = resultMapper.individualRunResultMapper(sourceAndSink.getFirst(), sourceAndSink.getSecond(),
		    link );
	    individualRun.add(runResult);
	}

	resultRuns.add(new Tuple<List<R>, Time>(individualRun, runDuration));
	exporter.write(individualRun, resultMapper.getMappingStrategy(), resultMapper.getScenario(),
		new StringBuffer().append(resultRuns.size() - 1).toString());
    }

    public  void finish(AverageResultMapper<A,R> resultMapper) {
	this.recordAverageRun(resultMapper);
	CSVExporter exporter = new CSVExporter(resultFileName, RecordType.total);
	A mean = resultMapper.toMeanMapper(resultRuns);
	List<A> r = new ArrayList<A>();
	r.add(mean);
	exporter.write(r, resultMapper.getMappingStrategy(), resultMapper.getScenario(), "total");
    }

    private void recordAverageRun(AverageResultMapper<A,R> resultMapper) {
	CSVExporter averageRunExporter = new CSVExporter(resultFileName, RecordType.averageRun);
	List<A> resultList = new ArrayList<A>();
	for (Tuple<List<R>, Time> run : resultRuns) {
	    A averageRunResult = resultMapper.averageRunResultMapper(run.getFirst(), run.getSecond());
	    resultList.add(averageRunResult);
	}

	averageRunExporter.write(resultList, resultMapper.getMappingStrategy(), resultMapper.getScenario(),
		new StringBuffer().append(resultRuns.size() - 1).append("_").append("average").toString());
    }

}
