package de.manetmodel.results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.jgraphlib.util.Tuple;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.results.CSVExporter.RecordType;
import de.manetmodel.units.Time;

public class MANETRunResultRecorder<I extends ResultParameter, A extends ResultParameter, N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>>
	extends RunResultRecorder<I, A, N, L, W, F> {

    private String resultFileName;
    RunResultMapper<I, A, N, L, W> resultMapper;

    public MANETRunResultRecorder(String resultFileName, RunResultMapper<I, A, N, L, W> resultMapper, int run) {
	super(new RunResultContent<I, A>(run));
	this.resultFileName = resultFileName;
	this.resultMapper = resultMapper;

    }

    public void recordIndividual(MANET<N, L, W, F> manet, Time runDuration) {

	if (manet.getUtilization().get() > 0) {
	    CSVExporter exporter = new CSVExporter(resultFileName, RecordType.individualRun);
	    List<I> individualRun = new ArrayList<I>();

	    for (L link : manet.getEdges()) {

		Tuple<N, N> sourceAndSink = manet.getVerticesOf(link);

		I runResult = resultMapper.individualRunResultMapper(sourceAndSink.getFirst(),
			sourceAndSink.getSecond(), link);

		individualRun.add(runResult);
	    }

	    this.runResultContent.setIndividualResultContent(individualRun);
	    this.runResultContent.setRunDuration(runDuration);

	    exporter.write(individualRun, resultMapper.getIndividualMappingStrategy(), resultMapper.getScenario(),
		    Integer.toString(runResultContent.getCurrentRun()));
	}
    }

    public void recordAverage(MANET<N, L, W, F> manet) {
	List<I> individualResultContent = this.runResultContent.getIndividualResultContent();
	Time runDuration = runResultContent.getRunDuration();

	if (individualResultContent != null && runDuration != null) {

	    CSVExporter averageRunExporter = new CSVExporter(resultFileName, RecordType.averageRun);

	    A averageRunResult = resultMapper.averageRunResultMapper(individualResultContent, manet.getFlows(),
		    runDuration);

	    runResultContent.setAverageResultContent(averageRunResult);

	    averageRunExporter.write(Arrays.asList(averageRunResult), resultMapper.getAverageMappingStrategy(),
		    resultMapper.getScenario(), Integer.toString(runResultContent.getCurrentRun()));
	}
    }

}
