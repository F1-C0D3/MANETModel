package de.manetmodel.results;

import java.util.List;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;
import de.manetmodel.scenarios.Scenario;
import de.manetmodel.units.Time;

public abstract class RunResultMapper<I extends ResultParameter, A extends ResultParameter, N extends Node, L extends Link<W>, W extends LinkQuality>
	extends ResultMapper {

    protected ColumnPositionMappingStrategy<I> individualMappingStrategy;
    protected ColumnPositionMappingStrategy<A> averageMappingStrategy;

    public RunResultMapper(Scenario scenario, ColumnPositionMappingStrategy<I> individualMappingStrategy,
	    ColumnPositionMappingStrategy<A> averageMappingStrategy) {
	super(scenario);
	this.individualMappingStrategy = individualMappingStrategy;
	this.averageMappingStrategy = averageMappingStrategy;

    }

    public ColumnPositionMappingStrategy<I> getIndividualMappingStrategy() {
	return this.individualMappingStrategy;
    }

    public void setIndividualMappingStrategy(ColumnPositionMappingStrategy<I> individualMappingStrategy) {
	this.individualMappingStrategy = individualMappingStrategy;
    }

    public ColumnPositionMappingStrategy<A> getAverageMappingStrategy() {
	return this.averageMappingStrategy;
    }

    public void setAverageMappingStrategy(ColumnPositionMappingStrategy<A> averageMappingStrategy) {
	this.averageMappingStrategy = averageMappingStrategy;
    }

    public abstract I individualRunResultMapper(N source, N sink, L link);

    public abstract <F extends Flow<N, L, W>> A averageRunResultMapper(List<I> runParameters, List<F> flows,
	    Time duration,int runNumber);

}
