package de.manetmodel.network;

import java.util.HashSet;
import java.util.Set;

import de.jgraphlib.graph.WeightedEdge;
import de.manetmodel.network.unit.DataRate;

public class Link<W extends LinkProperties> extends WeightedEdge<W> {

    // Links, that are actively and passively affected (in interference range)
    private Set<Link<W>> interferedLinks;
    
    // Indicates, whether Link is occupied actively through transmission as part of a flow
    private boolean isActive; 
    
    public Link() {
	interferedLinks = new HashSet<Link<W>>();
	getWeight().setUtilization(new DataRate(0L));
	isActive = false;
    }

    // can be removed
    public double getReceptionPower() {
	return getWeight().getReceptionPower();
    }
    
    // can be removed
    public void setReceptionPower(double receptionPower) {
	getWeight().setReceptionPower(receptionPower);
    }

    // can be removed
    public void setTransmissionRate(DataRate transmissionBitrate) {
	getWeight().setTransmissionRate(transmissionBitrate);
    }
    
    // can be removed
    public DataRate getTransmissionRate() {
	return getWeight().getTransmissionRate();
    }

    // can be removed
    public Set<Link<W>> inReceptionRange() {
	return interferedLinks;
    }
    
    public void setInterferedLinks(Set<Link<W>> l) {
	interferedLinks.addAll(l);
	getWeight().setInterference(interferedLinks.size());
    }

    // can be removed
    public DataRate getUtilization() {
	return getWeight().getUtilization();
    }

    // can be removed
    public void setUtilization(DataRate u) {
	//this.utilization.set(u.get());
	getWeight().setUtilization(u);
    }

    // can be removed
    public void increaseUtilizationBy(DataRate u) {
	getWeight().getUtilization().set(getWeight().getUtilization().get() + u.get());
    }

    @Override
    public String toString() {
	return new StringBuffer("ID: ").append(getID()).append(", Utilization: ").append(this.getUtilization())
		.toString();
    }

    public void setIsActive(boolean isParticipant) {
	this.isActive = isParticipant;

    }

    public boolean getIsActive() {
	return this.isActive;
    }
}
