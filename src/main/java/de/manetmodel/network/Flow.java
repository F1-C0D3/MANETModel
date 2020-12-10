package de.manetmodel.network;

import de.manetmodel.graph.ManetPath;

public class Flow<N extends Node, L extends Link> extends ManetPath<N, L> {

    private double bitrate;

    public Flow(N source, N target) {
	this(source, target, 0d);
    }

    public Flow(N source, N target, double bitrate) {
	super(source, target);
	this.bitrate = bitrate;
    }

    public double getBitrate() {
	return this.bitrate;
    }

}
