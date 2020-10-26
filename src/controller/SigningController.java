package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;

import org.apache.commons.lang3.mutable.MutableBoolean;

import main.App;
import model.SigningModel;
import model.certificates.AppCertificatesValidator;
import model.certificates.Certificate;
import model.certificates.MSCAPICertificatesHolder;
import model.certificates.ValidationResult;
import services.signing.ISigningService;
import view.ICadesSigningView;
import view.ISigningView;
import view.components.SigningMessage;

public abstract class SigningController 
{
	SigningModel model;
	ISigningView view;
	
	public SigningController(SigningModel model, ISigningView view)
	{
		this.view = view;
		this.model = model;
		
		view.getLog().getTextPane().setEditable(false);
		view.setOpaque(false);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				view.spinner_loadingCertificates().setVisible(true);
				MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
				model.setCertificatesHolder(certificatesHolder);
				Object[] c = ((ArrayList<Certificate>) certificatesHolder.getCertificates()).toArray();
				ComboBoxModel certificates = new DefaultComboBoxModel(c);
				view.select_certificates().setModel(certificates);
				view.spinner_loadingCertificates().setVisible(false);
				view.select_certificates().setSelectedItem(certificatesHolder.getSelectedCertificate());
				view.label_serialNumber().setText("Serial Number: " + certificatesHolder.getSelectedCertificate().getSerialNumber());
			}
		}).start();
		
		view.button_chooseFile().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.setCurrentDirectory(new File("C:\\Users\\user\\Desktop")/*new File(System.getProperty("user.home"))*/);
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					model.setSelectedFiles(fileChooser.getSelectedFiles());
					String val = "";
					for (File f : fileChooser.getSelectedFiles()) {
						if (val.length() > 0)
							val += "|";
						val += f.toString();
					}
					view.input_choosedFiles().setText(val);
					onFilesChoosed(fileChooser.getSelectedFiles());
				}
			}
		});
		
		view.select_certificates().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if (model.getCertificatesHolder().getSelectedCertificate() == null) return;
		    	Certificate x = (Certificate) view.select_certificates().getSelectedItem();
		    	model.setSigningCertificate((Certificate) view.select_certificates().getSelectedItem());;
		    	view.label_serialNumber().setText("Serial Number: " + model.getCertificatesHolder().getSelectedCertificate().getSerialNumber());
		    }
		});
		
		view.button_sign().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callSigningProcess();
			}
		});
		
		model.addPropertyChangeListener("certificatesHolder", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Object[] c = ((ArrayList<Certificate>) model.getCertificatesHolder().getCertificates()).toArray();
				ComboBoxModel certificates = new DefaultComboBoxModel(c);
				view.select_certificates().setModel(certificates);
			}
		});
	}

	public void callSigningProcess()
	{
		System.out.println("signing with certificate " + model.getCertificatesHolder().getSelectedCertificate());
		
		view.getLog().clearLog();
		MutableBoolean continueSigning = new MutableBoolean(false);
		Certificate cert = (Certificate) view.select_certificates().getSelectedItem();
		
		App.frame.setEnabled(false);
		try {
			AppCertificatesValidator validator = AppCertificatesValidator.getInstance();
			ValidationResult vr = validator.validate(cert);
			continueSigning.setValue(vr.canSign);
			System.out.println("can sign" + vr.canSign);
			view.getLog().logErrorln(vr.message);
		} catch (Exception e1) {
			view.getLog().logErrorln(e1.getMessage());
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
		
		// CadesSigning cadesSigner = new CadesSigning(model);
		
		dialog.setModal(false);
		dialog.setVisible(true);
		//selectedFiles = new File[] {new File("C:/Users/user/Desktop/x.pdf")};
		File[] selectedFiles = model.getSelectedFiles();
		
		ISigningService signingService = getSigningService();
		
	    new Thread(new Runnable() {
	        public void run() {
	    		for (int i=0; i<selectedFiles.length; i++) {
	    			if (!continueSigning.booleanValue())
	    				continue;
	    			File file = selectedFiles[i];
	    			dialog.update(file.getName(), new int[] {i, selectedFiles.length});
	    			
	    			// String sep = view.signingLog.getTextPane().getText().length() > 0 ? "\n" : "";
	    			try {
	    				signingService.sign(file);
	    				view.getLog().logSuccessln(file.getName() + " successfully signed");
	    			} catch (Exception e) {
	    				view.getLog().logErrorln(e.getMessage());
	    				e.printStackTrace();
	    			}
	    		}
	    		App.frame.setEnabled(true);
	    		dialog.dispose();
	        }
	     }).start();
	}

	/**
	 * 
	 * @return ar trebui sa intoarca un signingservice configurat care sa aiba metoda sign(file)
	 */
	abstract public ISigningService getSigningService();
	
	protected abstract void onFilesChoosed(File[] selectedFiles);
	
}
