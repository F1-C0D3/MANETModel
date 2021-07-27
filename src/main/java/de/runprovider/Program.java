package de.runprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import de.jgraphlib.graph.generator.GraphProperties.DoubleRange;
import de.jgraphlib.graph.generator.GraphProperties.IntRange;
import de.jgraphlib.graph.generator.NetworkGraphGenerator;
import de.jgraphlib.graph.generator.NetworkGraphProperties;
import de.jgraphlib.util.RandomNumbers;
import de.jgraphlib.util.Triple;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.PedestrianMobilityModel;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.radio.ScalarRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.DataRate.DataRateRange;
import de.manetmodel.network.unit.DataUnit;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.network.unit.Speed.SpeedRange;
import de.manetmodel.network.unit.Time;
import de.results.RunResultMapper;
import de.results.ResultParameter;
import de.results.MANETRunResultMapper;
import de.results.RunResultParameter;
import de.results.RunResultParameterSupplier;
import de.results.AverageResultParameter;
import de.results.MANETAverageResultMapper;
import de.results.MANETResultRecorder;
import de.results.Scenario;

public class Program<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> {

    Supplier<N> nodeSupplier;
    Supplier<L> linkSupplier;
    Supplier<F> flowSupplier;
    Supplier<W> linkQualitysupplier;

    public Program(Supplier<N> nodeSupplier, Supplier<L> linkSupplier, Supplier<W> linkQualitySupplier,
	    Supplier<F> flowSupplier) {
	this.nodeSupplier = nodeSupplier;
	this.linkSupplier = linkSupplier;
	this.flowSupplier = flowSupplier;
	this.linkQualitysupplier = linkQualitySupplier;

    }

    public List<Triple<Integer, Integer, DataRate>> generateFlowSourceTargetPairs(int numNodes, int numFlows,
	    DataRateRange range, int runs) {
	List<Integer> exclusionList = new ArrayList<Integer>();
	List<Triple<Integer, Integer, DataRate>> flowSourceTargetPairs = new ArrayList<Triple<Integer, Integer, DataRate>>();
	for (int i = 0; i < (numFlows * 2); i++) {
	    int randomNodeId = RandomNumbers.getInstance(runs).getRandomNotInE(0, numNodes, exclusionList);
	    exclusionList.add(randomNodeId);
	}
	Triple<Integer, Integer, DataRate> stTuple = null;
	for (int i = 0; i < exclusionList.size(); i++) {

	    if (i % 2 == 0) {
		stTuple = new Triple<Integer, Integer, DataRate>();
		stTuple.setFirst(exclusionList.get(i));
	    } else {
		stTuple.setSecond(exclusionList.get(i));
		int dataRate = RandomNumbers.getInstance(runs).getRandom((int) range.min().get(),
			(int) range.max().get());
		stTuple.setThird(new DataRate(dataRate));
		flowSourceTargetPairs.add(stTuple);
	    }
	}
	return flowSourceTargetPairs;
    }

    public void addFlows(MANET<N, L, W, F> manet, List<Triple<Integer, Integer, DataRate>> flowSourceTargetIds,
	    int runs) {
	for (Triple<Integer, Integer, DataRate> triple : flowSourceTargetIds) {
	    manet.addFlow(manet.getVertex(triple.getFirst()), manet.getVertex(triple.getSecond()), triple.getThird());
	}
    }

    public NetworkGraphProperties generateNetwork(MANET<N, L, W, F> manet, int runs, int numNodes) {
	NetworkGraphProperties properties = new NetworkGraphProperties(/* width */ 1000, /* height */ 1000,
		/* vertices */ new IntRange(numNodes, numNodes), /* vertex distance */ new DoubleRange(55d, 100d),
		/* edge distance */ new DoubleRange(95d, 125d));
	new NetworkGraphGenerator<N, L, W>(manet, RandomNumbers.getInstance(runs)).generate(properties);

	return properties;
    }

    public MobilityModel setMobilityModel(int runs) {
	/* Mobility model to include movement of nodes based on velocity and pattern */
	return new PedestrianMobilityModel(RandomNumbers.getInstance(runs),
		new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer),
		new Time(Unit.Time.second, 30l),
		new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10);

    }

    public IRadioModel setRadioModel() {
	/* Radio wave propagation model to determine bitrate and receptionpower */
	return new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d);
    }

    public MANET<N, L, W, F> createMANET(MobilityModel mobilityModel, IRadioModel radioModel) {

	MANET<N, L, W, F> manet = new MANET<N, L, W, F>(nodeSupplier, linkSupplier, linkQualitysupplier, flowSupplier, radioModel,
		mobilityModel);
	
	return manet;
    }

    public <R extends AverageResultParameter> MANETAverageResultMapper<R> setTotalResultMapper(
	    Supplier<R> resultParameterSupplier, String resultFileName, int numNodes, int numFlows,
	    DataRate meantransmissionRate) {

	/* Result recording options for further evaluation */
	ColumnPositionMappingStrategy<R> mappingStrategy = new ColumnPositionMappingStrategy<R>() {
	    @Override
	    public String[] generateHeader(R bean) throws CsvRequiredFieldEmptyException {
		return this.getColumnMapping();
	    }
	};
	mappingStrategy.setColumnMapping("overUtilization", "utilization", "activePathParticipants", "connectionStability",
		"simulationTime");
	return new MANETAverageResultMapper<R>(resultParameterSupplier, mappingStrategy,
		new Scenario(resultFileName, numFlows, numNodes, meantransmissionRate));

    }

    public <R extends RunResultParameter> MANETResultRecorder<R> setResultRecorder(String resultFileName) {

	/* Result recording options for further evaluation */
	return new MANETResultRecorder<R>(resultFileName);

    }

    public <R extends RunResultParameter> MANETRunResultMapper<R> setIndividualRunResultMapper(
	    Supplier<R> resultParameterSupplier, NetworkGraphProperties networkProperties, MobilityModel mobilityModel,
	    IRadioModel radioModel, String resultFileName, int numNodes, int numFlows, DataRate meantransmissionRate) {

	ColumnPositionMappingStrategy<R> mappingStrategy = new ColumnPositionMappingStrategy<R>() {
	    @Override
	    public String[] generateHeader(R bean) throws CsvRequiredFieldEmptyException {
		return this.getColumnMapping();
	    }
	};

	mappingStrategy.setColumnMapping("lId", "n1Id", "n2Id", "overUtilization", "utilization", "isPathParticipant",
		"connectionStability");
	MANETRunResultMapper<R> resultMapper = new MANETRunResultMapper<R>(resultParameterSupplier, mappingStrategy,
		new Scenario(resultFileName, numFlows, numNodes, meantransmissionRate), networkProperties, radioModel,
		mobilityModel);
	return resultMapper;
    }

}
