package de.manetmodel.scenarios;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Scenario {

    private String indivdualName;

    protected int overUtilizePercentage;
    protected int numFlows;
    protected int numRuns;
    protected int numNodes;
    protected int datePrefixFlag;
    protected int seed;

    public Scenario(String indivdualName, int numFlows, int numNodes, int runs, int overUtilizePercentage, int datePrefixFlag, int seed) {
	this.numRuns = runs;
	this.indivdualName = indivdualName;
	this.numFlows = numFlows;
	this.overUtilizePercentage = overUtilizePercentage;
	this.numNodes = numNodes;
	this.datePrefixFlag = datePrefixFlag;
	this.seed=seed;
    }

    public Scenario(int flows, int nodes, int runs,int overUtilizePercentage,int datePrefixFlag, int seed) {
	this.numRuns = runs;
	this.indivdualName = new String();
	this.numFlows = flows;
	this.overUtilizePercentage = overUtilizePercentage;
	this.numNodes = nodes;
	this.datePrefixFlag = datePrefixFlag;
	this.seed=seed;
    }

    public Scenario(int runs) {
	this.numRuns = runs;
	this.numFlows = -1;
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
    
    public int getNumNodes() {
	return numNodes;
    }

    public String getScenarioName() {
	return indivdualName;
    }
    public void setScenarioName(String individualName) {
	this.indivdualName = individualName;
    }

    public int getDatePrefixFlag() {
        return datePrefixFlag;
    }

    public void setDatePrefixFlag(int datePrefixFlag) {
        this.datePrefixFlag = datePrefixFlag;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
    
    

    
}
