package de.manetmodel.results;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.scenarios.Scenario;

public abstract class RunResultMapper<R extends RunResultParameter,N extends Node,L extends Link<W>, W extends LinkQuality> extends ResultMapper<R> {

    protected ColumnPositionMappingStrategy<R> mappingStrategy;
    protected MobilityModel mobilityModel;

    public RunResultMapper(ColumnPositionMappingStrategy<R> mappingStrategy, Scenario scenario,
	    MobilityModel mobilityModel) {
	super(scenario);

	this.mappingStrategy = mappingStrategy;
	this.mobilityModel = mobilityModel;

    }

    public abstract R individualRunResultMapper(N source, N sink, L link);

}
