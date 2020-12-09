package de.manetmodel.network.radio;

public class IdealRadio implements IRadioModel
{

	private double transmissionRange;
	private double interferenceRange;
	private double transmissionBitrate;

	public IdealRadio(double transmissionRange, double interferenceRange, double transmissionBitrate)
	{
		this.transmissionRange = transmissionRange;
		this.interferenceRange = interferenceRange;
		this.transmissionBitrate = transmissionBitrate;
	}

	public IdealRadio(double transmissionRange, double transmissionBitrate)
	{
		this(transmissionRange, transmissionRange, transmissionBitrate);
	}

	@Override
	public double computeTransmissionBitrate()
	{
		return this.transmissionBitrate;
	}

	@Override
	public double computeReception(double distance)
	{
		return 1d;
	}

}
