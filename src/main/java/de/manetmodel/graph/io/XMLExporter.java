package de.manetmodel.graph.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class XMLExporter<V extends Vertex, E extends Edge> {

	WeightedUndirectedGraph<V, E> graph;
	
	public XMLExporter(WeightedUndirectedGraph<V, E> graph) {	
		this.graph = graph;
	}
	
	public boolean exportGraph(String filePath) 
	{
		try 
		{			
			JAXBContext jaxbContent = JAXBContext.newInstance(graph.getClass());			
		    Marshaller marshaller= jaxbContent.createMarshaller();		    
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);			    
		    OutputStream outputStream = new FileOutputStream(filePath);		 		    
			marshaller.marshal(graph, outputStream);							
			outputStream.close();			
			return true;
        } 
		catch (JAXBException exception) 
		{
        	exception.printStackTrace();
        } 
		catch (IOException exception) 
		{
        	exception.printStackTrace();
        } 
		
		return false;		
	}
}
