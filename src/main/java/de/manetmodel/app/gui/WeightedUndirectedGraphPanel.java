package de.manetmodel.app.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.manetmodel.graph.Edge;
import de.manetmodel.graph.Path;
import de.manetmodel.graph.Vertex;
import de.manetmodel.graph.WeightedUndirectedGraph;
import de.manetmodel.graph.WeightedUndirectedGraphSupplier;
import de.manetmodel.graph.viz.VisualEdge;
import de.manetmodel.graph.viz.VisualGraph;
import de.manetmodel.graph.viz.VisualVertex;
import de.manetmodel.util.Tuple;

public class WeightedUndirectedGraphPanel<V extends Vertex, E extends Edge> extends JPanel {
		
	private VisualGraph<V,E> graph;	
	private Scope scope;	
    private int vertexWidth = 50; 
    private int padding = vertexWidth;
    private static final Stroke EDGE_STROKE = new BasicStroke(2);  
    private static final Stroke EDGE_PATH_STROKE = new BasicStroke(10);
    private static final Stroke VERTEX_STROKE = new BasicStroke(2);  
    double xScale;   
    double yScale; 

    public WeightedUndirectedGraphPanel(VisualGraph<V,E> graph) {
        this.graph = graph;      
        this.scope = this.getScope(graph);
    }
    
    public void paintPlayground(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(padding, padding, getWidth() - (2 * padding), getHeight() - 2 * padding);
    }
        
    public void paintEdge(Graphics2D g2, VisualEdge edge) {
    	int x1 = (int) ((graph.getVertices().get(edge.getSource()).x()) * xScale + padding);	
    	int y1 = (int) ((scope.y.max - graph.getVertices().get(edge.getSource()).y()) * yScale + padding); 
    	int x2 = (int) ((graph.getVertices().get(edge.getTarget()).x()) * xScale + padding);	
    	int y2 = (int) ((scope.y.max - graph.getVertices().get(edge.getTarget()).y()) * yScale + padding); 
    	
    	g2.setStroke(EDGE_PATH_STROKE);
        g2.setColor(Color.LIGHT_GRAY);   
    	g2.drawLine(x1, y1, x2, y2);	
    	
    	g2.setStroke(EDGE_STROKE);
        g2.setColor(edge.getColor());   
    	g2.drawLine(x1, y1, x2, y2);
    	
    	Point lineCenter = new Point(x1/2+x2/2, y1/2+y2/2);      	        	
    	String str = String.format("%d: %s", edge.getID(), edge.getText());	
    	FontMetrics fm = g2.getFontMetrics();
        Rectangle2D stringBounds = fm.getStringBounds(str, g2);        
        g2.setColor(Color.GRAY);
        g2.setColor(Color.BLACK); 
        g2.drawString(str, lineCenter.x - (int) stringBounds.getCenterX(), lineCenter.y - (int) stringBounds.getCenterY());  
    }
    
    public void paintVertex(Graphics2D g2, VisualVertex vertex) {
    	int x = (int) ((vertex.x() * xScale + padding) - vertexWidth / 2);	
    	int y = (int) (((scope.y.max - vertex.y()) * yScale + padding) - vertexWidth / 2); 
    	  	
    	g2.setStroke(VERTEX_STROKE);
    	g2.setColor(vertex.getBackgroundColor());
        g2.fillOval(x, y, vertexWidth, vertexWidth);
        g2.setColor(vertex.getBorderColor());
        g2.drawOval(x, y, vertexWidth, vertexWidth); 
   
        String str = Integer.toString(vertex.getID());
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D stringBounds = fm.getStringBounds(str, g2);  
        Point vertexCenter = new Point(x+(vertexWidth/2), y+(vertexWidth/2));
        g2.drawString(str, vertexCenter.x - (int) stringBounds.getCenterX(), vertexCenter.y - (int) stringBounds.getCenterY());
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;     
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if(graph.getVertices().isEmpty()) return;
        
        this.xScale = ((double) getWidth() - 2 * padding) / (scope.x.max);
        this.yScale = ((double) getHeight() - 2 * padding) / (scope.y.max);      

        this.paintPlayground(g2);
                    
        for(VisualEdge edge : graph.getEdges())      	
        	this.paintEdge(g2, edge);
           
        for(VisualVertex vertex : graph.getVertices())  	        	
        	this.paintVertex(g2, vertex);          
    } 
 
