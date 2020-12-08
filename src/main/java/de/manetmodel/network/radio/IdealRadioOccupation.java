package de.manetmodel.network.radio;

import de.manetmodel.network.Link;
import de.manetmodel.network.Node;

public class IdealRadioOccupation extends RadioOccupationModel<Node, Link>
{
	private double transmissionRange;
	private double interferenceRange;
	private double transmissionBitrate;

	public IdealRadioOccupation(double transmissionRange, double interferenceRange, double transmissionBitrate)
	{
		this.transmissionRange = transmissionRange;
		this.interferenceRange = interferenceRange;
		this.transmissionBitrate = transmissionBitrate;
	}

	public IdealRadioOccupation(double transmissionRange, double transmissionBitrate)
	{
		this(transmissionRange, transmissionRange, transmissionBitrate);
	}

	@Override
	public double computeTransmissionBitrate()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double computeReception(double distance)
	{
		// TODO Auto-generated method stub
		return 1d;
	}

	@Override
	public boolean interferencePresent(double distance)
	{
		if (distance < interferenceRange)
			return true;
		return false;
	}

}
