package de.results;

import java.util.List;
import java.util.function.Supplier;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;

public abstract class ParameterRecorder<L extends Link<W>, W extends LinkQuality, R extends RunResult> {
    Supplier<R> supplier;

    public ParameterRecorder(Supplier<R> supplier) {
	this.supplier = supplier;
    }

    public abstract R toResultFormatR(L l);

    public abstract R toMean(List<List<R>> runs);
}
