package de.manetmodel.scenarios;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Tuple;


public class Scenario {

    protected int numFlows;
    protected int numNodes;
    private String indivdualName;
    protected int numRuns;

    public Scenario(String indivdualName, int numFlows, int numNodes, int runs) {
	this.numRuns = runs;
	this.indivdualName = indivdualName;
	this.numNodes = numNodes;
	this.numFlows = numFlows;
    }

    public Scenario(int flows, int nodes, int runs) {
	this.numRuns = runs;
	this.indivdualName = new String();
	this.numNodes = nodes;
	this.numFlows = flows;
    }

    public Scenario(int runs) {
	this.numRuns = runs;
	this.numFlows = -1;
	this.numNodes = -1;
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
