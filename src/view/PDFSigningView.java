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
import modelView.PDFSigningModelView;
import net.miginfocom.swing.MigLayout;
import utils.PropertyChangeSupportExtended;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

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

public class PDFSigningView extends JPanel implements PropertyChangeListener {

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
	private JLabel label_serialNumber;

	//=======================||
	// >>> CREATE VISUAL <<<
	//=======================||
	public PDFSigningView() {
		
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
		addEventsListeners();
	}
	
	//====================||
	// OBSERVER METHODS
	//====================||
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observed.addPropertyChangeListener(pcl);
    }
 
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	observed.removePropertyChangeListener(pcl);
    }
    
    public PDFSigningView silenced() {
		this.observed.skipNextTurn();
		return this;
	}
    //====================\\

	private void setCertificates(Object[] o) {
		ComboBoxModel cbm = new DefaultComboBoxModel(o);
		this.certificateSelector.setModel(cbm);
	}
	
	private void selectCertificate(Certificate cert) {
		if (cert == null) return;
		for (int i=0; i<this.certificateSelector.getModel().getSize(); i++) {
			Certificate localCert = (Certificate) this.certificateSelector.getModel().getElementAt(i);
			BigInteger localCertSN = localCert.getPrivateKey().getCertificate().getCertificate().getSerialNumber();
			BigInteger certSN = cert.getPrivateKey().getCertificate().getCertificate().getSerialNumber();
			if (localCertSN.compareTo(certSN) == 0) {
				this.certificateSelector.setSelectedIndex(i);
				this.label_serialNumber.setText("SN: " + cert.getSerialNumber());
				return;
			}
		}
		System.out.println("certificatul selectat in Model nu a fost gasit");
	}
	
	private void isVisibleSerialNumber(boolean b) {
		this.isVisibleSN.setSelected(b);
	}
	
	private void isVisibleReason(boolean b) {
		this.isVisibleReason.setSelected(b);
	}
	
	private void isVisibleLocation(boolean b) {
		this.isVisibleLocation.setSelected(b);
	}
	
	private void isVisibleSignature(boolean b) {
		int i = b ? 1 : 0;
		this.signatureVisibility.setSelectedIndex(i);
	}
	
	private void isRealSignature(boolean b) {
		this.isRealSignature.setSelected(b);
	}
	
	private void setSigningLocation(String s) {
		this.signingLocation.setText(s);
	}
	
	private void setSigningReason(String s) {
		this.signingReason.setText(s);
	}
	
	private void setSigningPage(SigningPage p) {
		this.customSigningPage.setEnabled(true);
		if (p != SigningPage.CUSTOM_PAGE) {
			this.customSigningPage.setEnabled(false);
		}
		this.signingPage.setSelectedItem(p);
	}
	
	private void setCustomSigningPage(int p) {
		this.customSigningPage.setText(String.valueOf(p));
	}
	
	private void setSignatureSize(SignatureSize s) {
		this.signatureSize.setSelectedItem(s);
	}
	
	private void setSignaturePosition(SignaturePosition p) {
		this.signaturePosition.setSelectedItem(p);
	}

	//======================================================================================================================||
	//                                                 SWING EVENTS LISTENERS
	// - vor fi declansati la orice schimbare, 
	// - deci ar trebui sa am un filtru pentru specifica daca model a actualizat view, sau view a actualizat model
	// - trebuie sa fie mereu prezente in View, nu in controller pentru a le putea pune in legatura cu observer-ul
	// - observer-ul va fi ca un filtru care va spune model-ului ca de la View vin actualizarile.
	// - in controller voi avea acele metode
	//======================================================================================================================||
	public void addEventsListeners() {
		this.chooseFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(new File("C:\\Users\\user\\Desktop")/*new File(System.getProperty("user.home"))*/);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					String val = "";
					for (File f : fileChooser.getSelectedFiles()) {
						if (val.length() > 0)
							val += "|";
						val += f.toString();
					}
					choosedFilesInput.setText(val);
				}
			}
		});
		
		this.performSignButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//observed.firePropertyChange(propName, oldVal, newVal);
			}
		});

		//=================||
		// CHECKBOXES
		//=================||
		this.isVisibleSN.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				observed.firePropertyChange("isVisibleSN", null, isVisibleSN.isSelected());
			}
		});
		// visible reason
		this.isVisibleReason.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				observed.firePropertyChange("isVisibleReason", null, isVisibleReason.isSelected());
			}
		});
		// visible location
		this.isVisibleLocation.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				observed.firePropertyChange("isVisibleLocation", null, isVisibleLocation.isSelected());
			}
		});
		// real signature
		this.isRealSignature.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				observed.firePropertyChange("isRealSignature", null, isRealSignature.isSelected());
			}
		});
		//============================\\
		
		//=================||
		// TEXTFIELDS
		//=================||
		this.signingReason.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("signingReason", null, signingReason.getText());
			}
			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("signingReason", null, signingReason.getText());
			}
			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("signingReason", null, signingReason.getText());
			}
		});
		// location
		this.signingLocation.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("signingLocation", null, signingLocation.getText());
			}

			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("signingLocation", null, signingLocation.getText());
			}

			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("signingLocation", null, signingLocation.getText());
			}
		});
		// signing page customised
		this.customSigningPage.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("customSigningPage", null, customSigningPage.getText());
			}

			@Override
			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("customSigningPage", null, customSigningPage.getText());
			}

			@Override
			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				observed.firePropertyChange("customSigningPage", null, customSigningPage.getText());
			}
		});
		//============================\\
		
		//===================||
		// COMBOBOX (select)
		//===================||
		this.signatureVisibility.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				observed.firePropertyChange("signatureVisibility", null, signatureVisibility.getSelectedIndex());
			}
		});
		// page
		this.signingPage.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
		    	customSigningPage.setEnabled(false);
		    	if (signingPage.getSelectedItem() == SigningPage.CUSTOM_PAGE)
		    		customSigningPage.setEnabled(true);
				observed.firePropertyChange("signingPage", null, signingPage.getSelectedItem());
			}
		});
		// size
		this.signatureSize.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				observed.firePropertyChange("signatureSize", null, signatureSize.getSelectedItem());
			}
		});
		// position
		this.signaturePosition.addActionListener (new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				observed.firePropertyChange("signaturePosition", null, signaturePosition.getSelectedItem());
			}
		});
		//============================\\
	}
	//==============================================================================================================\\
	
	//==================================================================================================================||
	//                                               E DE LA MODEL
	//==================================================================================================================||
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// dezactivam observed-ul la a mai emite evenimente
		System.out.println(evt.getPropertyName());
		switch (evt.getPropertyName()) {
		case "*":
			PDFSigningModelView mv = (PDFSigningModelView) evt.getNewValue();
			this.setCertificates(mv.certificates);
			this.selectCertificate(mv.selectedCertificate);
			this.isVisibleSerialNumber(mv.isVisibleSerialNumber);
			this.isVisibleReason(mv.isVisibleReason);
			this.isVisibleLocation(mv.isVisibleLocation);
			this.isVisibleSignature(mv.isVisibleSignature);
			this.isRealSignature(mv.isRealSignature);
			this.setSigningLocation(mv.signingLocation);        
			this.setSigningReason(mv.signingReason);
			this.setSigningPage(mv.signingPage);
			this.setCustomSigningPage(mv.customSigningPage);
			this.setSignatureSize(mv.signatureSize);
			this.setSignaturePosition(mv.signaturePosition);
		break;
		}
		// reactivam observed-ul pentru a emite evenimente in continuare
	}
	//==============================================================================================================\\
}
