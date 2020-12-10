package de.manetmodel.graph;

public class Playground
{
	public IntRange width;
	public IntRange height;
	public IntRange vertexCount;
	public IntRange edgeCount;
	public DoubleRange edgeDistance;

	public Playground(){}

	public boolean isInside(double x, double y)
	{
		return (x >= width.min && x <= width.max) && (y >= height.min && y <= height.max);
	}

	public static class IntRange
	{
		public int min;
		public int max;

		public IntRange(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
	}

	public static class DoubleRange
	{
		public double min;
		public double max;

		public DoubleRange(double min, double max)
		{
			this.min = min;
			this.max = max;
		}
	}
}