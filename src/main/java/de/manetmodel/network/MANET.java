package de.manetmodel.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.mobility.MobilityModel;
import de.manetmodel.network.mobility.MovementPattern;
import de.manetmodel.network.radio.IRadioModel;
import de.manetmodel.network.unit.DataRate;
import de.manetmodel.network.unit.Speed;
import de.manetmodel.network.unit.Unit;

public class MANET<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>>
	extends DirectedWeighted2DGraph<N, L, W, F> {

    protected IRadioModel radioModel;
    protected MobilityModel mobilityModel;
    protected DataRate utilization;
    protected DataRate capacity;
    protected List<List<Integer>> utilizationAdjacencies;
    protected Set<Integer> activeUtilizedLinks;

    public MANET(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier, Supplier<W> edgeWeightSupplier,
	    Supplier<F> flowSupplier, IRadioModel radioModel, MobilityModel mobilityModel) {
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier);
	this.radioModel = radioModel;
	this.mobilityModel = mobilityModel;
	this.capacity = new DataRate(0L);
	this.utilization = new DataRate(0L);
	this.utilizationAdjacencies = new ArrayList<List<Integer>>();
	this.activeUtilizedLinks = new HashSet<Integer>();
    }

    public MANET(MANET<N, L, W, F> manet) {
	super(manet.vertexSupplier, manet.edgeSupplier, manet.edgeWeightSupplier, manet.pathSupplier);
	this.vertices = manet.vertices;
	this.edges = manet.copyEdges();
	this.paths = manet.copyPaths(manet);
	this.utilization = new DataRate(manet.utilization.get());
	this.capacity = new DataRate(manet.capacity.get());
	this.radioModel = manet.radioModel;
	this.mobilityModel = manet.mobilityModel;
	this.sourceTargetAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>(manet.sourceTargetAdjacencies);
	this.targetSourceAdjacencies = new ArrayList<ArrayList<Tuple<Integer, Integer>>>(manet.targetSourceAdjacencies);
	this.edgeAdjacencies = new ArrayList<Tuple<Integer, Integer>>(manet.edgeAdjacencies);
	this.utilizationAdjacencies = new ArrayList<List<Integer>>(manet.utilizationAdjacencies);
	this.activeUtilizedLinks = new HashSet<Integer>(manet.activeUtilizedLinks);
    }

    public MANET<N, L, W, F> copy() {
	return new MANET<N, L, W, F>(this);
    }

    public List<N> copyVertices() {
	List<N> nodeCopies = new ArrayList<N>();
	for (N node : getVertices()) {
	    N nodeCopy = vertexSupplier.get();
	    nodeCopy.setID(node.getID());
	    nodeCopy.setPosition(node.getPosition());
	    nodeCopies.add(nodeCopy);
	}
	return nodeCopies;
    }

    public List<L> copyEdges() {
	List<L> linkCopies = new ArrayList<L>();
	for (L link : getEdges()) {
	    L linkCopy = edgeSupplier.get();
	    linkCopy.setID(link.getID());
	    W linkQualityCopy = edgeWeightSupplier.get();
	    linkQualityCopy.setDistance(link.getWeight().getDistance());
	    linkQualityCopy.setReceptionPower(link.getWeight().getReceptionPower());
	    linkQualityCopy.setTransmissionRate(new DataRate(link.getWeight().getTransmissionRate().get()));
	    linkQualityCopy.setUtilization(new DataRate(link.getWeight().getUtilization().get()));
	    linkQualityCopy.setNumberOfUtilizedLinks(link.getWeight().getNumberOfUtilizedLinks());
	    linkCopy.setWeight(linkQualityCopy);
	    linkCopies.add(linkCopy);
	}
	return linkCopies;
    }

    public List<F> copyPaths(MANET<N, L, W, F> manet) {
	List<F> flowCopies = new ArrayList<F>();
	for (F flow : paths) {
	    F flowCopy = pathSupplier.get();
	    flowCopy.set(flow.getID(), manet.getVertex(flow.getSource().getID()),
		    manet.getVertex(flow.getTarget().getID()), flow.getDataRate());
	    for (Tuple<L, N> linkNodeTuple : flow.subList(1, flow.size()))
		flowCopy.add(new Tuple<L, N>(manet.getEdge(linkNodeTuple.getSecond().getID()),
			manet.getVertex(linkNodeTuple.getSecond().getID())));
	    flowCopies.add(flowCopy);
	}
	return flowCopies;
    }

    public F copyPath(int i) {
	F path = super.copyPath(i);
	path.setDataRate(getFlows().get(i).getDataRate());
	return path;
    }

    @Override
    public L addEdge(N source, N target) {
	return addEdge(source, target, edgeWeightSupplier.get());
    }

    public void initialize() {
	initializeUtilization();
    }

    private void initializeUtilization() {

	for (L link : this.getEdges()) {

	    Set<L> utilizedLinks = new HashSet<L>();

	    N linkSource = this.getVerticesOf(link).getFirst();

	    N linkTarget = this.getVerticesOf(link).getSecond();

	    for (N node : this.getConnectedVertices(linkSource))
		utilizedLinks.addAll(getEdgesOf(node));

	    for (N node : this.getConnectedVertices(linkTarget))
		utilizedLinks.addAll(getEdgesOf(node));

	    link.getWeight().setNumberOfUtilizedLinks(utilizedLinks.size());

	    utilizationAdjacencies.add(utilizedLinks.stream().map(L::getID).collect(Collectors.toList()));

	}
    }

    public List<L> getUtilizedLinksOf(L link) {

	List<L> utilizedLinks = new ArrayList<L>();

	for (Integer ID : utilizationAdjacencies.get(link.getID()))
	    utilizedLinks.add(getEdge(ID));

	return utilizedLinks;
    }

    @Override
    public L addEdge(N source, N target, W weight) {

	L newLink = super.addEdge(source, target, weight);

	double distance = this.getDistance(source.getPosition(), target.getPosition());

	W linkQuality = edgeWeightSupplier.get();

	linkQuality.setDistance(distance);
	linkQuality.setTransmissionRate(radioModel.transmissionBitrate(distance));
	linkQuality.setReceptionPower(radioModel.receptionPower(distance));
	linkQuality.setUtilization(new DataRate(0));

	newLink.setWeight(linkQuality);

	capacity.set(capacity.get() + radioModel.transmissionBitrate(distance).get());

	return newLink;
    }

    public void addFlow(F flow) {
	flow.setID(paths.size());
	paths.add(flow);
    }

    public F addFlow(N source, N target, DataRate dataRate) {
	F flow = pathSupplier.get();
	flow.set(paths.size(), source, target, dataRate);
	paths.add(flow);
	return flow;
    }

    public List<F> getFlows() {
	return paths;
    }

    public F getFlow(int i) {
	return paths.get(i);
    }

    public F removeFlow(int i) {
	return paths.remove(i);
    }

    public void clearFlows() {
	paths.clear();
    }

    public List<Integer> getFlowIDs() {
	List<Integer> flowIDs = new ArrayList<Integer>();
	for (F flow : paths)
	    flowIDs.add(flow.getID());
	return flowIDs;
    }

    public void deployFlow(F flow) {

	for (L link : flow.getEdges()) {
	    link.getWeight().setActive();
	    increaseUtilizationBy(link, flow.getDataRate());
	}
    }

    public void undeployFlows() {
	for (L link : getEdges()) {
	    link.getWeight().setUtilization(new DataRate(0L));
	    link.getWeight().setPassive();
	}
	this.utilization = new DataRate(0L);
    }

    public void undeployFlow(F flow) {

	for (L l : flow.getEdges()) {

	    if (isFlowRegistered(flow)) {
		if (getFlowsContainingLink(l).size() <= 1)
		    l.getWeight().setPassive();
	    } else {
		if (getFlowsContainingLink(l).size() < 1)
		    l.getWeight().setPassive();
	    }
	    decreaseUtilizationBy(l, flow.getDataRate());

	}
    }

    private boolean isFlowRegistered(F f) {
	return paths.stream().map(F::getID).collect(Collectors.toList()).contains(f.getID());
    }

    private List<F> getFlowsContainingLink(L l) {

	List<F> fContainingl = new ArrayList<F>();

	for (F f : paths) {

	    if (f.contains(l))
		fContainingl.add(f);

	}

	return fContainingl;
    }

    public void increaseUtilizationBy(L activeUtilizedLink, DataRate dataRate) {

	activeUtilizedLink.getWeight().setActive();
	activeUtilizedLinks.add(activeUtilizedLink.getID());

	for (L passiveUtilizedLink : getUtilizedLinksOf(activeUtilizedLink)) {

	    utilization.set(this.utilization.get() + dataRate.get());

	    passiveUtilizedLink.getWeight().setUtilization(
		    new DataRate(passiveUtilizedLink.getWeight().getUtilization().get() + dataRate.get()));
	}
    }

    public void decreaseUtilizationBy(L activeUtilizedLink, DataRate dataRate) {

	for (L ul : getUtilizedLinksOf(activeUtilizedLink)) {

	    DataRate cUtilization = ul.getWeight().getUtilization();
	    cUtilization.set(cUtilization.get() - dataRate.get());
	    ul.getWeight().setUtilization(cUtilization);
	    this.utilization.set(this.utilization.get() - dataRate.get());
	}
    }

    @Override
    public N addVertex(double x, double y) {

	N n = super.addVertex(x, y);

	Speed initialSpeed = new Speed(mobilityModel.speedRange.max().value / 2d, Unit.Distance.meter,
		Unit.Time.second);

	List<MovementPattern> patternList = new ArrayList<MovementPattern>();

	MovementPattern movementPattern = new MovementPattern(initialSpeed, n.getPosition(), 0d);

	patternList.add(movementPattern);

	for (int i = 0; i < mobilityModel.getTicks() - 1; i++) {
	    movementPattern = mobilityModel.computeNextMovementPattern(movementPattern);
	    patternList.add(movementPattern);
	}

	n.setPrevMobility(patternList);

	return n;
    }

    public double getOverUtilizationPercentage() {
	return ((double) getOverUtilization().get() / (double) getCapacity().get()) * 100;
    }

    public DataRate getOverUtilization() {

	DataRate overUtilzation = new DataRate(0);

	for (L link : getOverUtilizedLinks())
	    if (link.getWeight().isActive())
		overUtilzation = new DataRate(overUtilzation.get() + link.getOverUtilization().get());

	return overUtilzation;
    }

    public DataRate getOverUtilizationOf(F flow) {

	DataRate overUtilzation = new DataRate(0);

	for (Tuple<L, N> linkNodeTuple : flow)
	    overUtilzation = new DataRate(overUtilzation.get() + linkNodeTuple.getFirst().getOverUtilization().get());

	return overUtilzation;
    }

    public DataRate getOverUtilizationOf(List<L> links) {

	DataRate overUtilzation = new DataRate(0);

	for (L link : links)
	    overUtilzation = new DataRate(overUtilzation.get() + link.getOverUtilization().get());

	return overUtilzation;
    }

    public double getOverUtilizationPercentageOf(List<L> links) {

	DataRate overUtilization = new DataRate(0);
	DataRate capacity = new DataRate(0);

	for (L link : links) {
	    capacity = new DataRate(overUtilization.get() + link.getWeight().getTransmissionRate().get());
	    overUtilization = new DataRate(overUtilization.get() + link.getOverUtilization().get());
	}

	return ((double) overUtilization.get() / (double) capacity.get()) * 100;
    }

    public List<L> getOverUtilizedLinks() {

	List<L> overutilizedLinks = new ArrayList<L>();

	for (L link : getEdges())
	    if (link.getOverUtilization().get() > 0)
		overutilizedLinks.add(link);

	return overutilizedLinks;
    }

    public List<L> getOverUtilizedActiveLinks() {

	List<L> overutilizedLinks = new ArrayList<L>();

	for (L link : getEdges()) {
	    if (link.getWeight().isActive()) {
		if (link.getOverUtilization().get() > 0)
		    overutilizedLinks.add(link);
	    }
	}

	return overutilizedLinks;
    }

    public List<L> getOverUtilizedLinksOf(F flow) {

	List<L> overutilizedLinks = new ArrayList<L>();

	for (L link : flow.getEdges())
	    if (link.getWeight().isActive())
		if (link.getOverUtilization().get() > 0)
		    overutilizedLinks.add(link);

	return overutilizedLinks;
    }

    public List<L> getActiveUtilizedLinks() {
	return activeUtilizedLinks.stream().map(i -> getEdge(i)).collect(Collectors.toList());
    }

    // Returns all active utilized links of flows that are utilized by given link
    public List<L> getActiveUtilizedLinksOf(L link) {

	Set<L> activeUtilizedLinks = new HashSet<L>();

	// (1) Add all links that are utilized by link to the set
	activeUtilizedLinks.addAll(getUtilizedLinksOf(link));

	// (2) Build intersection with all active utilized links
	activeUtilizedLinks.retainAll(getActiveUtilizedLinks());

	return activeUtilizedLinks.stream().collect(Collectors.toList());
    }

    public Supplier<F> getFlowSupplier() {
	return this.pathSupplier;
    }

    public DataRate getUtilization() {
	return this.utilization;
    }

    public DataRate getCapacity() {
	return this.capacity;
    }
}