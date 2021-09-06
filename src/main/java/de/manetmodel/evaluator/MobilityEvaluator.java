package de.manetmodel.evaluator;

import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.Node;

public class MobilityEvaluator<N extends Node> extends PropertyStandardization {
   
    public MobilityEvaluator(DoubleScope scoreScope, double weight) {
	super(scoreScope, weight);
    }

    public double compute(N source, N sink){
	
	MovementPattern sourceTick = source.getMobility().get(source.getMobility().size()-1);
	//System.out.println(String.format("sourceMobility: %s", sourceTick.toString()));

	MovementPattern sinkTick = sink.getMobility().get(source.getMobility().size()-1);
	//System.out.println(String.format("sinkMobility: %s", sinkTick.toString()));
	
	//double angleDregrees = Math.abs(Math.toDegrees(sourceTick.getAngle()) - Math.toDegrees(sinkTick.getAngle()));
	double angleDregrees = Math.abs(sourceTick.getAngle() - sinkTick.getAngle());
	//System.out.println(String.format("angleDregrees %.2f", angleDregrees));
	
	double vectorProduct = sourceTick.getSpeed().value * sinkTick.getSpeed().value;
	//System.out.println(String.format("vectorProduct %.2f", vectorProduct));
	
	double scalarProduct = Math.cos(Math.toRadians(angleDregrees)) * vectorProduct;
	//System.out.println(String.format("scalarProdcut %.2f", scalarProduct));
	
	setPropertyScope(new DoubleScope(-vectorProduct, vectorProduct));
	
	//System.out.println(String.format("score %.2f", getScore(scalarProduct)));
	
	return getScore(scalarProduct);
    }   
}
