package de.manetmodel.graph.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.manetmodel.graph.ManetEdge;
import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetVertex;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class XMLExporter<G extends ManetGraph<? extends ManetVertex, ? extends ManetEdge>> {

	public XMLExporter() {}
	
	public boolean exportGraph(G graph, String filePath) {
		
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
