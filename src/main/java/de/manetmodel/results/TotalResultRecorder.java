package de.manetmodel.results;

import java.util.List;

public abstract class TotalResultRecorder<T extends ResultParameter, I extends IndividualRunResultParameter,A extends AverageRunResultParameter> {

    protected String resultFileName;

    public TotalResultRecorder(String resultFileName) {
	this.resultFileName = resultFileName;
    }

    public abstract void finish(List<RunResultContent<I, A>> runResultContents);
}
