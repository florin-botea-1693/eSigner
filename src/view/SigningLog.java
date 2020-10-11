package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SigningLog extends JScrollPane 
{

	private JTextPane textPane;
	private Style successStyle;
	private Style errorStyle;
	
	public JTextPane getTextPane() {
		return this.textPane;
	}

	public SigningLog() 
	{
		
		//this.setPreferredSize(new Dimension(200, 100));
		this.textPane = new JTextPane();
		this.textPane.setBackground(Color.white);
		
		this.setViewportView(textPane);

		this.successStyle = this.textPane.addStyle("success", null);
		StyleConstants.setForeground(successStyle, Color.GREEN);
		this.errorStyle = this.textPane.addStyle("error", null);
		StyleConstants.setForeground(errorStyle, Color.RED);
	}

	public void logSuccessln(String message) {
		StyledDocument doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), (doc.getLength() > 0 ? "\n" : "")+message, successStyle);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void logErrorln(String message) {
		StyledDocument doc = textPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), (doc.getLength() > 0 ? "\n" : "")+message, errorStyle);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void clearLog() {
		StyledDocument doc = textPane.getStyledDocument();
		try {
			doc.remove(0, doc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
