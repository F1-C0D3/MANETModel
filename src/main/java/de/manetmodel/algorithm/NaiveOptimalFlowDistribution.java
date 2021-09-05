package de.manetmodel.algorithm;

import java.util.ArrayList;
import java.util.List;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public class NaiveOptimalFlowDistribution<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> {

    public <M extends MANET<N, L, W, F>> List<List<F>> getFeasibleDistributions(M manet) {

	List<List<F>> allPaths = new ArrayList<List<F>>();
	List<List<F>> feasibileSolutions = new ArrayList<List<F>>();

	for (F flow : manet.getFlows()) {
	    allPaths.add(manet.getAllPaths(flow));
	    System.out.println(String.format("Generated %d possible paths for flow %d ~> %d",
		    allPaths.get(allPaths.size() - 1).size(), flow.getSource().getID(), flow.getTarget().getID()));
	}

	List<List<F>> cartesianProducts = cartesianProduct(allPaths);

	System.out.println(String.format("Cartesian product (all possible combination options) over all paths is %d",
		cartesianProducts.size()));

	for (int i = 0; i < cartesianProducts.size(); i++) {

	    for (F flow : cartesianProducts.get(i)) {
		manet.deployFlow(flow);
	    }

	    if (!manet.isOverutilized()) {
		feasibileSolutions.add(cartesianProducts.get(i));
	    }

	    manet.clearFlows();
	    manet.undeployFlows();
	}

	System.out.println(String.format("Found %d feasible solutions", feasibileSolutions.size()));

	return feasibileSolutions;
    }

    private <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
	List<List<T>> resultLists = new ArrayList<List<T>>();
	if (lists.size() == 0) {
	    resultLists.add(new ArrayList<T>());
	    return resultLists;
	} else {
	    List<T> firstList = lists.get(0);
	    List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
	    for (T condition : firstList) {
		for (List<T> remainingList : remainingLists) {
		    ArrayList<T> resultList = new ArrayList<T>();
		    resultList.add(condition);
		    resultList.addAll(remainingList);
		    resultLists.add(resultList);
		}
	    }
	}
	return resultLists;
    }
}
