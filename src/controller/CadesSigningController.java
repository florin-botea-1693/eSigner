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

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import main.App;
import model.CadesSigningModel;
import model.SigningModel;
import model.PdfSigningModel;
import model.certificates.AppCertificatesValidator;
import model.certificates.Certificate;
import model.certificates.ValidationResult;
import services.signing.CadesSigningService;
import services.signing.ISigningService;
import view.CadesSigningView;
import view.ICadesSigningView;
import view.ISigningView;
import view.PdfSigningView;
import view.components.SigningMessage;

public class CadesSigningController extends SigningController
{
	CadesSigningModel model;
	ICadesSigningView view;

	private File[] selectedFiles = {};
	
	public CadesSigningController(CadesSigningModel model, ICadesSigningView view)
	{
		super(model, view);
		
		this.model = model;
		this.view = view;
		
		// Configure view to match the model
		{
			view.select_signingExtension().setSelectedItem(model.getSigningExtension());
			view.select_digestAlgorithm().setSelectedItem(model.getDigestAlgorithm());
			view.select_signaturePackaging().setSelectedItem(model.getSignaturePackaging());
		}
		
		// then attach the events listeners
		view.select_signingExtension().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSigningExtension((String) view.select_signingExtension().getSelectedItem());
		    }
		});
		
		view.select_digestAlgorithm().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String selected = (String) view.select_digestAlgorithm().getSelectedItem();
		    	model.setDigestAlgorithm(DigestAlgorithm.valueOf(selected));
		    }
		});
		
		view.select_signaturePackaging().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String selected = (String) view.select_signaturePackaging().getSelectedItem();
		    	model.setSignaturePackaging(SignaturePackaging.valueOf(selected));
		    }
		});
	}

	@Override
	public ISigningService getSigningService() {
		return new CadesSigningService(model);
	}

	@Override
	protected void onFilesChoosed(File[] selectedFiles) {}
}
