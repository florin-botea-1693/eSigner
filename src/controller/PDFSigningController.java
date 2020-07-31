package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import model.PDFSignerModel;
import model.certificates.Certificate;
import model.signing.PDFSigningOptions;
import model.signing.visible.SignaturePosition;
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
		Certificate cert = (Certificate) view.getCertificateSelector().getSelectedItem();
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
		view.getChooseFilesButton().addActionListener(new ActionListener() {
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
					view.getChoosedFilesInput().setText(val);
				}
			}
		});
		
		view.getPerformSignButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callSigningProcess();
			}
		});

		// CHECKBOXEX
		// visible sn
		view.getVisibleSN().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setVisibleSN(view.getVisibleSN().isSelected());
			}
		});
		// visible reason
		view.getVisibleReason().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
		});
		// visible location
		view.getVisibleLocation().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
		});
		// real signature
		view.getRealSignature().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setIsRealSignature(view.getRealSignature().isSelected());
			}
		});
		
		// FIELDS
		// reason
		view.getSigningReason().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
		});
		// location
		view.getSigningLocation().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
		});
		
		// SELECTS
		// visible
		view.getVisibility().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.getVisibility().getSelectedIndex()) {
		    		case 0:
		    			model.setVisibleSignature(true);
		    		break;
		    		case 1:
		    			model.setVisibleSignature(false);
		    		break;
		    	}
		    }
		});
		// page
		view.getSigningPage().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.getSigningPage().getSelectedIndex()) {
		    		case 0: // first
		    			model.setPage(1);
		    		break;
		    		case 1: // last
		    			model.setPage(-1);
		    		break;
		    		case 2: // all
		    			model.setPage(0);
		    		break;	
		    		case 3: // custom
		    		// enable custom page field
		    		break;
		    	}
		    }
		});
		// size
		view.getSignatureSize().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.getSignatureSize().getSelectedIndex()) {
		    		case 0: // first
		    			model.setSize("small");
		    		break;
		    		case 1: // last
		    			model.setSize("medium");
		    		break;
		    		case 2: // all
		    			model.setSize("large");
		    		break;	
		    	}
		    }
		});
		// position
		view.getPosition().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSignaturePosition((SignaturePosition) view.getPosition().getSelectedItem());
		    }
		});
	}

	//====================================||
	// DON'T JUDGE ME
	//====================================||
	public void syncData() {
		view.getPosition().setSelectedIndex(model.getSignaturePosition().index);
		view.getSignatureSize().setSelectedIndex(model.getSignatureSize().index);
	}
}
