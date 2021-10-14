package de.manetmodel.results;

import java.util.List;

import com.opencsv.bean.ColumnPositionMappingStrategy;

import de.manetmodel.scenarios.Scenario;

public abstract class TotalResultMapper<T extends ResultParameter, I extends IndividualRunResultParameter,A extends AverageRunResultParameter> extends ResultMapper {

    protected ColumnPositionMappingStrategy<T> totalMappingStrategy;
    
    public TotalResultMapper(Scenario scenario, ColumnPositionMappingStrategy<T> totalMappingStrategy) {
	super(scenario);
	this.totalMappingStrategy = totalMappingStrategy;
    }
    
    

    public ColumnPositionMappingStrategy<T> getTotalMappingStrategy() {
        return totalMappingStrategy;
    }



    public abstract  T toalMapper(List<RunResultContent<I, A>> runs);

}
