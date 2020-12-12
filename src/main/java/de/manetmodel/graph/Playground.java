package de.manetmodel.graph;

import java.text.DecimalFormat;

public class Playground {
    public IntRange width;
    public IntRange height;
    public IntRange vertexCount;
    public DoubleRange vertexDistance;
    public IntRange edgeCount;
    public DoubleRange edgeDistance;

    public Playground() {
    }

    public boolean isInside(double x, double y) {
	return (x >= width.min && x <= width.max) && (y >= height.min && y <= height.max);
    }

    public static class IntRange {
	public int min;
	public int max;

	public IntRange(int min, int max) {
	    this.min = min;
	    this.max = max;
	}

	@Override
	public String toString() {
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append("(min: ").append(this.min);
	    stringBuilder.append(", max: ").append(this.max).append(")");
	    return stringBuilder.toString();
	}
    }

    public static class DoubleRange {
	public double min;
	public double max;

	public DoubleRange(double min, double max) {
	    this.min = min;
	    this.max = max;
	}

	@Override
	public String toString() {
	    DecimalFormat decimalFormat = new DecimalFormat("#.00");
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder.append("(min: ").append(decimalFormat.format(this.min));
	    stringBuilder.append(", max: ").append(decimalFormat.format(this.max)).append(")");
	    return stringBuilder.toString();
	}
    }

    @Override
    public String toString() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append("width: ").append(this.width.toString());
	stringBuilder.append(", height: ").append(this.height.toString());
	stringBuilder.append(", vertexCount: ").append(this.vertexCount);
	stringBuilder.append(", vertexDistance: ").append(this.vertexDistance.toString());
	stringBuilder.append(", edgeCount: ").append(this.edgeCount);
	stringBuilder.append(", edgeDistance: ").append(this.edgeDistance.toString());
	return stringBuilder.toString();

    }
}