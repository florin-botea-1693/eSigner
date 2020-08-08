package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SigningMessage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField signingFile;
	private JButton cancelButton;

	public SigningMessage() {
		//this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Signing...");
		setBounds(100, 100, 308, 110);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		signingFile = new JTextField();
		signingFile.setBounds(10, 11, 272, 20);
		contentPanel.add(signingFile);
		signingFile.setColumns(10);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(217, 37, 65, 23);
		contentPanel.add(cancelButton);
	}
	
	public JButton getCancelButton() {
		return cancelButton;
	}
	
	public JTextField getSigningFile() {
		return signingFile;
	}
}
