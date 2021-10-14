package de.manetmodel.results;

import java.util.Arrays;
import java.util.List;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.results.CSVExporter.RecordType;

public class MANETTotalResultRecorder<T extends ResultParameter, I extends IndividualRunResultParameter,A extends AverageRunResultParameter>
	extends TotalResultRecorder<T,I,A> {

    private TotalResultMapper<T,I,A> mapper;
    public MANETTotalResultRecorder(String resultFileName, TotalResultMapper<T,I,A> mapper) {
	super(resultFileName);
	this.mapper=mapper;

    }

    @Override
    public void finish(List<RunResultContent<I, A>> runResultContents) {

	CSVExporter exporter = new CSVExporter(resultFileName, RecordType.total);
	T result = mapper.toalMapper(runResultContents);
	exporter.write(Arrays.asList(result), mapper.getTotalMappingStrategy(), mapper.getScenario(), "total");
//	
    }
}
