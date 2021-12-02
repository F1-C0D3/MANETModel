package de.manetmodel.results;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.units.Time;

public abstract class RunResultRecorder<I extends ResultParameter, A extends ResultParameter, N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> extends ResultRecorder{

    protected RunResultContent<I, A> runResultContent;

    public RunResultRecorder(RunResultContent<I, A> runResultContent) {
	this.runResultContent = runResultContent;
    }

    protected abstract void recordIndividual(MANET<N, L, W, F> manet, Time runDuration);

    protected abstract void recordAverage(MANET<N, L, W, F> manet);

    public RunResultContent<I, A> getRunResultContent() {
	return this.runResultContent;
    }
}
