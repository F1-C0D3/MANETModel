	package de.manetmodel.evaluator;

import org.junit.Test;

import de.jgraphlib.graph.elements.Position2D;
import de.manetmodel.evaluator.DoubleScope;
import de.manetmodel.evaluator.MobilityEvaluator;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.network.scalar.ScalarRadioNode;
import de.manetmodel.units.Speed;
import de.manetmodel.units.Unit;

//@formatter:off

public class MobilityEvaluatorTest {
    
    @Test
    public void mobilityEvaluatorTest() {
		
	MobilityEvaluator<ScalarRadioNode> evaluator = new MobilityEvaluator<ScalarRadioNode>(
		/*scoreScope*/new DoubleScope(0d,10d), 
		/*weight*/1);
	
	ScalarRadioNode node1 = new ScalarRadioNode();
	ScalarRadioNode node2 = new ScalarRadioNode();
	
	node1.setMobility(new MovementPattern(new Speed(5d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
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
	System.out.println(String.format("Score: %.2f \n", evaluator.compute(node1, node2)));			
    }
}
