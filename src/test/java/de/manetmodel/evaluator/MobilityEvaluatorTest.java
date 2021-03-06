	package de.manetmodel.evaluator;

import org.junit.Test;

import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.RandomNumbers;
import de.manetmodel.evaluation.SourceSinkSingleTickMobilityEvaluation;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.mobilitymodel.PedestrianMobilityModel;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Speed.SpeedRange;
import de.manetmodel.units.Unit;

//@formatter:off

public class MobilityEvaluatorTest {
    
    @Test
    public void mobilityEvaluatorTest() {
		
	SourceSinkSingleTickMobilityEvaluation<ScalarRadioNode> evaluator = new SourceSinkSingleTickMobilityEvaluation<ScalarRadioNode>(
		/*scoreScope*/new DoubleScope(1d, 10d), 
		/*weight*/1,
		new PedestrianMobilityModel(new RandomNumbers(), new SpeedRange(1000, 5000, Unit.TimeSteps.second, Unit.Distance.meter), new Speed(50, Unit.Distance.meter, Unit.TimeSteps.second)));
	
	ScalarRadioNode node1 = new ScalarRadioNode();
	ScalarRadioNode node2 = new ScalarRadioNode();
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 0d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(1000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 0d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(2000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 0d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(1000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(1000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 0d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	/*node1.setMobility(new MovementPattern(new Speed(5d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 0d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 30d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 60d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 90d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 120d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 150d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 180d));
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));*/
    }
}
