package de.manetmodel.results;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.scenarios.Scenario;

public abstract class RunResultMapper<R extends RunResultParameter,N extends Node,L extends Link<W>, W extends LinkQuality> extends ResultMapper<R> {

    protected ColumnPositionMappingStrategy<R> mappingStrategy;

    public RunResultMapper( Scenario scenario,ColumnPositionMappingStrategy<R> mappingStrategy) {
	super(scenario,mappingStrategy);

	this.mappingStrategy = mappingStrategy;

    }

    public abstract R individualRunResultMapper(N source, N sink, L link);

}
