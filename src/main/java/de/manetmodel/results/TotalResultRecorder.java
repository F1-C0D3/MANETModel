package de.manetmodel.results;

import java.util.ArrayList;
import java.util.List;

public abstract class TotalResultRecorder<T extends ResultParameter, I extends IndividualRunResultParameter, A extends AverageRunResultParameter> {

    protected String resultFileName;
    protected List<RunResultContent<I, A>> resultContents;

    public TotalResultRecorder(String resultFileName, List<RunResultContent<I, A>> runResultContents) {
	this.resultFileName = resultFileName;

	resultContents = new ArrayList<RunResultContent<I, A>>();

	for (RunResultContent<I, A> runResultContent : runResultContents) {
	
	    if (runResultContent.doRecord()) {
		resultContents.add(runResultContent);
	    }
	}
    }

    public abstract void finish();
}
