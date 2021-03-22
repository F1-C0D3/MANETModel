package de.runprovider;

import java.util.List;
import java.util.concurrent.Callable;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public class ExecutionCallable<F extends Flow<N,L,W>,N extends Node, L extends Link<W>, W extends LinkQuality> implements Callable<List<F>> {

    @Override
    public List<F> call() throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

}
