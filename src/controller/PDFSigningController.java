package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

public class PDFSigningController {
	PDFSignerModel model;
	PDFSigningView view;

	private File[] selectedFiles = {};
	
	public PDFSigningController(PDFSignerModel model, PDFSigningView view) {
		this.model = model;
		this.view = view;
		
		addEventsListeners();
		syncData();
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
		// visible sn
		view.isVisibleSN.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.stealth().setVisibleSN(view.isVisibleSN.isSelected());
			}
		});
		// visible reason
		view.isVisibleReason.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.stealth().setSigningReason(view.signingReason.getText(), view.isVisibleReason.isSelected());
			}
		});
		// visible location
		view.isVisibleLocation.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.stealth().setSigningLocation(view.signingLocation.getText(), view.isVisibleLocation.isSelected());
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

	//====================================||
	// DON'T JUDGE ME, poate pe viitor voi avea model care sa anunte direct view ca asa ar fi cel mai ok
	//====================================||
	public void syncData() {
		view.signingReason.setText(model.getSigningReason());
		view.signingLocation.setText(model.getSigningLocation());
		view.signatureVisibility.setSelectedIndex(model.isVisibleSignature());
		view.isRealSignature.setSelected(model.isRealSignature());
		view.signingPage.setSelectedIndex(model.getSigningPage().index);
		//view.customSigningPage.setText(model.getSigningPage().getCustom()); nu voi seta niciodata asta ca depinde de ce doc semnez
		view.signatureSize.setSelectedIndex(model.getSignatureSize().index);
		view.signaturePosition.setSelectedIndex(model.getSignaturePosition().index);
		view.isVisibleReason.setSelected(model.isVisibleReason());
		view.isVisibleLocation.setSelected(model.isVisibleLocation());
		view.isVisibleSN.setSelected(model.isVisibleSerialNumber());
	}
}
