package de.manetmodel.results;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.elements.Link;
import de.manetmodel.elements.LinkQuality;
import de.manetmodel.elements.Node;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.scenarios.Scenario;
import de.manetmodel.network.radio.RadioModel;

public abstract class RunResultMapper<W extends LinkQuality, R extends RunResultParameter> extends ResultMapper<R> {

    protected ColumnPositionMappingStrategy<R> mappingStrategy;
    protected MobilityModel mobilityModel;

    public RunResultMapper(ColumnPositionMappingStrategy<R> mappingStrategy, Scenario scenario,
	    MobilityModel mobilityModel) {
	super(scenario);

	this.mappingStrategy = mappingStrategy;
	this.mobilityModel = mobilityModel;

    }

    public abstract<N extends Node,L extends Link<W>, W extends LinkQuality> R individualRunResultMapper(N source, N sink, L link);

}
