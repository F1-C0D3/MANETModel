package de.manetmodel.graph.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

class DrawArc extends JComponent {

    private static Stroke VERTEX_PATH_STROKE = new BasicStroke(3);

    @Override
    public void paint(Graphics g) {
	//drawArc(int x, int y, int width, int length, int startAngle, int arcAngle)	
	
	Graphics2D g2 = (Graphics2D) g;
	
	g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
	
	g2.setStroke(VERTEX_PATH_STROKE);
	g2.setColor(Color.BLUE);
	g2.drawArc(0, 0, 150, 150, 0, 90);
	
	g2.setColor(Color.RED);
	g2.drawArc(0, 0, 150, 150, 90, 90);
	
	g2.setColor(Color.YELLOW);
	g2.drawArc(0, 0, 150, 150, 180, 90);
	
	g2.setColor(Color.GREEN);
	g2.drawArc(0, 0, 150, 150, 270, 90);
    }
}

public class DrawArcTest {
    public static void main(String[] a) {
	JFrame window = new JFrame();
	window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	window.setBounds(0, 0, 300, 300);
	window.getContentPane().add(new DrawArc());
	window.setVisible(true);
    }
}
