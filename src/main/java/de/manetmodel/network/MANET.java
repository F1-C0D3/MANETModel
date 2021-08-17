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

    private IRadioModel radioModel;
    private MobilityModel mobilityModel;
    protected DataRate utilization;
    private DataRate capacity;
    protected List<List<Integer>> utilizationAdjacencies;

    public MANET(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier, Supplier<W> edgeWeightSupplier,
	    Supplier<F> flowSupplier, IRadioModel radioModel, MobilityModel mobilityModel) {
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier);
	this.radioModel = radioModel;
	this.mobilityModel = mobilityModel;
	this.capacity = new DataRate(0L);
	this.utilization = new DataRate(0L);
	this.utilizationAdjacencies = new ArrayList<List<Integer>>();
    }

    public MANET(MANET<N, L, W, F> manet) {
	super(manet.vertexSupplier, manet.edgeSupplier, manet.edgeWeightSupplier, manet.pathSupplier);
	// Shallow copy non-manipulated attributes
	this.edgeWeightSupplier = manet.edgeWeightSupplier;
	this.pathSupplier = manet.pathSupplier;
	this.vertices = manet.vertices;
	this.sourceTargetAdjacencies = manet.sourceTargetAdjacencies;
	this.targetSourceAdjacencies = manet.targetSourceAdjacencies;
	this.edgeAdjacencies = manet.edgeAdjacencies;
	this.utilizationAdjacencies = manet.utilizationAdjacencies;
	this.capacity = manet.capacity;
	this.radioModel = manet.radioModel;
	this.mobilityModel = manet.mobilityModel;
	// Deep copy edges & flows (so that manipulations on the copy doesn't change the
	// original version while runtime)
	this.edges = manet.copyEdges();
	this.paths = manet.copyPaths(manet);
	this.utilization = new DataRate(manet.utilization.get());
    }

    public MANET<N, L, W, F> copy() {
	return new MANET<N, L, W, F>(this);
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

    @Override
    public F copyPath(int i) {
	F pathCopy = super.copyPath(i);

	// System.out.println(super.getPaths().get(i).toString());

	pathCopy.setDataRate(paths.get(i).getDataRate());

	// System.out.println(pathCopy.toString());

	return pathCopy;
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

	newLink.getWeight().setDistance(distance);
	newLink.getWeight().setTransmissionRate(radioModel.transmissionBitrate(distance));
	newLink.getWeight().setReceptionPower(radioModel.receptionPower(distance));
	newLink.getWeight().setUtilization(new DataRate(0));
	newLink.getWeight()
		.setSinkAndSourceMobility(new Tuple<List<MovementPattern>, List<MovementPattern>>(
			getVerticesOf(newLink).getFirst().getPrevMobility(),
			getVerticesOf(newLink).getSecond().getPrevMobility()));

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
	return getPaths();
    }

    public F getFlow(int id) {
	return paths.get(id);
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
	ListIterator<Tuple<L, N>> flowIterator = flow.listIterator(1);
	while (flowIterator.hasNext()) {
	    Tuple<L, N> linkAndNode = flowIterator.next();
	    L l = linkAndNode.getFirst();
	    l.getWeight().setPassive();
	    DataRate r = flow.getDataRate();
	    for (L ul : getUtilizedLinksOf(l)) {
		DataRate cUtilization = this.getEdge(ul).getWeight().getUtilization();
		cUtilization.set(cUtilization.get() - r.get());
		this.getEdge(ul).getWeight().setUtilization(cUtilization);
		this.utilization.set(this.utilization.get() - r.get());
	    }

	}
    }

    public void increaseUtilizationBy(L activeUtilizedLink, DataRate dataRate) {
	for (L link : getUtilizedLinksOf(activeUtilizedLink)) {
	    utilization.set(this.utilization.get() + dataRate.get());
	    link.getWeight().setUtilization(new DataRate(link.getWeight().getUtilization().get() + dataRate.get()));
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
	for (int i = 0; i < mobilityModel.getSegments() - 1; i++) {
	    movementPattern = mobilityModel.computeNextMovementPattern(movementPattern);
	    patternList.add(movementPattern);
	}
	n.setPrevMobility(patternList);
	return n;
    }

    public DataRate getOverUtilization() {
	DataRate overUtilization = new DataRate(0L);
	for (L l : this.getEdges()) {
	    if (l.getWeight().isActive()) {
		DataRate tRate = l.getWeight().getTransmissionRate();
		DataRate utilization = l.getWeight().getUtilization();
		double oU = tRate.get() - utilization.get();
		overUtilization.set(oU < 0 ? overUtilization.get() + (long) Math.abs(oU) : overUtilization.get());
	    }
	}
	return overUtilization;
    }

    public List<L> getActiveUtilizedLinks() {

	Set<L> activeUtilizedLinks = new HashSet<L>();

	for (F flow : paths)
	    activeUtilizedLinks.addAll(flow.getEdges());

	return activeUtilizedLinks.stream().collect(Collectors.toList());
    }

    // Returns all active utilized links of flows that are utilized by given link
    public List<L> getActiveUtilizedLinksOf(L link) {

	Set<L> activeUtilizedLinks = new HashSet<L>();

	// (1) Add all links that utilized by link to the set
	activeUtilizedLinks.addAll(getUtilizedLinksOf(link));

	// (2) Build intersection with all active utilized links
	activeUtilizedLinks.retainAll(getActiveUtilizedLinks());

	return activeUtilizedLinks.stream().collect(Collectors.toList());
    }

    /*
     * Method detects links which are interfered due to eventually overlapping
     * transmissions. Is not considered to be used during evaluations
     * 
     * private void setLinksInterferedByL(N n) { Iterator<L> iterator =
     * this.getEdges().iterator();
     * 
     * while (iterator.hasNext()) { L le = iterator.next(); N se =
     * this.getVerticesOf(le).getFirst(); N sk = this.getVerticesOf(le).getSecond();
     * 
     * if (radioModel.interferencePresent(this.getDistance(n.getPosition(),
     * se.getPosition())) &&
     * radioModel.interferencePresent(this.getDistance(n.getPosition(),
     * sk.getPosition()))) { n.setInterferedLink(le); } } }
     */

    public DataRate getUtilization() {
	return this.utilization;
    }

    public DataRate getCapacity() {
	return this.capacity;
    }
}