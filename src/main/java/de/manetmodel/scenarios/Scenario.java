package de.manetmodel.scenarios;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Scenario {

    private String indivdualName;

    protected int overUtilizePercentage;
    protected int numFlows;
    protected int numNodes;
    protected int numRuns;

    public Scenario(String indivdualName, int numFlows, int numNodes, int runs, int overUtilizePercentage) {
	this.numRuns = runs;
	this.indivdualName = indivdualName;
	this.numNodes = numNodes;
	this.numFlows = numFlows;
	this.overUtilizePercentage = overUtilizePercentage;
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

    public int getOverUtilizePercentage() {
	return overUtilizePercentage;
    }

    public void setOverUtilizePercentage(int overUtilizePercentage) {
	this.overUtilizePercentage = overUtilizePercentage;
    }

    public int getNumFlows() {
	return numFlows;
    }

    public void setNumFlows(int numFlows) {
	this.numFlows = numFlows;
    }

    public int getNumRuns() {
	return numRuns;
    }

    public void setNumRuns(int numRuns) {
	this.numRuns = numRuns;
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

	return String.format("%s_flows=%d_overUtilization=%d_totalRuns=%d", this.indivdualName, this.numFlows,
		this.overUtilizePercentage, this.numRuns);
    }

}
