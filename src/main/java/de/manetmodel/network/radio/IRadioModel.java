package de.manetmodel.network.radio;

import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.Node;

public interface IRadioModel<N extends Node, L extends Link<W>, W extends LinkQuality> {

    void setLinkRadioParameters(L link, double linkDistance);

    void setNodeRadioParameters(N node,double coverageRange);
}
