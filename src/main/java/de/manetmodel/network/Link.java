package de.manetmodel.network;

import de.manetmodel.graph.IManetElement;
import de.manetmodel.graph.ManetEdge;

public class Link extends ManetEdge implements IManetElement
{
	private double receptionPower;
	private double transmissionBitrate;

	public void setReceptionPower(double receptionPower)
	{
		this.receptionPower = receptionPower;

	}

	public void setTransmissionRate(double transmissionBitrate)
	{
		this.transmissionBitrate = transmissionBitrate;

	}
}
