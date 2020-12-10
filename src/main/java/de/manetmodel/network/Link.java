package de.manetmodel.network;

import de.manetmodel.graph.Edge;

public class Link extends Edge
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