	private class Scope {	
	    Range x;
	    Range y;  	
	    boolean isSet = false;     	
	    @Override
    	public String toString() {
            return String.format("x:%d-%d, y:%d-%d", this.x.min, this.x.max, this.y.min, this.y.max);
    	}
    }
    

    private class Range {	
		double min;
		double max;	
		public Range(double min, double max) {
			this.min = min;
			this.max = max;
		}
	}
	
	private Scope getScope(VisualGraph<V,E> graph) {
		
		Scope scope = new Scope();
		        
        for(VisualVertex node : graph.getVertices()) {
     			
			if(!scope.isSet) {
				scope.x = new Range(node.x(), node.x());
				scope.y = new Range(node.y(), node.y());
				scope.isSet = true;
			}
			else {				
				if(node.x() > scope.x.max)
					scope.x.max = node.x();
				else if(node.x() < scope.x.min)
					scope.x.min = node.x();
				
				if(node.y() > scope.y.max)
					scope.y.max = node.y();
				else if(node.y() < scope.y.min)
					scope.y.min = node.y();				
			}   			
		}
        
		return scope;
	}
	
	public VisualGraph<V,E> getVisualGraph(){
		return this.graph;
	}
	
	public void updateVisualGraph(VisualGraph<V,E> graph) {
		this.graph = graph;
        this.scope = this.getScope(graph);
	}
    
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {        	    		
    			
    			WeightedUndirectedGraph<Vertex, Edge> graph = 
    					new WeightedUndirectedGraph<Vertex, Edge>(
    							new WeightedUndirectedGraphSupplier.VertexSupplier(), 
    							new WeightedUndirectedGraphSupplier.EdgeSupplier());	
    			
    			Vertex source = graph.addVertex(0d, 0d);
    			Vertex a = graph.addVertex(41.21, 56.24);
    			Vertex b = graph.addVertex(76.51, 8.77);
    			Vertex c = graph.addVertex(92.88, 38.26);
    			Vertex target = graph.addVertex(147.49, 99.35);
    			Edge sourceToA = graph.addEdge(source, a);
    			Edge sourceToB = graph.addEdge(source, b);
    			Edge aToB = graph.addEdge(a, b);
    			Edge aToC = graph.addEdge(a, c);
    			Edge bToC = graph.addEdge(b, c);
    			Edge cToTarget = graph.addEdge(c, target);
    			 		
    			Path<Vertex, Edge> path = new Path<Vertex, Edge>(source);
    			path.add(new Tuple<Edge, Vertex>(sourceToB, b));
    			path.add(new Tuple<Edge, Vertex>(bToC, c));
    			path.add(new Tuple<Edge, Vertex>(cToTarget, target));
    			
    			WeightedUndirectedGraphPanel<Vertex, Edge> panel = new WeightedUndirectedGraphPanel<Vertex, Edge>(graph.toVisualGraph());
    			panel.getVisualGraph().addPath(path, Color.RED);
	       			       			
	       		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	       		int width = (int) screenSize.getWidth() * 3/4;
	       		int height = (int) screenSize.getHeight() * 3/4;
	       		panel.setPreferredSize(new Dimension(width, height));
	       		panel.setFont(new Font("Consolas", Font.PLAIN, 16));  
	       		panel.setLayout(null);
	       		JFrame frame = new JFrame("VisualGraphPanel");
	       		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       		frame.getContentPane().add(panel);
	       		frame.pack();
	       		frame.setLocationRelativeTo(null);
	       		frame.setVisible(true);  	       		
    		}
        });
    }
}