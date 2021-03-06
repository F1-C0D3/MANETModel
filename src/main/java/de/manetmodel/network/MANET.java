package de.manetmodel.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import de.jgraphlib.graph.DirectedWeighted2DGraph;
import de.jgraphlib.graph.elements.Position2D;
import de.jgraphlib.util.Tuple;
import de.manetmodel.evaluator.LinkQualityEvaluator;
import de.manetmodel.mobilitymodel.MobilityModel;
import de.manetmodel.mobilitymodel.MovementPattern;
import de.manetmodel.radiomodel.RadioModel;
import de.manetmodel.units.DataRate;
import de.manetmodel.units.Speed;

public class MANET<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>>
	extends DirectedWeighted2DGraph<N, L, W, F> {

    protected RadioModel<N, L, W> radioModel;
    protected MobilityModel mobilityModel;
    protected DataRate utilization;
    protected DataRate capacity;
    protected TreeMap<Integer, List<Integer>> utilizationAdjacencies;
    protected LinkQualityEvaluator<N, L, W> linkQualityEvaluator;

    public MANET(Supplier<N> vertexSupplier, Supplier<L> edgeSupplier, Supplier<W> edgeWeightSupplier,
	    Supplier<F> flowSupplier, RadioModel<N, L, W> radioModel, MobilityModel mobilityModel,
	    LinkQualityEvaluator<N, L, W> linkQualityEvaluator) {
	super(vertexSupplier, edgeSupplier, edgeWeightSupplier, flowSupplier);
	this.radioModel = radioModel;
	this.mobilityModel = mobilityModel;
	this.capacity = new DataRate(0L);
	this.utilization = new DataRate(0L);
	this.utilizationAdjacencies = new TreeMap<Integer, List<Integer>>();
	this.linkQualityEvaluator = linkQualityEvaluator;
    }

    public MANET(MANET<N, L, W, F> manet) {
	super(manet.vertexSupplier, manet.edgeSupplier, manet.edgeWeightSupplier, manet.pathSupplier);
	this.vertices = manet.copyVertices();
	this.edges = manet.copyEdges();
	this.paths = manet.copyPaths(manet);
	this.utilization = new DataRate(manet.utilization.get());
	this.capacity = new DataRate(manet.capacity.get());
	this.radioModel = manet.radioModel;
	this.mobilityModel = manet.mobilityModel;
	this.sourceTargetAdjacencies = manet.copySourceTargetAdjacencies();
	this.targetSourceAdjacencies = manet.copyTargetSourceAdjacencies();
	this.edgeAdjacencies = manet.copyEdgeAdjacencies();
	this.utilizationAdjacencies = new TreeMap<Integer, List<Integer>>(manet.utilizationAdjacencies);
    }

    public MobilityModel getMobilityModel() {
	return mobilityModel;
    }

    public RadioModel<N, L, W> getRadioModel() {
	return radioModel;
    }

    public void setLinkQualtiyEvaluator(LinkQualityEvaluator<N, L, W> linkQualityEvaluator) {
	this.linkQualityEvaluator = linkQualityEvaluator;
    }

    public MANET<N, L, W, F> copy() {
	return new MANET<N, L, W, F>(this);
    }

    public TreeMap<Integer, N> copyVertices() {
	TreeMap<Integer, N> nodeCopies = new TreeMap<Integer, N>();
	for (N node : getVertices()) {
	    N nodeCopy = vertexSupplier.get();
	    nodeCopy.setID(node.getID());
	    if (!Objects.isNull(mobilityModel)) {
		List<MovementPattern> patternListCopy = new ArrayList<MovementPattern>();

		for (MovementPattern movementPattern : node.getMobilityCharacteristic()) {
		    Speed speedCopy = new Speed(movementPattern.getSpeed().value);
		    Position2D positionCopy = new Position2D(movementPattern.getPostion().x(),
			    movementPattern.getPostion().y());
		    MovementPattern movementPatternCopy = new MovementPattern(speedCopy, positionCopy,
			    movementPattern.getAngle());
		    patternListCopy.add(movementPatternCopy);
		}

		nodeCopy.setMobilityCharacteristic(patternListCopy);
	    }

	    if (!Objects.isNull(linkQualityEvaluator) && !Objects.isNull(radioModel))
		// I don't know another way howto obtain the maximum distance a link can have
		radioModel.setNodeRadioParameters(nodeCopy, radioModel.getLinkMaxTransmissionRange());
	    nodeCopies.put(node.getID(), nodeCopy);
	}
	return nodeCopies;
    }

    @Override
    public N addVertex(Position2D position) {

	N n = super.addVertex(position);

	if (!Objects.isNull(mobilityModel)) {

	    Speed initialSpeed = mobilityModel.initializeSpeed();

	    List<MovementPattern> patternList = new ArrayList<MovementPattern>();

	    MovementPattern movementPattern = new MovementPattern(initialSpeed, n.getPosition(), 0d);

	    patternList.add(movementPattern);

	    for (int i = 0; i < mobilityModel.getTicks() - 1; i++) {
		movementPattern = mobilityModel.computeNextMovementPattern(movementPattern);
		patternList.add(movementPattern);
	    }
	    Collections.reverse(patternList);
	    n.setMobilityCharacteristic(patternList);
	}
	if (!Objects.isNull(linkQualityEvaluator) && !Objects.isNull(radioModel))
	    // I don't know another way howto obtain the maximum distance a link can have
	    radioModel.setNodeRadioParameters(n, radioModel.getLinkMaxTransmissionRange());

	return n;
    }

    public TreeMap<Integer, L> copyEdges() {

	TreeMap<Integer, L> linkCopies = new TreeMap<Integer, L>();

	for (L link : getEdges()) {

	    L linkCopy = edgeSupplier.get();

	    linkCopy.setID(link.getID());

	    if (link.isActive())
		linkCopy.setActive();
	    else
		linkCopy.setPassive();

	    linkCopy.setUtilization(link.getUtilization());
	    linkCopy.setNumberOfUtilizedLinks(link.getNumberOfUtilizedLinks());
	    radioModel.setLinkRadioParameters(linkCopy, link.getWeight().getDistance());

	    W linkQualityCopy = edgeWeightSupplier.get();
	    linkQualityCopy.setDistance(link.getWeight().getDistance());
	    linkQualityCopy.setScore(link.getWeight().getScore());

	    linkCopy.setWeight(linkQualityCopy);
	    linkCopies.put(linkCopy.getID(), linkCopy);

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
	path.setDataRate(getPaths().get(i).getDataRate());
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

	    link.setNumberOfUtilizedLinks(utilizedLinks.size());

	    utilizationAdjacencies.put(link.getID(), utilizedLinks.stream().map(L::getID).collect(Collectors.toList()));
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

	L link = super.addEdge(source, target, weight);

	double distance = this.getDistance(source.getPosition(), target.getPosition());

	radioModel.setLinkRadioParameters(link, distance);
	link.setUtilization(new DataRate(0));

	if (!Objects.isNull(linkQualityEvaluator))
	    linkQualityEvaluator.compute(source, link, target);

	capacity.set(capacity.get() + link.getTransmissionRate().get());

	return link;
    }

    public void addFlows(List<F> flows) {
	for (F f : flows) {
	    addFlow(f);
	}
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
	    link.setActive();
	    increaseUtilizationBy(link, flow.getDataRate());
	}
    }

    public void deployFlows(List<F> flows) {
	for (F flow : flows) {
	    deployFlow(flow);
	}
    }

    public void undeployFlows() {
	for (L link : getEdges()) {
	    link.setUtilization(new DataRate(0L));
	    link.setPassive();
	}
	this.utilization = new DataRate(0L);
    }

    public void reset() {
	for (F flow : paths) {
	    undeployFlow(flow);
	    flow.clear();
	}
    }

    public void undeployFlow(F flow) {

	for (L l : flow.getEdges()) {

	    if (isFlowRegistered(flow)) {
		if (getFlowsContainingLink(l).size() <= 1)
		    l.setPassive();
	    } else {
		if (getFlowsContainingLink(l).size() < 1)
		    l.setPassive();
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

    public void increaseUtilizationBy(L link, DataRate dataRate) {

	link.setActive();

	for (L utilizedLink : getUtilizedLinksOf(link)) {
	    utilization.set(this.utilization.get() + dataRate.get());

	    utilizedLink.setUtilization(new DataRate(utilizedLink.getUtilization().get() + dataRate.get()));
	}
    }

    private void decreaseUtilizationBy(L activeUtilizedLink, DataRate dataRate) {
	for (L ul : getUtilizedLinksOf(activeUtilizedLink)) {
	    DataRate cUtilization = ul.getUtilization();
	    cUtilization.set(cUtilization.get() - dataRate.get());
	    ul.setUtilization(cUtilization);
	    this.utilization.set(this.utilization.get() - dataRate.get());
	}
    }

    // Determine max possible utilization based on DataRates of flows
    public DataRate maxPossibleUtilization() {

	// (1) Set DataRates
	for (L link : this.getEdges()) {
	    for (F flow : this.paths)
		increaseUtilizationBy(link, flow.getDataRate());
	}

	// (2) Record utilization
	DataRate utilization = new DataRate(this.getUtilization().get());

	// (3) Remove DataRates
	for (L link : this.getEdges()) {
	    for (F flow : this.paths)
		decreaseUtilizationBy(link, flow.getDataRate());
	}

	return utilization;

    }

    public N addVertex(double x, double y) {
	return addVertex(new Position2D(x, y));
    }

    public double getOverUtilizationPercentage() {
	return ((double) getOverUtilization().get() / (double) getCapacity().get()) * 100;
    }

    public DataRate getOverUtilization() {

	DataRate overUtilzation = new DataRate(0);

	for (L link : getOverUtilizedLinks())
	    if (link.isActive())
		overUtilzation = new DataRate(overUtilzation.get() + link.getOverUtilization().get());

	return overUtilzation;
    }

    public Boolean isOverutilized() {
	for (L link : getOverUtilizedLinks())
	    if (link.isActive())
		if (link.getOverUtilization().get() > 0)
		    return true;

	return false;
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
	    capacity = new DataRate(capacity.get() + link.getTransmissionRate().get());
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
	    if (link.isActive()) {
		if (link.getOverUtilization().get() > 0)
		    overutilizedLinks.add(link);
	    }
	}

	return overutilizedLinks;
    }

    public List<L> getOverUtilizedLinksOf(F flow) {

	List<L> overutilizedLinks = new ArrayList<L>();

	for (L link : flow.getEdges())
	    if (link.isActive())
		if (link.getOverUtilization().get() > 0)
		    overutilizedLinks.add(link);

	return overutilizedLinks;
    }

    public List<L> getActiveUtilizedLinks() {
	return this.getEdges().stream().filter(l -> l.isActive() == true).collect(Collectors.toList());
    }

    // Returns all active utilized links of flows that are utilized by given link
    public List<L> getPassiveUtilizedLinksOf(L link) {

	Set<L> activeUtilizedLinks = new HashSet<L>();

	// (1) Add all links that are utilized by link to the set
	activeUtilizedLinks.addAll(getUtilizedLinksOf(link));

	// (2) Build intersection with all active utilized links
	activeUtilizedLinks.retainAll(getActiveUtilizedLinks());

	return activeUtilizedLinks.stream().collect(Collectors.toList());
    }

    public DataRate getMaxUtilizationOf(L link) {

	long maxUtilization = 0;

	for (F flow : getFlows())
	    maxUtilization += getUtilizedLinksOf(link).size() * flow.getDataRate().get();

	return new DataRate(maxUtilization);
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

    public List<F> getAllPaths(F flow) {

	List<F> allPaths = new ArrayList<F>();

	allPaths = super.getAllPaths(flow.getSource(), flow.getTarget());

	for (F path : allPaths)
	    path.setDataRate(flow.getDataRate());

	return allPaths;
    }

    @Override
    public boolean removeEdge(L edge) {
	utilizationAdjacencies.remove(edge.getID());
	return super.removeEdge(edge);
    }

}