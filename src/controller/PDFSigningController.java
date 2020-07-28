package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import model.PDFSignerModel;
import model.certificates.Certificate;
import model.signing.PDFSigningOptions;
import model.signing.Signer;
import view.PDFSigningView;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;

import java.io.File;
import java.io.IOException; 

public class PDFSigningController extends Controller {
	PDFSignerModel cfg;
	PDFSigningView view;

	private File[] selectedFiles = {};
	
	public PDFSigningController(PDFSignerModel cfg, PDFSigningView view) {
		this.cfg = cfg;
		this.view = view;
		
		init();
	}
	
	public void callSigningProcess() {
		Certificate cert = (Certificate) view.getCertificateSelector().getSelectedItem();
		for (File file : selectedFiles) {
			try {
				// aici voi avea altfel
				// pdfSigner nu se va mai numi asa, ci PDFSigningConfig
				// Signer.sign(pdfSigningConfig, file);
				// Signer va decide singur bazandu-se pe parametrii lui sign ce sa instantieze si cum sa semneze
				Signer.sign(cfg, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void init() {
		view.getChooseFilesButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
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
				cfg.setVisibleSN(view.getVisibleSN().isSelected());
			}
		});
		// visible reason
		view.getVisibleReason().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				cfg.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
		});
		// visible location
		view.getVisibleLocation().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				cfg.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
		});
		// real signature
		view.getRealSignature().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				cfg.setIsRealSignature(view.getRealSignature().isSelected());
			}
		});
		
		// FIELDS
		// reason
		view.getSigningReason().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				cfg.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				cfg.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				cfg.setReason(view.getSigningReason().getText(), view.getVisibleReason().isSelected());
			}
		});
		// location
		view.getSigningLocation().getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				cfg.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				cfg.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				cfg.setLocation(view.getSigningLocation().getText(), view.getVisibleLocation().isSelected());
			}
		});
		
		// SELECTS
		// visible
		view.getVisibility().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.getVisibility().getSelectedIndex()) {
		    		case 0:
		    		cfg.setVisibleSignature(true);
		    		break;
		    		case 1:
		    		cfg.setVisibleSignature(false);
		    		break;
		    	}
		    }
		});
		// page
		view.getSigningPage().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.getSigningPage().getSelectedIndex()) {
		    		case 0: // first
		    		cfg.setPage(1);
		    		break;
		    		case 1: // last
		    		cfg.setPage(-1);
		    		break;
		    		case 2: // all
		    		cfg.setPage(0);
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
		    		cfg.setSize("small");
		    		break;
		    		case 1: // last
		    			cfg.setSize("medium");
		    		break;
		    		case 2: // all
		    			cfg.setSize("large");
		    		break;	
		    	}
		    }
		});
		// position
		view.getPosition().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.getPosition().getSelectedIndex()) {
		    		case 0: // first
		    		// position
		    		break;
		    		case 1: // last
		    		// position
		    		break;
		    		case 2: // all
		    		// position
		    		break;	
		    	}
		    }
		});
	}
}
