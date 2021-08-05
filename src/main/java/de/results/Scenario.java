package de.results;

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

    protected int flows;
    protected int nodes;
    private String indivdualName;

    public Scenario(String indivdualName, int flows, int nodes) {
	this.indivdualName = indivdualName;
	this.nodes = nodes;
	this.flows = flows;
    }

    public Scenario(int flows, int nodes) {
	this.indivdualName = new String();
	this.nodes = nodes;
	this.flows = flows;
    }

    public Scenario() {
	this.flows = -1;
	this.nodes = -1;
    }

    private List<Tuple<Integer, Integer>> generateFlowSourceTargetPairs(int runs) {
	List<Integer> exclusionList = new ArrayList<Integer>();
	List<Tuple<Integer, Integer>> flowSourceTargetPairs = new ArrayList<Tuple<Integer, Integer>>();
	for (int i = 0; i < (flows * 2); i++) {
	    int randomNodeId = RandomNumbers.getInstance(runs).getRandomNotInE(0, nodes, exclusionList);
	    exclusionList.add(randomNodeId);
	}
	Tuple<Integer, Integer> stTuple = null;
	for (int i = 0; i < exclusionList.size(); i++) {

	    if (i % 2 == 0) {
		stTuple = new Tuple<Integer, Integer>();
		stTuple.setFirst(exclusionList.get(i));
	    } else {
		stTuple.setSecond(exclusionList.get(i));
		flowSourceTargetPairs.add(stTuple);
	    }
	}
	return flowSourceTargetPairs;
    }

    public Path getResultFolder() {
	return Paths.get(new StringBuffer().append(indivdualName).toString());
    }

    public String getResultFile() {
	return new StringBuffer().append(indivdualName).append("_Flows_").append(flows).append("_")
		.append("MeanTransmissionRate").append("_").append(meanTransmissionRate.toString()).append("_Nodes_")
		.append(nodes).toString();
    }

}
