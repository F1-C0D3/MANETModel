package de.runprovider;

import java.util.List;
import java.util.function.Supplier;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import de.jgraphlib.graph.generator.GraphProperties;
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
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Speed.SpeedRange;
import de.manetmodel.network.unit.Time;
import de.manetmodel.network.unit.Unit;
import de.manetmodel.scenarios.Scenario;
import de.results.AverageResultParameter;
import de.results.MANETAverageResultMapper;
import de.results.MANETIdealRadioRunResultMapper;
import de.results.MANETResultRecorder;
import de.results.RunResultMapper;
import de.results.RunResultParameter;

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

    public NetworkGraphProperties generateNetwork(MANET<N, L, W, F> manet, int runs, int numNodes) {
	NetworkGraphProperties properties = new NetworkGraphProperties( /* playground width */ 1024,
		/* playground height */ 768, /* number of vertices */ new IntRange(numNodes, numNodes),
		/* distance between vertices */ new DoubleRange(50d, 100d),
		/* edge distance */ new DoubleRange(100, 100));
	new NetworkGraphGenerator<N, L, W>(manet, RandomNumbers.getInstance(runs)).generate(properties);
	manet.initialize();
	return properties;
    }

    public MobilityModel setMobilityModel(int runs) {
	/* Mobility model to include movement of nodes based on velocity and pattern */
	return new PedestrianMobilityModel(RandomNumbers.getInstance(runs),
		new SpeedRange(4d, 40d, Unit.Time.hour, Unit.Distance.kilometer), new Time(Unit.Time.second, 30l),
		new Speed(4d, Unit.Distance.kilometer, Unit.Time.hour), 10);

    }

    public IRadioModel setRadioModel() {
	/* Radio wave propagation model to determine bitrate and receptionpower */
	return new ScalarRadioModel(0.002d, 1e-11, 2000000d, 2412000000d);
    }

    public MANET<N, L, W, F> createMANET(MobilityModel mobilityModel, IRadioModel radioModel) {

	MANET<N, L, W, F> manet = new MANET<N, L, W, F>(nodeSupplier, linkSupplier, linkQualitysupplier, flowSupplier,
		radioModel, mobilityModel);

	return manet;
    }

    public <R extends AverageResultParameter> MANETAverageResultMapper<R> setTotalResultMapper(
	    Supplier<R> resultParameterSupplier, Scenario scenario) {

	/* Result recording options for further evaluation */
	ColumnPositionMappingStrategy<R> mappingStrategy = new ColumnPositionMappingStrategy<R>() {
	    @Override
	    public String[] generateHeader(R bean) throws CsvRequiredFieldEmptyException {
		return this.getColumnMapping();
	    }
	};
	mappingStrategy.setColumnMapping("overUtilization", "utilization", "activePathParticipants",
		"connectionStability", "simulationTime");
	return new MANETAverageResultMapper<R>(resultParameterSupplier, mappingStrategy, scenario);

    }

    public <R extends RunResultParameter> MANETResultRecorder<R> setResultRecorder(String resultFileName) {

	/* Result recording options for further evaluation */
	return new MANETResultRecorder<R>(resultFileName);

    }

    public <R extends RunResultParameter> RunResultMapper<R> setIndividualRunResultMapper(
	    Supplier<R> resultParameterSupplier, GraphProperties networkProperties, MobilityModel mobilityModel,
	    IRadioModel radioModel, Scenario scenario) {

	ColumnPositionMappingStrategy<R> mappingStrategy = new ColumnPositionMappingStrategy<R>() {
	    @Override
	    public String[] generateHeader(R bean) throws CsvRequiredFieldEmptyException {
		return this.getColumnMapping();
	    }
	};

	mappingStrategy.setColumnMapping("lId", "n1Id", "n2Id", "overUtilization", "utilization", "isPathParticipant",
		"connectionStability");
	RunResultMapper<R> resultMapper = new MANETIdealRadioRunResultMapper<R>(resultParameterSupplier,
		mappingStrategy, scenario, networkProperties, radioModel, mobilityModel);
	return resultMapper;
    }

}
