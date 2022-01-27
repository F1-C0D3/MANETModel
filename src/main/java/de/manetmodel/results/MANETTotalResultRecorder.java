package de.manetmodel.results;

import java.util.Arrays;
import java.util.List;

import de.manetmodel.results.CSVExporter.RecordType;
import de.manetmodel.scenarios.Scenario;

public class MANETTotalResultRecorder<T extends ResultParameter, I extends IndividualRunResultParameter, A extends AverageRunResultParameter>
	extends TotalResultRecorder<T, I, A> {

    private TotalResultMapper<T, I, A> mapper;

    public MANETTotalResultRecorder(Scenario scenario, TotalResultMapper<T, I, A> mapper,
	    List<RunResultContent<I, A>> runResultContents) {
	super(scenario, runResultContents);
	this.mapper = mapper;

    }
    
    public void addRunResultContent( List<RunResultContent<I, A>> runResultContents) {
	
    }

    @Override
    public void finish() {
	if (super.resultContents.size() > 0) {
	    CSVExporter exporter = new CSVExporter(directoryStructure, RecordType.total);
	    T result = mapper.toalMapper(super.resultContents);
	    exporter.write(Arrays.asList(result), mapper.getTotalMappingStrategy(),outputFilename());
	}

    }
    
    @Override
    protected String outputFilename() {
	// TODO Auto-generated method stub
	StringBuffer outputBuffer = new StringBuffer();
	outputBuffer
		.append(String.format("Seed=%d_flows=%d_oU=%d_", scenario.getNumFlows(), scenario.getOverUtilizePercentage()));
	outputBuffer.append(super.outputFilename());
	return outputBuffer.toString();

    }

}
