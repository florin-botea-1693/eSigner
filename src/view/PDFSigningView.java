package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentListener;

import org.w3c.dom.events.DocumentEvent;

import model.PDFSignerModel;
import model.certificates.Certificate;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class PDFSigningView extends JPanel {

	private PDFSignerModel signingModel;
	
	private JTextField choosedFilesInput;
	private JButton chooseFilesButton;
	private JComboBox certificateSelector;
	private JButton performSignButton;
	private JTextField signingReason; 
	private JTextField signingLocation; 
	private JLabel signingModeL;
	private JComboBox visibility;
	private JCheckBox realSignature;
	private JComboBox signingPage;
	private JTextField customPage;
	private JComboBox size;
	private JComboBox position;
	private JLabel lblNewLabel;
	private JTextArea signingLog;
	private JCheckBox visibleReason;
	private JCheckBox visibleLocation;
	private JLabel customPageL;
	private JCheckBox visibleSN;
	private JLabel snLabel;
	
	public JTextField getChoosedFilesInput() {return choosedFilesInput;}
	public JButton getChooseFilesButton() {return chooseFilesButton;}
	public JComboBox getCertificateSelector() {return certificateSelector;}
	public JButton getPerformSignButton() {return performSignButton;}
	public JTextField getSigningReason() {return signingReason;}
	public JTextField getSigningLocation() {return signingLocation;}
	public JComboBox getVisibility() {return visibility;}
	public JCheckBox getRealSignature() {return realSignature;}
	public JComboBox getSigningPage() {return signingPage;}
	public JTextField getCustomPage() {return customPage;}
	public JComboBox getSignatureSize() {return size;}
	public JComboBox getPosition() {return position;}
	public JTextArea getSigningLog() {return signingLog;}
	public JCheckBox getVisibleReason() {return visibleReason;}
	public JCheckBox getVisibleLocation() {return visibleLocation;}
	public JCheckBox getVisibleSN() {return visibleSN;}
	public JLabel getSnLabel() {return snLabel;}
	
	/**
	 * Create the panel.
	 */
	public PDFSigningView(PDFSignerModel signingModel) {
		
		this.signingModel = signingModel;
		
		//setPreferredSize(new Dimension(600, 600));
		
		setAutoscrolls(true);
		
		setLayout(new MigLayout("", "[128.00,grow][220][260,grow][142.00][112.00][50.00,grow][][24.00]", "[][36.00][27.00][29.00][][31.00][][31.00][][29.00][36.00][61.00,grow]"));
		
		choosedFilesInput = new JTextField();
		choosedFilesInput.setColumns(10);
		choosedFilesInput.setEnabled(false);
		add(choosedFilesInput, "cell 0 0 3 1,growx");
		
		chooseFilesButton = new JButton("PDF(s) to sign");
		add(chooseFilesButton, "cell 3 0 2 1,alignx right");
		
		certificateSelector = new JComboBox(signingModel.getCertificates().toArray());
		add(certificateSelector, "cell 0 1 5 1,growx");
		
		//add(new JSeparator(SwingConstants.HORIZONTAL), "cell 7 1");
		
		snLabel = new JLabel("Serial number");
		add(snLabel, "cell 0 2 3 1");
		
		visibleSN = new JCheckBox("Visible SN");
		add(visibleSN, "cell 3 2 2 1,alignx right");
		
		JLabel reasonL = new JLabel("Reason");
		add(reasonL, "cell 0 3");
		signingReason = new JTextField();
		add(signingReason, "cell 0 4 4 1,growx");
		signingReason.setColumns(10);
		visibleReason = new JCheckBox("Visible");
		add(visibleReason, "cell 4 4,alignx right");
		
		JLabel locationL = new JLabel("Location");
		add(locationL, "cell 0 5 6 1");
		signingLocation = new JTextField();
		add(signingLocation, "cell 0 6 4 1,growx");
		signingLocation.setColumns(10);
		visibleLocation = new JCheckBox("Visible");
		add(visibleLocation, "cell 4 6,alignx right");
		
		signingModeL = new JLabel("Signing mode");
		add(signingModeL, "cell 0 7");
		
		visibility = new JComboBox(new String[]{"Visible signature", "Invisible signature"});
		add(visibility, "cell 0 8,growx");
		
		signingPage = new JComboBox(new String[]{"First page", "Last page", "All pages", "Custom page"});
		add(signingPage, "cell 1 8,growx");
		
		realSignature = new JCheckBox("Real signature");
		add(realSignature, "cell 2 8");
		
		customPageL = new JLabel("page");
		add(customPageL, "cell 3 8,alignx trailing");
		customPage = new JTextField();
		add(customPage, "cell 4 8,growx");
		customPage.setColumns(10);
		
		lblNewLabel = new JLabel("Aspect and position");
		add(lblNewLabel, "cell 0 9");
		
		size = new JComboBox(SignatureSize.values());
		add(size, "cell 0 10,growx");
		
		position = new JComboBox(SignaturePosition.values());
		add(position, "cell 1 10,growx");
		
		performSignButton = new JButton("Sign");
		add(performSignButton, "cell 4 10,alignx right");
		
		JScrollPane sf = new JScrollPane();
		sf.setPreferredSize(new Dimension(200, 100));
		add(sf, "cell 0 11 5 1,growx");
		
		signingLog = new JTextArea();
		sf.setViewportView(signingLog);
		//sf.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
	}
}
