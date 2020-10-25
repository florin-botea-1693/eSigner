package view.components;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class SigningMessage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton cancelButton;
	private JLabel currentFile;
	private JProgressBar progressBar;
	private JLabel range;

	public SigningMessage() {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Signing...");
		setBounds(100, 100, 308, 98);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		currentFile = new JLabel("Signing...");
		currentFile.setBounds(10, 11, 272, 14);
		contentPanel.add(currentFile);
		
		range = new JLabel("0/0");
		range.setBounds(172, 36, 46, 14);
		contentPanel.add(range);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 36, 152, 14);
		contentPanel.add(progressBar);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(217, 27, 65, 23);
		contentPanel.add(cancelButton);
	}
	
	public JButton getCancelButton() {
		return cancelButton;
	}
	
	public void update(String signingFile, int[] range) {
		this.currentFile.setText("Signing " + signingFile);
		this.range.setText(String.valueOf(range[0]) + "/" + String.valueOf(range[1]));
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(range[1]);
		this.progressBar.setValue(range[0]);
		this.getContentPane().revalidate();
		this.getContentPane().repaint();
	}
}
