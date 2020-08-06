package controller;

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
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;

import java.io.File;
import java.io.IOException; 

public class PDFSigningController implements PropertyChangeListener {
	PDFSignerModel model;
	PDFSigningView view;

	private File[] selectedFiles = {};
	
	public PDFSigningController(PDFSignerModel model, PDFSigningView view) {
		this.model = model;
		this.view = view;
		
		//addEventsListeners();
	}
	
	public void callSigningProcess() {
		Certificate cert = (Certificate) view.certificateSelector.getSelectedItem();
		for (File file : selectedFiles) {
			try {
				// aici voi avea altfel
				// pdfSigner nu se va mai numi asa, ci PDFSigningConfig
				// Signer.sign(pdfSigningConfig, file);
				// Signer va decide singur bazandu-se pe parametrii lui sign ce sa instantieze si cum sa semneze
				model.sign(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addEventsListeners() {
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

		// CHECKBOXEX
		// visible reason
		view.isVisibleReason.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.silenced().setSigningReason(view.signingReason.getText(), view.isVisibleReason.isSelected());
			}
		});
		// visible location
		view.isVisibleLocation.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.silenced().setSigningLocation(view.signingLocation.getText(), view.isVisibleLocation.isSelected());
			}
		});
		// real signature
		view.isRealSignature.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setIsRealSignature(view.isRealSignature.isSelected());
			}
		});
		
		// FIELDS
		// reason
		view.signingReason.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.setSigningReason(view.signingReason.getText(), view.isVisibleReason.isSelected());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.setSigningReason(view.signingReason.getText(), view.isVisibleReason.isSelected());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.setSigningReason(view.signingReason.getText(), view.isVisibleReason.isSelected());
			}
		});
		// location
		view.signingLocation.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.setSigningLocation(view.signingLocation.getText(), view.isVisibleLocation.isSelected());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.setSigningLocation(view.signingLocation.getText(), view.isVisibleLocation.isSelected());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.setSigningLocation(view.signingLocation.getText(), view.isVisibleLocation.isSelected());
			}
		});
		// signing page customised
		view.customSigningPage.getDocument().addDocumentListener(new DocumentListener() { // am de furca aici....
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.customSigningPage.getText());
				    model.setSigningPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.customSigningPage.getText());
				    model.setSigningPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.customSigningPage.getText());
				    model.setSigningPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
		});
		
		// SELECTS
		// visible
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
		// page
		view.signingPage.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSigningPage((SigningPage) view.signingPage.getSelectedItem());
		    }
		});
		// size
		view.signatureSize.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSignatureSize((SignatureSize) view.signatureSize.getSelectedItem());
		    }
		});
		// position
		view.signaturePosition.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSignaturePosition((SignaturePosition) view.signaturePosition.getSelectedItem());
		    }
		});
	}

	//===================================================================================================================||
	// 													ROUTER
	// ASTA E UN FEL DE ROUTER, porneste de la view <--- trebuie sa fie cat mai dumb posibil
	//===================================================================================================================||
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// e de la view, deci dezactivam observed-ul model la a mai emite evenimente
		switch (evt.getPropertyName()) {
		case "isVisibleSN": setVisibleSerialNumber(evt.getNewValue());
		break;
		case "isVisibleReason": setVisibleReason(evt.getNewValue());
		break;
		case "isVisibleLocation": setVisibleLocation(evt.getNewValue());
		break;
		case "isRealSignature": setRealSignature(evt.getNewValue());
		break;
		case "signingReason": setSigningReason(evt.getNewValue());
		break;
		case "signingLocation": setSigninLocation(evt.getNewValue());
		break;
		case "customSigningPage": setCustomSigningPage(evt.getNewValue());
		break;
		case "signatureVisibility": setSignatureVisibility(evt.getNewValue());
		break;
		case "signingPage": setSigningPage(evt.getNewValue());
		break;
		case "signatureSize": setSignatureSize(evt.getNewValue());
		break;
		case "signaturePosition": setSignaturePosition(evt.getNewValue());
		break;
		}
		// reactivam observed-ul model pentru a emite in continuare evenimente
	}

	private void setVisibleSerialNumber(Object newValue) {
		System.out.println("a");
	}

	private void setVisibleReason(Object newValue) {
		System.out.println("a");
	}

	private void setVisibleLocation(Object newValue) {
		System.out.println("a");
	}

	private void setRealSignature(Object newValue) {
		System.out.println("a");
	}

	private void setSigningReason(Object newValue) {
		System.out.println("a");
	}

	private void setSigninLocation(Object newValue) {
		System.out.println("a");
	}

	private void setCustomSigningPage(Object newValue) {
		System.out.println("a");
	}

	private void setSignatureVisibility(Object newValue) {
		System.out.println("a");
	}

	private void setSigningPage(Object newValue) {
		System.out.println("a");
	}

	private void setSignatureSize(Object newValue) {
		System.out.println("a");
	}

	private void setSignaturePosition(Object newValue) {
		System.out.println("a");
	}
}
