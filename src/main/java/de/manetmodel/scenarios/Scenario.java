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

public class Scenario {

    protected int numFlows;
    protected int numNodes;
    private String indivdualName;

    public Scenario(String indivdualName, int flows, int nodes) {
	this.indivdualName = indivdualName;
	this.numNodes = nodes;
	this.numFlows = flows;
    }

    public Scenario(int flows, int nodes) {
	this.indivdualName = new String();
	this.numNodes = nodes;
	this.numFlows = flows;
    }

    public Scenario() {
	this.numFlows = -1;
	this.numNodes = -1;
    }

    public String getScenarioName() {
	return indivdualName;
    }

    public int getNumNodes() {
	return numNodes;
    }

    public List<Flow<Node, Link<LinkQuality>, LinkQuality>> generateFlows(
	    MANET<Node, Link<LinkQuality>, LinkQuality, Flow<Node, Link<LinkQuality>, LinkQuality>> manet, int runs) {
	return null;
    }

    public Path getResultFolder() {
	return Paths.get(new StringBuffer().append(indivdualName).toString());
    }

    public String getResultFile() {
	return new StringBuffer().append(indivdualName).append("_Flows_").append(numFlows).append("_")
		.append("MeanTransmissionRate").append("_Nodes_").append(numNodes).toString();
    }

}
