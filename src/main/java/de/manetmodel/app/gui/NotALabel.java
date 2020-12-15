package de.manetmodel.app.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NotALabel {

    public static void main(String[] args) {
	new NotALabel();
    }

    public NotALabel() {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			| UnsupportedLookAndFeelException ex) {
		    ex.printStackTrace();
		}

		final JButton btn = new JButton("Am I label or a button?");
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setOpaque(true);
		btn.getModel().addChangeListener(new ChangeListener() {
		    @Override
		    public void stateChanged(ChangeEvent e) {
			ButtonModel model = (ButtonModel) e.getSource();
			if (model.isRollover()) {
			    btn.setBackground(Color.CYAN);
			} else {
			    btn.setBackground(null);
			}
		    }
		});

		btn.addActionListener(new ActionListener() {
		    private int count = 0;

		    @Override
		    public void actionPerformed(ActionEvent e) {
			count++;
			((JButton) e.getSource()).setText("I'm a super button!! Or label...");
		    }
		});

		JFrame frame = new JFrame("Testing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		frame.add(btn);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	    }
	});
    }
}
