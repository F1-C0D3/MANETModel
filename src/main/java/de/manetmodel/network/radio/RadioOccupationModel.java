package de.manetmodel.network.radio;

import de.manetmodel.network.Link;
import de.manetmodel.network.Node;

public abstract class RadioOccupationModel<N extends Node, L extends Link> implements IRadioModel
{
	public abstract boolean interferencePresent(double distnace);
}
