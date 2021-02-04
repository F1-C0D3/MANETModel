package de.manetmodel.graph.SampleTopologies;

import de.manetmodel.graph.EdgeDistance;
import de.manetmodel.network.Link;
import de.manetmodel.network.Manet;
import de.manetmodel.network.Node;

public class TrainingTopology {
	Manet<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> manet;

	public TrainingTopology(Manet<Node<EdgeDistance>, Link<EdgeDistance>, EdgeDistance> manet) {
		this.manet = manet;
	}

	public void create() {

		Node<EdgeDistance> zero = manet.addVertex(0d, 0d);
		Node<EdgeDistance> one = manet.addVertex(50d, 7d);
		manet.addEdge(zero, one);
		Node<EdgeDistance> two = manet.addVertex(110d, 7d);
		manet.addEdge(one, two);

		Node<EdgeDistance> three = manet.addVertex(190d, 7d);
		manet.addEdge(two, three);

		Node<EdgeDistance> four = manet.addVertex(270d, 7d);
		manet.addEdge(three, four);

		Node<EdgeDistance> five = manet.addVertex(0d, 90d);
		manet.addEdge(zero, five);

		Node<EdgeDistance> six = manet.addVertex(50, 40d);
		manet.addEdge(one, six);
		manet.addEdge(zero, six);
		manet.addEdge(two, six);
		manet.addEdge(five, six);

		Node<EdgeDistance> seven = manet.addVertex(155, 75d);
		manet.addEdge(two, seven);
		manet.addEdge(three, seven);

		Node<EdgeDistance> eight = manet.addVertex(110, 70d);
		manet.addEdge(six, eight);
		manet.addEdge(eight, seven);
		manet.addEdge(eight, two);

		Node<EdgeDistance> nine = manet.addVertex(220d, 105d);
		manet.addEdge(seven, nine);
		manet.addEdge(three, nine);

		Node<EdgeDistance> ten = manet.addVertex(114d, 109d);
		manet.addEdge(seven, ten);
		manet.addEdge(eight, ten);

		Node<EdgeDistance> eleven = manet.addVertex(53d, 155d);
		manet.addEdge(eleven, five);
		manet.addEdge(eleven, ten);

		Node<EdgeDistance> twelve = manet.addVertex(290d, 163d);
		manet.addEdge(nine, twelve);

		Node<EdgeDistance> thirteen = manet.addVertex(51d, 217d);
		manet.addEdge(eleven, thirteen);

		Node<EdgeDistance> fourteen = manet.addVertex(97d, 195d);
		manet.addEdge(thirteen, fourteen);
		manet.addEdge(eleven, fourteen);
		manet.addEdge(ten, fourteen);

		Node<EdgeDistance> fivteen = manet.addVertex(115d, 280d);
		manet.addEdge(thirteen, fivteen);
		manet.addEdge(fourteen, fivteen);

		Node<EdgeDistance> sixteen = manet.addVertex(149d, 265d);
		manet.addEdge(sixteen, fivteen);
		manet.addEdge(sixteen, fourteen);

		Node<EdgeDistance> seventeen = manet.addVertex(169d, 188d);
		manet.addEdge(fourteen, seventeen);
		manet.addEdge(sixteen, seventeen);
		manet.addEdge(nine, seventeen);
		manet.addEdge(seven, seventeen);

		Node<EdgeDistance> eighteen = manet.addVertex(14d, 229d);
		manet.addEdge(five, eighteen);
		manet.addEdge(thirteen, eighteen);
		manet.addEdge(eleven, eighteen);

		Node<EdgeDistance> nineteen = manet.addVertex(21d, 310d);
		manet.addEdge(eighteen, nineteen);

		Node<EdgeDistance> twenty = manet.addVertex(33d, 385d);
		manet.addEdge(nineteen, twenty);
		Node<EdgeDistance> twentyOne = manet.addVertex(42d, 479d);
		manet.addEdge(twentyOne, twenty);

		Node<EdgeDistance> twentytwo = manet.addVertex(115d, 339d);
		manet.addEdge(fivteen, twentytwo);
		manet.addEdge(sixteen, twentytwo);

		Node<EdgeDistance> twentythree = manet.addVertex(88d, 300d);
		manet.addEdge(twentytwo, twentythree);
		manet.addEdge(fivteen, twentytwo);
		manet.addEdge(thirteen, twentythree);

		Node<EdgeDistance> twentyfour = manet.addVertex(66d, 321d);
		manet.addEdge(thirteen, twentyfour);
		manet.addEdge(twentythree, twentyfour);
		manet.addEdge(twentytwo, twentyfour);
		manet.addEdge(fivteen, twentyfour);
		manet.addEdge(nineteen, twentyfour);
		manet.addEdge(twenty, twentyfour);

		Node<EdgeDistance> twentyfive = manet.addVertex(84d, 429d);
		manet.addEdge(twentyfive, twentytwo);
		manet.addEdge(twentyfive, twentyOne);
		manet.addEdge(twentyfive, twenty);

		Node<EdgeDistance> twentysix = manet.addVertex(359d, 205d);
		manet.addEdge(twelve, twentysix);

		Node<EdgeDistance> twentyseven = manet.addVertex(216d, 255d);
		manet.addEdge(seventeen, twentyseven);
		manet.addEdge(sixteen, twentyseven);

		Node<EdgeDistance> twentyeight = manet.addVertex(192d, 247d);
		manet.addEdge(seventeen, twentyeight);
		manet.addEdge(sixteen, twentyeight);
		manet.addEdge(twentyseven, twentyeight);

		Node<EdgeDistance> twentyNine = manet.addVertex(270d, 240d);
		manet.addEdge(twelve, twentyNine);
		manet.addEdge(twentyseven, twentyNine);

		Node<EdgeDistance> thirty = manet.addVertex(319d, 84d);
		manet.addEdge(four, thirty);
		manet.addEdge(thirty, twelve);
	}

}
