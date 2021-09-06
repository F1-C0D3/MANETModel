package de.manetmodel.evaluator;

import org.junit.Test;

import de.jgraphlib.graph.elements.Position2D;
import de.manetmodel.example.elements.ScalarRadioNode;
import de.manetmodel.example.evaluator.MobilityEvaluator;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Unit;

//@formatter:off

public class MobilityEvaluatorTest {
    
    @Test
    public void mobilityEvaluatorTest() {
		
	MobilityEvaluator<ScalarRadioNode> evaluator = new MobilityEvaluator<ScalarRadioNode>(
		/*scoreScope*/new DoubleScope(0d,10d), 
		/*weight*/1);
	
	ScalarRadioNode node1 = new ScalarRadioNode();
	ScalarRadioNode node2 = new ScalarRadioNode();
	
	node1.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(1,1), 0d));	
	node2.setMobility(new MovementPattern(new Speed(5000d, Unit.Distance.meter, Unit.TimeSteps.second), new Position2D(2,2), 0d));
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
