package de.manetgraph.visualization;

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

import de.manetgraph.ManetEdge;
import de.manetgraph.ManetGraph;
import de.manetgraph.ManetGraphSupplier;
import de.manetgraph.ManetVertex;

public class ManetGraphPanel extends JPanel {
		
	ManetGraph<ManetVertex, ManetEdge> graph;
	Scope scope;	
	
    private int vertexWidth = 50;
    private int padding = vertexWidth;
    private static final Stroke EDGE_STROKE = new BasicStroke(2);
    private static final Stroke VERTEX_STROKE = new BasicStroke(2);
    private Color edgeColor = Color.BLACK;
    private Color vertexColor = Color.LIGHT_GRAY;

    public ManetGraphPanel(ManetGraph<ManetVertex, ManetEdge> graph) {
        this.graph = graph;      
        this.scope = this.getScope(graph);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - 2 * padding) / (scope.x.max);
        double yScale = ((double) getHeight() - 2 * padding) / (scope.y.max);   
        
        g2.setColor(Color.WHITE);
        g2.fillRect(padding, padding, getWidth() - (2 * padding), getHeight() - 2 * padding);
        
        g2.setStroke(EDGE_STROKE);
        g2.setColor(edgeColor);
        
        for(ManetEdge edge : graph.edgeSet()) {
        	
        	ManetVertex source = graph.getEdgeSource(edge);
        	ManetVertex target = graph.getEdgeTarget(edge);
        	
        	int x1 = (int) ((source.x()) * xScale + padding);	
        	int y1 = (int) ((scope.y.max - source.y()) * yScale + padding); 
        	int x2 = (int) ((target.x()) * xScale + padding);	
        	int y2 = (int) ((scope.y.max - target.y()) * yScale + padding); 
        	g2.drawLine(x1, y1, x2, y2);

        	Point lineCenter = new Point(x1/2+x2/2, y1/2+y2/2); 
        	        	
        	String str = String.format("%.2f", edge.getWeight());	
        	FontMetrics fm = g2.getFontMetrics();
            Rectangle2D stringBounds = fm.getStringBounds(str, g2);        
            g2.setColor(Color.GRAY);
            g2.setColor(Color.BLACK); 
            g2.drawString(str, lineCenter.x - (int) stringBounds.getCenterX(), lineCenter.y - (int) stringBounds.getCenterY());  
            
        }
                
        for(ManetVertex vertex : graph.vertexSet()) {
        	
            int x = (int) ((vertex.x() * xScale + padding) - vertexWidth / 2);	
        	int y = (int) (((scope.y.max - vertex.y()) * yScale + padding) - vertexWidth / 2); 
        	
        	g2.setStroke(VERTEX_STROKE);
        	g2.setColor(vertexColor);
            g2.fillOval(x, y, vertexWidth, vertexWidth);
            g2.setColor(edgeColor);
            g2.drawOval(x, y, vertexWidth, vertexWidth);   
            String str = Integer.toString(vertex.getID());
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D stringBounds = fm.getStringBounds(str, g2);  
            Point vertexCenter = new Point(x+(vertexWidth/2), y+(vertexWidth/2));
            g2.drawString(str, vertexCenter.x - (int) stringBounds.getCenterX(), vertexCenter.y - (int) stringBounds.getCenterY());
        }      
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
	
	private Scope getScope(ManetGraph<ManetVertex,ManetEdge> graph) {
		
		Scope scope = new Scope();
		
		for(ManetVertex vertex : graph.vertexSet()) {
		
			if(!scope.isSet) {
				scope.x = new Range(vertex.x(), vertex.x());
				scope.y = new Range(vertex.y(), vertex.y());
				scope.isSet = true;
			}
			else {				
				if(vertex.x() > scope.x.max)
					scope.x.max = vertex.x();
				else if(vertex.x() < scope.x.min)
					scope.x.min = vertex.x();
				
				if(vertex.y() > scope.y.max)
					scope.y.max = vertex.y();
				else if(vertex.y() < scope.y.min)
					scope.y.min = vertex.y();				
			}   			
		}	
		
		return scope;
	}

    
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {        
    		    			
    			ManetGraph<ManetVertex, ManetEdge> graph = new ManetGraph<ManetVertex, ManetEdge>(new ManetGraphSupplier.ManetVertexSupplier(), new ManetGraphSupplier.ManetEdgeSupplier());
    			graph.generateRandomGraph();
    	
	       		ManetGraphPanel panel = new ManetGraphPanel(graph);
	       		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	       		int width = (int) screenSize.getWidth() * 3/4;
	       		int height = (int) screenSize.getHeight() * 3/4;
	       		panel.setPreferredSize(new Dimension(width, height));
	       		panel.setFont(new Font("Consolas", Font.PLAIN, 16));  
	       		panel.setLayout(null);
	       		JFrame frame = new JFrame("AcoGraph");
	       		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       		frame.getContentPane().add(panel);
	       		frame.pack();
	       		frame.setLocationRelativeTo(null);
	       		frame.setVisible(true);  
	
    		}
        });
    }
}