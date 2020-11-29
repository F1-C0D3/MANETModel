package de.manetmodel.network;

public class IdealRadioWavePropagation extends RadioWavePropagation<Integer>
{

	public IdealRadioWavePropagation(double transmissionRange, double interferenceRange)
	{
		super(transmissionRange, interferenceRange);
	}

	public IdealRadioWavePropagation(double transmissionRange)
	{
		super(transmissionRange, transmissionRange);
	}

	@Override
	public Integer getTransmissionQuality(Link l, double distance)
	{
		return 1;
	}
}
