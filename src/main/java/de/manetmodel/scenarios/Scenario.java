package de.manetmodel.scenarios;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Triple;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.DataRate.DataRateRange;

public abstract class Scenario {

    protected int numFlows;
    protected int numNodes;
    private String indivdualName;
    protected List<Tuple<Integer, Integer>> flowSourceTargetPairs;

    public Scenario(String indivdualName, int numFlows, int numNodes, int runs) {
	this.indivdualName = indivdualName;
	this.numNodes = numNodes;
	this.numFlows = numFlows;
	this.flowSourceTargetPairs = new ArrayList<Tuple<Integer, Integer>>(numFlows);

	this.generateSourceTargetPairs(runs);
    }

    public Scenario(int flows, int nodes, int runs) {
	this.indivdualName = new String();
	this.numNodes = nodes;
	this.numFlows = flows;

	this.flowSourceTargetPairs = new ArrayList<Tuple<Integer, Integer>>(numFlows);

	this.generateSourceTargetPairs(runs);
    }

    public Scenario(int runs) {
	this.numFlows = -1;
	this.numNodes = -1;

	this.flowSourceTargetPairs = new ArrayList<Tuple<Integer, Integer>>(numFlows);

	this.generateSourceTargetPairs(runs);
    }

    protected void generateSourceTargetPairs(int runs) {

	RandomNumbers instance = RandomNumbers.getInstance(runs);

	for (int i = 0; i < numFlows; i++) {
	    flowSourceTargetPairs
		    .add(new Tuple<Integer, Integer>(instance.getRandom(0, numNodes), instance.getRandom(0, numNodes)));
	}
    }

    public String getScenarioName() {
	return indivdualName;
    }

    public int getNumNodes() {
	return numNodes;
    }

    public Path getResultFolder() {
	return Paths.get(new StringBuffer().append(indivdualName).toString());
    }

    public String getResultFile() {
	return new StringBuffer().append(indivdualName).append("_Flows_").append(numFlows).append("_")
		.append("MeanTransmissionRate").append("_Nodes_").append(numNodes).toString();
    }

}
