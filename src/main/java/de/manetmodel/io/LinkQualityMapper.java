package de.manetmodel.io;

import java.util.ArrayList;
import java.util.List;

import de.jgraphlib.graph.io.EdgeWeightMapper;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.DataUnit.Type;

public class LinkQualityMapper extends EdgeWeightMapper<LinkQuality> {

	@Override
	public List<Tuple<String, String>> translate(LinkQuality linkQuality) {		
		List<Tuple<String, String>> attributesValues = new ArrayList<Tuple<String, String>>();	
		attributesValues.add(new Tuple<String, String>("distance", Double.toString(linkQuality.getDistance())));	
		attributesValues.add(new Tuple<String, String>("receptionPower", Double.toString(linkQuality.getReceptionPower())));	
		attributesValues.add(new Tuple<String, String>("transmissionRate", Long.toString(linkQuality.getTransmissionRate().get())));
		attributesValues.add(new Tuple<String, String>("utilization", Long.toString(linkQuality.getUtilization().get())));	
		attributesValues.add(new Tuple<String, String>("utilizedLinks", Integer.toString(linkQuality.getNumUtilizedLinks())));
		return attributesValues;
	}

	@Override
	public LinkQuality translate(List<Tuple<String, String>> attributesValues) {		
		LinkQuality linkQuality = new LinkQuality();
		linkQuality.setDistance(Double.parseDouble(attributesValues.get(0).getSecond()));		
		linkQuality.setReceptionPower(Double.parseDouble(attributesValues.get(1).getSecond()));
		linkQuality.setTransmissionRate(new DataRate(Double.parseDouble(attributesValues.get(2).getSecond()), Type.bit));
		linkQuality.setUtilization(new DataRate(Double.parseDouble(attributesValues.get(3).getSecond()), Type.bit));
		linkQuality.setNumUtilizedLinks(Integer.parseInt(attributesValues.get(4).getSecond()));	
		return new LinkQuality();		
	}
}
