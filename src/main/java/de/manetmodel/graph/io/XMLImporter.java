package de.manetmodel.graph.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.manetmodel.graph.ManetEdge;
import de.manetmodel.graph.ManetGraph;
import de.manetmodel.graph.ManetVertex;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class XMLImporter {
	
	public XMLImporter() {}
	
	public ManetGraph<ManetVertex,ManetEdge> importGraph(String filePath) {
		try
		{						
			JAXBContext jaxbContext = JAXBContext.newInstance(ManetGraph.class);               
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
		    InputStream inputStream = new FileInputStream(filePath);
		    ManetGraph<ManetVertex,ManetEdge> graph = (ManetGraph<ManetVertex, ManetEdge>) jaxbUnmarshaller.unmarshal(inputStream);		   
		    return graph;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (JAXBException e) 
		{
		    e.printStackTrace();
		}
		return null;
	}
}
