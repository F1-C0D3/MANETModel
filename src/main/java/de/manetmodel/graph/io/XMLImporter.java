package de.manetmodel.graph.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.manetmodel.graph.UndirectedWeightedGraph;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedEdge;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XMLImporter<V extends Vertex<P>, P, E extends WeightedEdge<W>,W> {

    UndirectedWeightedGraph<V, P, E, W> graph;

    public XMLImporter(UndirectedWeightedGraph<V, P, E, W> graph) {
	this.graph = graph;
    }
    
    /*
    public boolean importGraph(String filePath) {
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(graph.getClass());
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    InputStream inputStream = new FileInputStream(filePath);
	    graph = (WeightedUndirectedGraph<V, E>) jaxbUnmarshaller.unmarshal(inputStream);
	    return true;
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}

	return false;
    }*/
}

