package de.manetmodel.graph.viz;

import org.junit.Test;

import de.manetmodel.app.gui.math.Line2D;
import de.manetmodel.app.gui.math.Point2D;
import de.manetmodel.app.gui.math.VectorLine2D;

public class VectorLine2DTest {

    @Test
    public void test() {
	
	Point2D p1 = new Point2D(1,1);
	Point2D p2 = new Point2D(5,1);
	
	Line2D horizontalLine = new Line2D(p1.x(), p1.y(), p2.x(), p2.y());
	
	System.out.println(horizontalLine.toString());
		
	VectorLine2D verticalLine = new VectorLine2D(p1.x(), p1.y(), horizontalLine.getPerpendicularSlope());
	
	System.out.println(verticalLine.toString());
	
	Point2D p3 = verticalLine.getPointInDistance(5);
	
	System.out.format(p3.toString());
	
    }

}
