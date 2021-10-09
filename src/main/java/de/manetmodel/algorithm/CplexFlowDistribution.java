package de.manetmodel.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import de.jgraphlib.graph.elements.Path;
import de.jgraphlib.util.Log;
import de.jgraphlib.util.Log.HeaderLevel;
import de.jgraphlib.util.Timer;
import de.jgraphlib.util.Tuple;
import de.manetmodel.network.Flow;
import de.manetmodel.network.Link;
import de.manetmodel.network.LinkQuality;
import de.manetmodel.network.MANET;
import de.manetmodel.network.Node;
import de.manetmodel.network.scalar.ScalarRadioFlow;
import de.manetmodel.network.scalar.ScalarRadioLink;
import de.manetmodel.network.scalar.ScalarRadioNode;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.cplex.IloCplex;

public class CplexFlowDistribution<N extends Node, L extends Link<W>, W extends LinkQuality, F extends Flow<N, L, W>> {

    Log log;
    Timer timer;
    List<F> feasibleDistribution;

    public CplexFlowDistribution() {
	log = new Log();
	timer = new Timer();
	feasibleDistribution = new ArrayList<F>();
    }

    /*
     * Returns undeployed flows incl. paths in feasibleDistribution if solution
     * exists. Otherwise, feasibleDistribution is empty.
     */
    public <M extends MANET<N, L, W, F>> List<F> generateFeasibleSolution(M manet) {

	log.infoHeader(HeaderLevel.h1, "(1) Computation");

	timer.start();

	try (IloCplex cplex = new IloCplex()) {

	    // Decision variables
	    IloIntVar[][] x_f_l = new IloIntVar[manet.getFlows().size()][manet.getEdges().size()];
	    IloIntVar[] a_l = new IloIntVar[manet.getEdges().size()];

	    for (F f : manet.getFlows()) {
		for (L link : manet.getEdges()) {

		    x_f_l[f.getID()][link.getID()] = cplex.boolVar();
		    x_f_l[f.getID()][link.getID()].setName(String.format("x^%d_[%d]_(%d,%d)", f.getID(), link.getID(),
			    manet.getVerticesOf(link).getFirst().getID(),
			    manet.getVerticesOf(link).getSecond().getID()));

		    a_l[link.getID()] = cplex.boolVar();
		    a_l[link.getID()].setName(String.format("a_l_[%d]_(%d,%d)", link.getID(),
			    manet.getVerticesOf(link).getFirst().getID(),
			    manet.getVerticesOf(link).getSecond().getID()));
		}
	    }

		/*
		 * Guarantee same amount at incoming edges goes out at outgoing edges This also
		 * includes unsplittable path guarantee
		 */

		for (F f : manet.getFlows()) {

			for (N node : manet.getVertices()) {

				List<L> incomingEdgesOf = manet.getIncomingEdgesOf(node);
				List<L> outgoingEdgesOf = manet.getOutgoingEdgesOf(node);
				IloLinearIntExpr unsplittablePathIncoming = cplex.linearIntExpr();
				IloLinearIntExpr unsplittablePathOutgoing = cplex.linearIntExpr();
				IloLinearIntExpr nodeEqualDemand = cplex.linearIntExpr();
				
				for (L link : incomingEdgesOf) {
					nodeEqualDemand.addTerm(+(int) f.getDataRate().get(), x_f_l[f.getID()][link.getID()]);
					unsplittablePathIncoming.addTerm(+1, x_f_l[f.getID()][link.getID()]);
					unsplittablePathOutgoing.addTerm(0, x_f_l[f.getID()][link.getID()]);
				}

				for (L link : outgoingEdgesOf) {
					nodeEqualDemand.addTerm(-(int) f.getDataRate().get(), x_f_l[f.getID()][link.getID()]);
					unsplittablePathOutgoing.addTerm(+1, x_f_l[f.getID()][link.getID()]);
					unsplittablePathIncoming.addTerm(0, x_f_l[f.getID()][link.getID()]);
				}

				if (node.getID() == f.getSource().getID()) {
					cplex.addEq(-f.getDataRate().get(), nodeEqualDemand);
					cplex.addEq(1, unsplittablePathOutgoing);
					cplex.addEq(0, unsplittablePathIncoming);

				} else if (node.getID() == f.getTarget().getID()) {
					cplex.addEq(+f.getDataRate().get(), nodeEqualDemand);
					cplex.addEq(1, unsplittablePathIncoming);
					cplex.addEq(0, unsplittablePathOutgoing);
				} else {
					cplex.addEq(0, nodeEqualDemand);
					cplex.addGe(1, unsplittablePathIncoming);
					cplex.addGe(1, unsplittablePathOutgoing);
//					cplex.addEq(0, cplex.diff(unsplittablePathIncoming, unsplittablePathOutgoing));
				}

			}

		}

	    /*
	     * if x^f_l == 1 -> y_l ==0 Identifies if a link is an active link or not
	     */
	    for (L currnetlink : manet.getEdges()) {

		for (F f : manet.getFlows()) {

		    cplex.add(cplex.ifThen(cplex.eq(x_f_l[f.getID()][currnetlink.getID()], 1),
			    cplex.eq(a_l[currnetlink.getID()], 0)));
		}
	    }

	    /*
	     * Capacity constraint: Ensures that if a link is active, c_l of l is greater or
	     * equal the utilization
	     */
	    for (L currentlink : manet.getEdges()) {

		IloLinearIntExpr flowExpression = cplex.linearIntExpr();

		for (F f : manet.getFlows()) {

		    for (L ul : manet.getUtilizedLinksOf(currentlink)) {

			flowExpression.addTerm((int) f.getDataRate().get(), x_f_l[f.getID()][ul.getID()]);
			flowExpression.addTerm(-(int) f.getDataRate().get(), a_l[currentlink.getID()]);

		    }

		}

		cplex.addGe((int) currentlink.getTransmissionRate().get(), flowExpression);
	    }

	    /*
	     * Setting each link weight to 1
	     */
	    double[][] linkWeightMatrix = new double[manet.getFlows().size()][manet.getEdges().size()];

	    for (int k = 0; k < linkWeightMatrix.length; k++) {

		for (int i = 0; i < linkWeightMatrix[k].length; i++) {

		    linkWeightMatrix[k][i] = 1;
		}
	    }

	    IloNumExpr[] linkWeightExpr = new IloNumExpr[manet.getFlows().size()];

	    for (int i = 0; i < linkWeightMatrix.length; i++) {
		linkWeightExpr[i] = cplex.scalProd(linkWeightMatrix[i], x_f_l[i]);
	    }

	    cplex.addMinimize(cplex.sum(linkWeightExpr));
	    cplex.setParam(IloCplex.Param.MIP.Limits.Solutions, 1);
	    cplex.setParam(IloCplex.Param.Threads, Runtime.getRuntime().availableProcessors());

	    if (cplex.solve()) {

		log.infoHeader(HeaderLevel.h2, "(2) Found deployment");
		log.info("Given setting returns is a feasible solution.");

		// Create and deploy found flows
		for (int i = 0; i < x_f_l.length; i++) {
		    F flow = manet.getFlow(i);
		    int index = 0;
		    N node = flow.getSource();
		    while (node.getID() != flow.getTarget().getID()) {

			List<L> oLinks = manet.getOutgoingEdgesOf(node);

			for (L link : oLinks) {
			    if (cplex.getValue(x_f_l[i][link.getID()]) >=0.90) {
				flow.add(new Tuple<L, N>(link, manet.getTargetOf(link)));
				break;
			    }
			}
			index++;
			node = flow.get(index).getSecond();
		    }

		    feasibleDistribution.add(flow);
		}
	    }

	    return feasibleDistribution;
	} catch (IloException e) {
	    e.printStackTrace();
	}

	return feasibleDistribution;
    }
}
