package de.manetmodel.app.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class TerminalPanel extends JPanel {

    JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane();
    private Font font = new Font("Monospaced", Font.PLAIN, 20);
    
    Consumer<String> inputListener;

    public TerminalPanel() {
	this.setLayout(new BorderLayout());

	this.scrollPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

	this.textArea.addKeyListener(new KeyListener());
	this.textArea.setFont(font);
	this.textArea.setOpaque(false);
	this.textArea.setBackground(new Color(0, 0, 0, 0));
	this.textArea.setForeground(Color.WHITE);

	this.scrollPane.setOpaque(false);
	this.scrollPane.setBackground(new Color(0, 0, 0, 0));
	this.scrollPane.getViewport().setOpaque(false);
	this.scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
	this.scrollPane.setViewportView(textArea);

	this.add(scrollPane);
	
	this.showPrompt();
    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		JFrame frame = new JFrame("Terminal");
		TerminalPanel term = new TerminalPanel();
		frame.setBounds(0, 0, 1000, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(term);
		term.showPrompt();
	    }
	});
    }
    
    public void addMessage(String message) {
	this.textArea.append(message);
	this.showPrompt();
    }
    
    public void addInputListener(Consumer<String> inputListener) {
	this.inputListener = inputListener;
    }

    public void paintComponent(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
	g.setColor(getBackground());
	Rectangle r = g.getClipBounds();
	g.fillRect(r.x, r.y, r.width, r.height);
	super.paintComponent(g);
    }

    public void clear() {
	textArea.setText("");
	showPrompt();
    }

    private void showPrompt() {
	textArea.setText(textArea.getText() + "> ");
    }

    private void showNewLine() {
	textArea.setText(textArea.getText() + System.lineSeparator());
    }

    private class KeyListener extends KeyAdapter {

	private final String BACK_SPACE_KEY_BINDING = getKeyBinding(textArea.getInputMap(), "BACK_SPACE");
	private final int INITIAL_CURSOR_POSITION = 2;

	private boolean isKeysDisabled;
	private int minCursorPosition = INITIAL_CURSOR_POSITION;

	private String getKeyBinding(InputMap inputMap, String name) {
	    return (String) inputMap.get(KeyStroke.getKeyStroke(name));
	}

	public void keyPressed(KeyEvent e) {
	    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
		int cursorPosition = textArea.getCaretPosition();
		if (cursorPosition == minCursorPosition && !isKeysDisabled)
		    disableBackspaceKey();
		else if (cursorPosition > minCursorPosition && isKeysDisabled)
		    enableBackspaceKey();

	    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		disableTerminal();
		executeCommand(extractCommand());
		showNewLine();
		showPrompt();
		enableTerminal();
	    }
	}

	public void keyReleased(KeyEvent e) {
	    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		textArea.setCaretPosition(textArea.getCaretPosition() - 1);
		setMinCursorPosition();
	    }
	}

	private void disableBackspaceKey() {
	    isKeysDisabled = true;
	    textArea.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
	}

	private void enableBackspaceKey() {
	    isKeysDisabled = false;
	    textArea.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), BACK_SPACE_KEY_BINDING);
	}

	private void setMinCursorPosition() {
	    minCursorPosition = textArea.getCaretPosition();
	}
    }

    public void enableTerminal() {
	textArea.setEnabled(true);
    }

    public void disableTerminal() {
	textArea.setEnabled(false);
    }

    private void executeCommand(String command) {
	inputListener.accept(command);
    }

    private String extractCommand() {
	removeLastLineSeparator();
	String newCommand = stripPreviousCommands();
	return newCommand;
    }

    private void removeLastLineSeparator() {
	String terminalText = textArea.getText();
	if(terminalText.endsWith(System.lineSeparator())) {
	    terminalText = terminalText.substring(0, terminalText.length() - 1);
	}
	textArea.setText(terminalText);
    }

    private String stripPreviousCommands() {
	String terminalText = textArea.getText();
	int lastPromptIndex = terminalText.lastIndexOf('>') + 2;
	if (lastPromptIndex < 0 || lastPromptIndex >= terminalText.length())
	    return "";
	else
	    return terminalText.substring(lastPromptIndex);
    }
}