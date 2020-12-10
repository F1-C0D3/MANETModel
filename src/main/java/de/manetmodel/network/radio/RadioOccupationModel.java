package de.manetmodel.network.radio;

public abstract class RadioOccupationModel implements IRadioModel
{
	public abstract boolean interferencePresent(double distnace);
}
