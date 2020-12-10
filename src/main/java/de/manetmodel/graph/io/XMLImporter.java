package de.manetmodel.graph.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XMLImporter<V extends Vertex, E extends Edge>{
	
	WeightedUndirectedGraph<V, E> graph;
	
	public XMLImporter(WeightedUndirectedGraph<V,E> graph) {
		this.graph = graph;
	}
	
	public boolean importGraph(String filePath)
	{
		try
		{						
			JAXBContext jaxbContext = JAXBContext.newInstance(graph.getClass());  			
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 	    
		    InputStream inputStream = new FileInputStream(filePath);		    
		    graph = (WeightedUndirectedGraph<V, E>) jaxbUnmarshaller.unmarshal(inputStream);    
		    return true;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (JAXBException e) 
		{
		    e.printStackTrace();
		}
		
		return false;
	}
}
