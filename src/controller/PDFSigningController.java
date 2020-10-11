package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import model.PdfSignerModel;
import model.certificates.AppCertificatesValidator;
import model.certificates.Certificate;
import model.certificates.ValidationResult;
import model.signing.SigningMode;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import view.PDFSigningView;
import view.SigningMessage;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.mutable.MutableBoolean;

import main.App;

import java.io.File;
import java.util.ArrayList; 

public class PDFSigningController
{
	
	PdfSignerModel model;
	PDFSigningView view;

	private File[] selectedFiles = {};
	
	public PDFSigningController(PdfSignerModel model, PDFSigningView view) 
	{
		
		this.model = model;
		this.view = view;

		view.signingLog.getTextPane().setEditable(false);
		view.setOpaque(false);
		
		//========================================||
		// VIEW -> MODEL
		//========================================||
		view.chooseFileAndCertificate.button_chooseFile.addActionListener(new ActionListener() {
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
					view.chooseFileAndCertificate.input_choosedFiles.setText(val);
					view.preview.addFiles(selectedFiles);
				}
			}
		});
		
		view.button_sign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callSigningProcess();
			}
		});

		view.check_visibleReason.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.getSigningOptions().setVisibleReason(view.check_visibleReason.isSelected());
			}
		});
		
		view.chooseFileAndCertificate.check_visibleSN.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.getSigningOptions().setVisibleSN(view.chooseFileAndCertificate.check_visibleSN.isSelected());
			}
		});

		view.check_visibleLocation.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.getSigningOptions().setVisibleLocation(view.check_visibleLocation.isSelected());
			}
		});

		view.check_realSignature.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.getSigningOptions().setRealSignature(view.check_realSignature.isSelected());
			}
		});
		
		view.input_reason.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setReason(view.input_reason.getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setReason(view.input_reason.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setReason(view.input_reason.getText());
			}
		});
		
		view.input_organization.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setOrganization(view.input_organization.getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setOrganization(view.input_organization.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setOrganization(view.input_organization.getText());
			}
		});

		view.input_location.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setLocation(view.input_location.getText());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setLocation(view.input_location.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				model.getSigningOptions().ignoreNextEvent().setLocation(view.input_location.getText());
			}
		});

		view.input_customPage.getDocument().addDocumentListener(new DocumentListener() { // am de furca aici....
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.input_customPage.getText());
				    model.getSigningOptions().ignoreNextEvent().setCustomPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.input_customPage.getText());
				    model.getSigningOptions().ignoreNextEvent().setCustomPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				try {
				    int intVal = Integer.parseInt(view.input_customPage.getText());
				    model.getSigningOptions().ignoreNextEvent().setCustomPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
		});
		
		view.chooseFileAndCertificate.select_certificates.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	Certificate x = (Certificate) view.chooseFileAndCertificate.select_certificates.getSelectedItem();
		    	model.setSigningCertificate((Certificate) view.chooseFileAndCertificate.select_certificates.getSelectedItem());;
		    }
		});
		
		view.select_visibleSignature.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.select_visibleSignature.getSelectedIndex()) {
		    		case 0:
		    			model.getSigningOptions().setVisibleSignature(false);
		    			view.preview.draggableSignatureField.setVisible(false);
		    		break;
		    		case 1:
		    			model.getSigningOptions().setVisibleSignature(true);
		    			view.preview.draggableSignatureField.setVisible(true);
		    		break;
		    	}
		    }
		});

		view.select_signingPage.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.getSigningOptions().setPage((SigningPage) view.select_signingPage.getSelectedItem());
		    }
		});

		view.select_signatureSize.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.getSigningOptions().setSize((SignatureSize) view.select_signatureSize.getSelectedItem());
		    }
		});

		view.select_signaturePosition.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.getSigningOptions().setPosition((SignaturePosition) view.select_signaturePosition.getSelectedItem());
		    	view.preview.setDraggableSignatureFieldPosition((SignaturePosition) view.select_signaturePosition.getSelectedItem());
		    }
		});
		
		view.button_preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (view.button_preview.getText().equals(">>"))
				{
					App.frame.setPreferredSize(new Dimension(App.frame.getWidth()*2, App.frame.getHeight()));
					view.add(view.RIGHT_PANEL);
					view.revalidate();
					view.repaint();
					App.frame.revalidate();
					App.frame.repaint();
					App.frame.pack();
					view.button_preview.setText("<<");
				}
				else
				{
					view.remove(view.RIGHT_PANEL);
					App.frame.setPreferredSize(new Dimension(App.frame.getWidth()/2, App.frame.getHeight()));
					App.frame.revalidate();
					App.frame.repaint();
					App.frame.pack();
					view.button_preview.setText(">>");
				}
			}
		});
		
		view.preview.draggableSignatureField.addMouseListener(new MouseListener() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {}
	    	@Override
	    	public void mousePressed(MouseEvent e) {}
	    	@Override
	    	public void mouseReleased(MouseEvent e) {
	    		model.getSigningOptions().setPosition(view.preview.draggableSignatureField.getPctX(), view.preview.draggableSignatureField.getPctY());
	    		// nu o setez custom, ci direct coordonate, iar custom pun in model ca sa evit logica extra acici
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {}
	    	@Override
	    	public void mouseExited(MouseEvent e) {}
	    });
		
		//==================================================================================================
		
		//========================================||
		// MODEL -> VIEW
		//========================================||
		// un workaround ar fi sa... ma ghidez mereu dupa sursa ev, nu dupa newval...

		model.addPropertyChangeListener("certificatesHolder", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Object[] c = ((ArrayList<Certificate>) model.getCertificatesHolder().getCertificates()).toArray();
				ComboBoxModel certificates = new DefaultComboBoxModel(c);
				view.chooseFileAndCertificate.select_certificates.setModel(certificates);
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("organization", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.input_organization.setText(model.getSigningOptions().getOrganization());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("selectedCertificate", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Certificate cert = model.getCertificatesHolder().getSelectedCertificate();
				if (cert != null) {
					view.button_sign.setEnabled(true);
					view.chooseFileAndCertificate.select_certificates.setSelectedItem(cert);
					view.chooseFileAndCertificate.label_serialNumber.setText("SN: " + cert.getSerialNumber());
				} else {
					view.button_sign.setEnabled(false);
				}
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("isVisibleSN", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.chooseFileAndCertificate.check_visibleSN.setSelected(model.getSigningOptions().isVisibleSN());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("reason", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.input_reason.setText(model.getSigningOptions().getReason());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("isVisibleReason", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.check_visibleReason.setSelected(model.getSigningOptions().isVisibleReason());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("location", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.input_location.setText(model.getSigningOptions().getLocation());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("isVisibleLocation", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.check_visibleLocation.setSelected(model.getSigningOptions().isVisibleLocation());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("isVisibleSignature", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				boolean b = model.getSigningOptions().isVisibleSignature();
				view.select_visibleSignature.setSelectedIndex(b ? 1 : 0);
				view.select_signingPage.setEnabled(b);
				view.input_customPage.setEnabled(b && view.select_signingPage.getSelectedItem() == SigningPage.CUSTOM_PAGE);
				view.check_realSignature.setEnabled(b && view.select_signingPage.getSelectedItem() == SigningPage.ALL_PAGES);
				view.select_signaturePosition.setEnabled(b);
				view.select_signatureSize.setEnabled(b);
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("page", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.select_signingPage.setSelectedItem(model.getSigningOptions().getPage());
				view.input_customPage.setEnabled(model.getSigningOptions().getPage() == SigningPage.CUSTOM_PAGE);
				view.check_realSignature.setEnabled(model.getSigningOptions().getPage() == SigningPage.ALL_PAGES);
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("isRealSignature", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.check_realSignature.setSelected(model.getSigningOptions().isRealSignature());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("customPage", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.input_customPage.setText(String.valueOf(model.getSigningOptions().getCustomPage()));
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("position", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.select_signaturePosition.setSelectedItem(model.getSigningOptions().getPosition());
			}
		});
		
		model.getSigningOptions().addPropertyChangeListener("size", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				view.select_signatureSize.setSelectedItem(model.getSigningOptions().getSize());
			}
		});
	}
	
	//==================================================================================================
	
	
	public void callSigningProcess()
	{
		
		view.signingLog.clearLog();
		MutableBoolean continueSigning = new MutableBoolean(false);
		Certificate cert = (Certificate) view.chooseFileAndCertificate.select_certificates.getSelectedItem();
		
		App.frame.setEnabled(false);
		try {
			AppCertificatesValidator validator = AppCertificatesValidator.getInstance();
			ValidationResult vr = validator.validate(cert);
			continueSigning.setValue(vr.canSign);
			System.out.println("can sign" + vr.canSign);
			view.signingLog.logErrorln(vr.message);
		} catch (Exception e1) {
			view.signingLog.logErrorln(e1.getMessage());
			App.frame.setEnabled(true);
		}
		
		if (continueSigning.getValue() == false) {
			App.frame.setEnabled(true);
			return;
		}
			
		final SigningMessage dialog = new SigningMessage();
		dialog.setAlwaysOnTop(true);
		dialog.getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				continueSigning.setValue(false);
				App.frame.setEnabled(true);
				dialog.dispose();
			}
		});
		
		dialog.setModal(false);
		dialog.setVisible(true);
		selectedFiles = new File[] {new File("C:/Users/user/Desktop/x.pdf")};
	    new Thread(new Runnable() {
	        public void run() {
	        	SigningMode signer = model.getPdfSigner();
	    		for (int i=0; i<selectedFiles.length; i++) {
	    			if (!continueSigning.booleanValue())
	    				continue;
	    			File file = selectedFiles[i];
	    			dialog.update(file.getName(), new int[] {i, selectedFiles.length});
	    			
	    			// String sep = view.signingLog.getTextPane().getText().length() > 0 ? "\n" : "";
	    			try {
	    				signer.performSign(file);
	    				view.signingLog.logSuccessln(file.getName() + " successfully signed");
	    			} catch (Exception e) {
	    				view.signingLog.logErrorln(e.getMessage());
	    				e.printStackTrace();
	    			}
	    		}
	    		App.frame.setEnabled(true);
	    		dialog.dispose();
	        }
	     }).start();
	}
}
