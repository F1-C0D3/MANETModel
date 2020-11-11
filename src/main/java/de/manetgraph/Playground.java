package de.manetgraph;

public class Playground {
	
	public IntRange width;
	public IntRange height;	
	public IntRange edgeCount;
	public IntRange vertexCount;
	public DoubleRange vertexDistance;
	public DoubleRange edgeDistance;

	public Playground() {
		
	}
	
	public boolean isInside(Coordinate coordinate) {
		return (coordinate.x() >= width.min && coordinate.x() <= width.max) && (coordinate.y() >= height.min && coordinate.y() <= height.max);	
	}
	
	public static class IntRange{	
		public int min;
		public int max;	
		
		public IntRange(int min, int max) {
			this.min = min;
			this.max = max;
		}	
	}
	
	public static class DoubleRange {
		public double min;
		public double max;	
		
		public DoubleRange(double min, double max) {
			this.min = min;
			this.max = max;
		}
	}
}