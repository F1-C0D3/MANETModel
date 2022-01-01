package de.manetmodel.results;

import java.util.ArrayList;
import java.util.List;

import de.manetmodel.scenarios.Scenario;

public abstract class TotalResultRecorder<T extends ResultParameter, I extends IndividualRunResultParameter, A extends AverageRunResultParameter> extends ResultRecorder{

    protected List<RunResultContent<I, A>> resultContents;

    public TotalResultRecorder(Scenario scenario, List<RunResultContent<I, A>> runResultContents) {
	super(scenario);

	resultContents = new ArrayList<RunResultContent<I, A>>();

	for (RunResultContent<I, A> runResultContent : runResultContents) {
	
	    if (runResultContent.doRecord()) {
		resultContents.add(runResultContent);
	    }
	}
    }

    public abstract void finish();
}
