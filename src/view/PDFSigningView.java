package view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentListener;

import org.w3c.dom.events.DocumentEvent;

import model.PDFSignerModel;
import model.certificates.Certificate;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import model.signing.visible.SigningPage;
import net.miginfocom.swing.MigLayout;
import utils.PropertyChangeSupportExtended;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigInteger;
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
	private final JFrame parent;

	private PropertyChangeSupportExtended observed;
	
	public final JTextField choosedFilesInput;
	public final JButton chooseFilesButton;
	public final JComboBox certificateSelector;
	public final JButton performSignButton;
	public final JTextField signingReason; 
	public final JTextField signingLocation;
	public final JComboBox signatureVisibility;
	public final JCheckBox isRealSignature;
	public final JComboBox signingPage;
	public final JTextField customSigningPage;
	public final JComboBox signatureSize;
	public final JComboBox signaturePosition;
	public final JTextArea signingLog;
	public final JCheckBox isVisibleReason;
	public final JCheckBox isVisibleLocation;
	public final JCheckBox isVisibleSN;
	
	private JLabel lblNewLabel;
	private JLabel customPageL;
	public final JLabel label_serialNumber;
	
	public JFrame getParentJFrame() { return parent;}

	//=======================||
	// >>> CREATE VISUAL <<<
	//=======================||
	public PDFSigningView(JFrame parent) {
		this.parent = parent;
		
		this.observed = new PropertyChangeSupportExtended(this);
		
		//setPreferredSize(new Dimension(600, 600));
		
		setAutoscrolls(true);
		
		setLayout(new MigLayout("", "[128.00,grow][220][260,grow][142.00][112.00][50.00,grow][][24.00]", "[][36.00][27.00][29.00][][31.00][][31.00][][29.00][36.00][61.00,grow]"));
		
		choosedFilesInput = new JTextField();
		choosedFilesInput.setColumns(10);
		choosedFilesInput.setEnabled(false);
		add(choosedFilesInput, "cell 0 0 3 1,growx");
		
		chooseFilesButton = new JButton("PDF(s) to sign");
		add(chooseFilesButton, "cell 3 0 2 1,alignx right");
		
		certificateSelector = new JComboBox();//signingModel.getCertificates().toArray()
		add(certificateSelector, "cell 0 1 5 1,growx");
		
		//add(new JSeparator(SwingConstants.HORIZONTAL), "cell 7 1");
		
		label_serialNumber = new JLabel("Serial number");
		add(label_serialNumber, "cell 0 2 3 1");
		
		this.isVisibleSN = new JCheckBox("Visible SN");
		add(this.isVisibleSN, "cell 3 2 2 1,alignx right");
		
		JLabel label_reason = new JLabel("Reason");
		add(label_reason, "cell 0 3");
		this.signingReason = new JTextField();
		add(this.signingReason, "cell 0 4 4 1,growx");
		this.signingReason.setColumns(10);
		this.isVisibleReason = new JCheckBox("Visible");
		add(this.isVisibleReason, "cell 4 4,alignx right");
		
		JLabel label_location = new JLabel("Location");
		add(label_location, "cell 0 5 6 1");
		this.signingLocation = new JTextField();
		add(this.signingLocation, "cell 0 6 4 1,growx");
		signingLocation.setColumns(10);
		this.isVisibleLocation = new JCheckBox("Visible");
		add(this.isVisibleLocation, "cell 4 6,alignx right");
		JLabel label_signingMode = new JLabel("Signing mode");
		add(label_signingMode, "cell 0 7");
		
		this.signatureVisibility = new JComboBox(new String[]{"Invisible signature", "Visible signature"});
		add(this.signatureVisibility, "cell 0 8,growx");
		
		this.signingPage = new JComboBox(SigningPage.values());
		add(this.signingPage, "cell 1 8,growx");
		
		this.isRealSignature = new JCheckBox("Real signature");
		add(this.isRealSignature, "cell 2 8");
		
		JLabel label_customPage = new JLabel("page");
		add(label_customPage, "cell 3 8,alignx trailing");
		this.customSigningPage = new JTextField();
		add(this.customSigningPage, "cell 4 8,growx");
		this.customSigningPage.setColumns(10);
		
		JLabel label_aspectAndPos = new JLabel("Aspect and position");
		add(label_aspectAndPos, "cell 0 9");
		
		this.signatureSize = new JComboBox(SignatureSize.values());
		add(this.signatureSize, "cell 0 10,growx");
		
		this.signaturePosition = new JComboBox(SignaturePosition.values());
		add(this.signaturePosition, "cell 1 10,growx");
		
		this.performSignButton = new JButton("Sign");
		add(this.performSignButton, "cell 4 10,alignx right");
		
		JScrollPane sf = new JScrollPane();
		sf.setPreferredSize(new Dimension(200, 100));
		add(sf, "cell 0 11 5 1,growx");
		
		this.signingLog = new JTextArea();
		sf.setViewportView(this.signingLog);
		//sf.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		//addEventsListeners();
	}
}
