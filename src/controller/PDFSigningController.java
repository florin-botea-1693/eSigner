package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import model.PDFSignerModel;
import model.certificates.Certificate;
import model.signing.PDFSigningOptions;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import model.signing.visible.SigningPage;
import view.PDFSigningView;
import view.SigningMessage;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList; 

public class PDFSigningController {
	PDFSignerModel model;
	PDFSigningView view;

	private File[] selectedFiles = {};
	
	public PDFSigningController(PDFSignerModel model, PDFSigningView view) {
		this.model = model;
		this.view = view;

		init();
	}
	
	public void init() {
		view.signingLog.setEditable(false);
		view.setOpaque(false);
		//========================================||
		// VIEW -> MODEL
		//========================================||
		view.chooseFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(new File("C:\\Users\\user\\Desktop")/*new File(System.getProperty("user.home"))*/);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					selectedFiles = fileChooser.getSelectedFiles();
					String val = "";
					for (File f : selectedFiles) {
						if (val.length() > 0)
							val += "|";
						val += f.toString();
					}
					view.choosedFilesInput.setText(val);
				}
			}
		});
		
		view.performSignButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callSigningProcess();
			}
		});

		view.isVisibleReason.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setVisibleReason(view.isVisibleReason.isSelected());
			}
		});
		
		view.isVisibleSN.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setVisibleSN(view.isVisibleSN.isSelected());
			}
		});

		view.isVisibleLocation.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setVisibleLocation(view.isVisibleLocation.isSelected());
			}
		});

		view.isRealSignature.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setIsRealSignature(view.isRealSignature.isSelected());
			}
		});
		
		view.signingReason.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.ignoreNextEvent().setSigningReason(view.signingReason.getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.ignoreNextEvent().setSigningReason(view.signingReason.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.ignoreNextEvent().setSigningReason(view.signingReason.getText());
			}
		});

		view.signingLocation.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.ignoreNextEvent().setSigningLocation(view.signingLocation.getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.ignoreNextEvent().setSigningLocation(view.signingLocation.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.ignoreNextEvent().setSigningLocation(view.signingLocation.getText());
			}
		});

		view.customSigningPage.getDocument().addDocumentListener(new DocumentListener() { // am de furca aici....
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.customSigningPage.getText());
				    model.ignoreNextEvent().setSigningPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.customSigningPage.getText());
				    model.ignoreNextEvent().setSigningPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.customSigningPage.getText());
				    model.ignoreNextEvent().setSigningPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
		});
		
		view.certificateSelector.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSigningCertificate((Certificate) view.certificateSelector.getSelectedItem());;
		    }
		});
		
		view.signatureVisibility.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.signatureVisibility.getSelectedIndex()) {
		    		case 0:
		    			model.setVisibleSignature(false);
		    		break;
		    		case 1:
		    			model.setVisibleSignature(true);
		    		break;
		    	}
		    }
		});

		view.signingPage.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSigningPage((SigningPage) view.signingPage.getSelectedItem());
		    }
		});

		view.signatureSize.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSignatureSize((SignatureSize) view.signatureSize.getSelectedItem());
		    }
		});

		view.signaturePosition.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSignaturePosition((SignaturePosition) view.signaturePosition.getSelectedItem());
		    }
		});
		
		//==================================================================================================
		
		//========================================||
		// MODEL -> VIEW
		//========================================||
		model.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				switch (evt.getPropertyName()) {
				case "certificates":
					ComboBoxModel certificates = new DefaultComboBoxModel(((ArrayList<Certificate>) evt.getNewValue()).toArray());
					view.certificateSelector.setModel(certificates);
					break;
				case "selectedCertificate":
					Certificate cert = (Certificate) evt.getNewValue();
					if (cert != null) {
						view.performSignButton.setEnabled(true);
						view.certificateSelector.setSelectedItem(cert);
						view.label_serialNumber.setText("SN: " + cert.getSerialNumber());
					} else {
						view.performSignButton.setEnabled(false);
					}
					break;
				case "isVisibleSN":
					view.isVisibleSN.setSelected((boolean) evt.getNewValue());
					break;
				case "signingReason":
					view.signingReason.setText((String) evt.getNewValue());
					break;
				case "isVisibleReason":
					view.isVisibleReason.setSelected((boolean) evt.getNewValue());
					break;
				case "signingLocation":
					view.signingLocation.setText((String) evt.getNewValue());
					break;
				case "isVisibleLocation":
					view.isVisibleLocation.setSelected((boolean) evt.getNewValue());
					break;
				case "signatureVisibility":
					boolean b = (boolean) evt.getNewValue();
					view.signatureVisibility.setSelectedIndex(b ? 1 : 0);
					view.signingPage.setEnabled(b);
					view.customSigningPage.setEnabled(b && view.signingPage.getSelectedItem() == SigningPage.CUSTOM_PAGE);
					view.isRealSignature.setEnabled(b && view.signingPage.getSelectedItem() == SigningPage.ALL_PAGES);
					view.signaturePosition.setEnabled(b);
					view.signatureSize.setEnabled(b);
					break;
				case "signingPage":
					view.signingPage.setSelectedItem(evt.getNewValue());
					view.customSigningPage.setEnabled(evt.getNewValue() == SigningPage.CUSTOM_PAGE);
					view.isRealSignature.setEnabled(evt.getNewValue() == SigningPage.ALL_PAGES);
					break;
				case "isRealSignature":
					view.isRealSignature.setSelected((boolean) evt.getNewValue());
					break;
				case "customSigningPage":
					view.customSigningPage.setText(String.valueOf(evt.getNewValue()));
					break;
				case "signaturePosition":
					view.signaturePosition.setSelectedItem((SignaturePosition) evt.getNewValue());
					break;
				case "signatureSize":
					view.signatureSize.setSelectedItem((SignatureSize) evt.getNewValue());
					break;
				}
			}
		});
		
		//==================================================================================================
	}
	
	public void callSigningProcess() {
		MutableBoolean continueSigning = new MutableBoolean(true);
		view.clearLog();
		Certificate cert = (Certificate) view.certificateSelector.getSelectedItem();

		final SigningMessage dialog = new SigningMessage();
		dialog.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				continueSigning.setValue(false);
				dialog.dispose();
			}
		});
		
		view.getParentJFrame().setEnabled(false);
		dialog.setModal(false);
		dialog.setVisible(true);
		
	    new Thread(new Runnable() {
	        public void run() {
	    		for (int i=0; i<selectedFiles.length; i++) {
	    			if (!continueSigning.booleanValue())
	    				continue;
	    			File file = selectedFiles[i];
	    			dialog.update(file.getName(), new int[] {i, selectedFiles.length});
	    			
	    			String sep = view.signingLog.getText().length() > 0 ? "\n" : "";
	    			try {
	    				model.sign(file);
	    				view.logSuccessln(file.getName() + " successfully signed");
	    			} catch (Exception e) {
	    				view.logErrorln(e.getMessage());
	    				e.printStackTrace();
	    			}
	    		}
	    		view.getParentJFrame().setEnabled(true);
	    		dialog.dispose();
	        }
	     }).start();
	}
}
