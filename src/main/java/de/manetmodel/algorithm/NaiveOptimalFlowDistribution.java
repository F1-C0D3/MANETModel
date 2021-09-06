package de.manetmodel.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.util.Log;
import de.jgraphlib.util.Log.HeaderLevel;
import de.jgraphlib.util.Timer;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;

public class NaiveOptimalFlowDistribution<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> {

    Log log;
    Timer timer;
    List<List<List<Integer>>> possiblePaths;
    int[][] pathIndexes;
    List<List<F>> feasibleDistributions;
    long count = 0;
    long combinations = 1;
    int statusMillis = 1000;

    public NaiveOptimalFlowDistribution() {	
	log = new Log();
	timer = new Timer();
	feasibleDistributions = new ArrayList<List<F>>();
    }

    public <M extends MANET<N, L, W, F>> void initialize(M manet) {

	possiblePaths = new ArrayList<List<List<Integer>>>();

	log.infoHeader(HeaderLevel.h1, String.format("%s", getClass().getSimpleName()));
	log.infoHeader(HeaderLevel.h1, "(1) Initialization");
	log.infoHeader(HeaderLevel.h2, "(1.1) Generate paths");

	for (F flow : manet.getFlows()) {

	    log.info(String.format("Generate paths for flow (source: %d, target: %d, datarate: %s)",
		    flow.getSource().getID(), flow.getTarget().getID(), flow.getDataRate().toString()));

	    possiblePaths.add(manet.getAllPathsByVertexIDs(flow.getSource(), flow.getTarget()));

	    log.info(String.format("\t ~> Generated %d possible paths for flow (source: %d, target: %d, datarate: %s)",
		    possiblePaths.get(possiblePaths.size() - 1).size(), flow.getSource().getID(),
		    flow.getTarget().getID(), flow.getDataRate().toString()));

	    combinations *= possiblePaths.get(possiblePaths.size() - 1).size();
	}

	log.info(String.format(
		"~> Overall combination options of %d (possible flow distribution represenations to check on the model)",
		combinations));

	pathIndexes = new int[possiblePaths.size()][];

	for (int i = 0; i < possiblePaths.size(); i++) {
	    pathIndexes[i] = new int[possiblePaths.get(i).size()];
	    Arrays.setAll(pathIndexes[i], p -> p);
	}
    }

    public <M extends MANET<N, L, W, F>> List<List<F>> generateFeasibleDistributions(M manet) {

	initialize(manet);

	log.infoHeader(HeaderLevel.h1, "(2) Computation");

	timer.start();

	cartesianProduct(pathIndexes, (int[] factor) -> {

	    // Update flows
	    for (int i = 0; i < factor.length; i++) {
		for (F flow : manet.getFlows()) {
		    List<Integer> possiblePath = possiblePaths.get(i).get(factor[i]);
		    Path<N, L, W> path = manet.createPath(manet.getVertices(possiblePath));
		    flow.update(path);
		}
	    }

	    // Deploy flows
	    for (F flow : manet.getFlows())
		manet.deployFlow(flow);

	    // Check if deployment is feasible
	    if (!manet.isOverutilized()) {
		feasibleDistributions.add(manet.getFlows());
		// EVALUATE
	    }

	    // Reset
	    manet.undeployFlows();

	    count++;

	    if (timer.getTime().getMillis() > statusMillis) {
		timer.reset();
		log.info(String.format("%d/%d (%.2f %%)", count, combinations,
			((double) count / (double) combinations) * 100));
	    }
	});

	log.infoHeader(HeaderLevel.h2, "(3) Evaluation");
	log.info(String.format("Given flows allow %d feasible distributions on the model",
		feasibleDistributions.size()));

	return feasibleDistributions;
    }

    public static void cartesianProduct(int[][] sets, Consumer<int[]> factorCallback) {
	int solutions = 1;
	for (int i = 0; i < sets.length; solutions *= sets[i].length, i++);
	for (int i = 0; i < solutions; i++) {
	    int j = 1;
	    int[] factor = new int[sets.length];
	    for (int k = 0; k < sets.length; k++) {
		factor[k] = sets[k][(i / j) % sets[k].length];
		j *= sets[k].length;
	    }
	    factorCallback.accept(factor);
	}
    }
}
