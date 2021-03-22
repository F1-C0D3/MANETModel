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
import de.manetmodel.network.unit.VelocityUnits;
import de.manetmodel.network.unit.Speed.SpeedRange;
import de.manetmodel.network.unit.Speed.Time;
import de.results.MANETRunResultMapper;
import de.results.RunResultMapper;
import de.results.MANETResultRunSupplier;
import de.results.MANETResultParameter;
import de.results.MANETResultRecorder;
import de.results.Scenario;

public class Program<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>, R extends MANETResultParameter> {

    Supplier<N> nodeSupplier;
    Supplier<L> linkSupplier;
    Supplier<F> flowSupplier;
    Supplier<R> resultParameterSupplier;
    Supplier<W> linkQualitysupplier;

    public Program(Supplier<N> nodeSupplier, Supplier<L> linkSupplier, Supplier<W> linkQualitySupplier,
	    Supplier<F> flowSupplier, Supplier<R> resultSupplier) {
	this.nodeSupplier = nodeSupplier;
	this.resultParameterSupplier = resultSupplier;
	this.linkSupplier = linkSupplier;
	this.flowSupplier = flowSupplier;
	this.linkQualitysupplier = linkQualitySupplier;

    }

    public List<Triple<Integer, Integer, DataRate>> generateFlowSourceTargetPairs(int numNodes, int numFlows,
	    DataRateRange range, int runs) {
	List<Integer> exclusionList = new ArrayList<Integer>();
	List<Triple<Integer, Integer, DataRate>> flowSourceTargetPairs = new ArrayList<Triple<Integer, Integer, DataRate>>();
	for (int i = 0; i < (numFlows*2); i++) {
	    int randomNodeId = new RandomNumbers(runs).getRandomNotInE(0, numNodes, exclusionList);
	    exclusionList.add(randomNodeId);
	}
	Triple<Integer, Integer, DataRate> stTuple = null;
	for (int i = 0; i < exclusionList.size(); i++) {

	    if (i % 2 == 0) {
		stTuple = new Triple<Integer, Integer, DataRate>();
		stTuple.setFirst(exclusionList.get(i));
	    } else {
		stTuple.setSecond(exclusionList.get(i));
		int dataRate = new RandomNumbers(runs).getRandom((int) range.min().get(), (int) range.max().get());
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
		/* edge distance */ 100);
	new NetworkGraphGenerator<N, L, W>(manet, new RandomNumbers(runs)).generate(properties);

	return properties;
    }

    public MobilityModel setMobilityModel(int runs) {
	/* Mobility model to include movement of nodes based on velocity and pattern */
	return new PedestrianMobilityModel(new RandomNumbers(runs),
		new SpeedRange(4d, 40d, VelocityUnits.TimeUnit.hour, VelocityUnits.DistanceUnit.kilometer),
		new Time(VelocityUnits.TimeUnit.second, 30d),
		new Speed(4d, VelocityUnits.DistanceUnit.kilometer, VelocityUnits.TimeUnit.hour), 10);

    }

    public IRadioModel setRadioModel() {
	/* Radio wave propagation model to determine bitrate and receptionpower */
	return new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d);
    }

    public MANET<N, L, W, F> createMANET(MobilityModel mobilityModel, IRadioModel radioModel) {

	MANET<N, L, W, F> manet = new MANET<N, L, W, F>(nodeSupplier, linkSupplier, flowSupplier, radioModel,
		mobilityModel);
	manet.setEdgeWeightSupplier(linkQualitysupplier);
	return manet;
    }

    public MANETResultRecorder<N, L, W, F, R> setResultRecorder(String resultFileName) {

	/* Result recording options for further evaluation */
	ColumnPositionMappingStrategy<R> mappingStrategy = new ColumnPositionMappingStrategy<R>() {
	    @Override
	    public String[] generateHeader(R bean) throws CsvRequiredFieldEmptyException {
		return this.getColumnMapping();
	    }
	};
	mappingStrategy.setType((Class<? extends R>) MANETResultParameter.class);
	return new MANETResultRecorder<N, L, W, F, R>(resultFileName, mappingStrategy);

    }

    public MANETRunResultMapper<W, R> setResultMapper(NetworkGraphProperties networkProperties,
	    MobilityModel mobilityModel, IRadioModel radioModel, MANETResultRecorder<N, L, W, F, R> resultRecorder,
	    String resultFileName, int numNodes, int numFlows) {
	MANETRunResultMapper<W, R> resultMapper = new MANETRunResultMapper<W, R>(resultParameterSupplier,
		new Scenario(resultFileName, numFlows, numNodes), networkProperties, radioModel, mobilityModel);
	resultRecorder.setRunRecorder(resultMapper);
	return resultMapper;
    }

}
