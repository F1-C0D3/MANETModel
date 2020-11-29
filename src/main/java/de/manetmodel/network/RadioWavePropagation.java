package de.manetmodel.network;

public abstract class RadioWavePropagation<T>
{
	private static double interferenceRange = 0;
	private static double transmissionRange = 0;

	public RadioWavePropagation(double transmissionRange, double interferenceRange)
	{
		this.interferenceRange = interferenceRange;
		this.transmissionRange = transmissionRange;
	}

	public double interferenceRange()
	{
		return interferenceRange;
	}

	public double transmissionRange()
	{
		return transmissionRange;
	}

	public abstract T getTransmissionQuality(Link l, double distance);
}
