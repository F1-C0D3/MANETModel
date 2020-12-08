package de.manetmodel.network.radio;

public interface IRadioModel
{
	double computeTransmissionBitrate();

	double computeReception(double distance);

}
