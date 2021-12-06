package de.manetmodel.results;

import java.nio.file.Path;
import java.nio.file.Paths;
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
import de.manetmodel.scenarios.Scenario;
import de.manetmodel.units.Time;

public class MANETRunResultRecorder<I extends ResultParameter, A extends ResultParameter, N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>>
	extends RunResultRecorder<I, A, N, L, W, F> {

    private RunResultMapper<I, A, N, L, W> resultMapper;

    public MANETRunResultRecorder(Scenario scenario, RunResultMapper<I, A, N, L, W> resultMapper, int run) {
	super(scenario, new RunResultContent<I, A>(run));
	this.resultMapper = resultMapper;

    }

    public void recordIndividual(MANET<N, L, W, F> manet, Time runDuration) {

	if (manet.getUtilization().get() > 0) {
	    List<Path> individualDirectoryPath = new ArrayList<Path>(directoryStructure);
	    CSVExporter exporter = new CSVExporter(individualDirectoryPath, RecordType.individualRun);
	    List<I> individualRun = new ArrayList<I>();

	    for (L link : manet.getEdges()) {

		Tuple<N, N> sourceAndSink = manet.getVerticesOf(link);

		I runResult = resultMapper.individualRunResultMapper(sourceAndSink.getFirst(),
			sourceAndSink.getSecond(), link);

		individualRun.add(runResult);
	    }

	    this.runResultContent.setIndividualResultContent(individualRun);
	    this.runResultContent.setRunDuration(runDuration);

	    exporter.write(individualRun, resultMapper.getIndividualMappingStrategy(), outputFilename());
	}
    }

    public void recordAverage(MANET<N, L, W, F> manet) {
	List<I> individualResultContent = this.runResultContent.getIndividualResultContent();
	Time runDuration = runResultContent.getRunDuration();
	int currentRun = runResultContent.getCurrentRun();

	if (individualResultContent != null && runDuration != null) {

	    List<Path> averageDirectoryPath = new ArrayList<Path>(directoryStructure);
	    CSVExporter averageRunExporter = new CSVExporter(averageDirectoryPath, RecordType.averageRun);

	    A averageRunResult = resultMapper.averageRunResultMapper(individualResultContent, manet.getFlows(),
		    runDuration, currentRun);

	    runResultContent.setAverageResultContent(averageRunResult);

	    averageRunExporter.write(Arrays.asList(averageRunResult), resultMapper.getAverageMappingStrategy(),
		    outputFilename());
	}
    }

    @Override
    protected String outputFilename() {
	// TODO Auto-generated method stub
	StringBuffer outputBuffer = new StringBuffer();
	outputBuffer.append(String.format("Run=%d_flows=%d_oU=%d_", runResultContent.getCurrentRun(),
		scenario.getNumFlows(), scenario.getOverUtilizePercentage()));
	outputBuffer.append(super.outputFilename());
	return outputBuffer.toString();

    }

}
