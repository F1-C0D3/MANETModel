package de.manetmodel.graph.gui;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

class DrawArc extends JComponent {

    public void paint(Graphics g) {
	//drawArc(int x, int y, int width, int length, int startAngle, int arcAngle)
	g.drawArc(150, 150, 150, 150, 0, 180);
    }
}

public class DrawArcTest {
    public static void main(String[] a) {
	JFrame window = new JFrame();
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	window.setBounds(0, 0, 300, 300);
	window.getContentPane().add(new DrawArc());
	window.setVisible(true);
    }
}
