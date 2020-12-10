package de.manetmodel.network.radio;

public class ShannonHartleyOccupation extends RadioOccupationModel
{

	/*
	 * Model under construction!!! Can not be applied
	 */
	@Override
	public double computeTransmissionBitrate()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double computeReception(double distance)
	{
		/** Example with 2.4Ghz **/
		/**
		 * ((299792458.0 / 2400000000)*2d) / (16d*Math.PI*2d*Math.pow(nodeDistance,
		 * 2d));
		 **/
		return 0d;
	}

	@Override
	public boolean interferencePresent(double distnace)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
