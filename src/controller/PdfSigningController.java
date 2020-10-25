package controller;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import model.PdfSigningModel;
import model.certificates.AppCertificatesValidator;
import model.certificates.Certificate;
import model.certificates.ValidationResult;
import model.signing.visible.AdobeLikeSignatureAspect;
import model.signing.visible.SignatureAspect;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import services.signing.ISigningService;
import services.signing.PDFInvisibleSigning;
import services.signing.PDFVisibleAllPagesRealSigning;
import services.signing.PDFVisibleAllPagesSigning;
import services.signing.PDFVisibleSigning;
import view.IPdfSigningView;
import view.PdfSigningView;
import view.components.SigningMessage;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.mutable.MutableBoolean;

import controller.listeners.InputListener;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.pdf.pdfbox.PdfBoxNativeObjectFactory;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import main.App;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList; 

public class PdfSigningController extends SigningController
{
	
	PdfSigningModel model;
	IPdfSigningView view;

	private File[] selectedFiles = {};
	
	public PdfSigningController(PdfSigningModel model, IPdfSigningView view) 
	{
		super(model, view);
		
		this.model = model;
		this.view = view;
		
		// sync view with the model
		{
			view.check_visibleReason().setSelected(model.isVisibleReason());
			view.check_visibleSN().setSelected(model.isVisibleSN());
			view.check_visibleLocation().setSelected(model.isVisibleLocation());
			view.check_realSignature().setSelected(model.isRealSignature());
			view.input_reason().setText(model.getReason());
			view.input_organization().setText(model.getOrganization());
			view.input_location().setText(model.getLocation());
			view.input_customPage().setText(String.valueOf(model.getCustomPage()));
			view.select_certificates().setSelectedItem(model.getCertificatesHolder().getSelectedCertificate());
			view.select_visibleSignature().setSelectedIndex(model.isVisibleSignature() ? 1 : 0);
			view.select_signingPage().setSelectedItem(model.getPage());
			view.select_signatureSize().setSelectedItem(model.getSize());
			view.select_signaturePosition().setSelectedItem(model.getPosition());
			
			view.getPreview().draggableSignatureField.setVisible(model.isVisibleSignature());
			view.getPreview().setDraggableSignatureFieldPosition(model.getPosition());
			view.getPreview().setDraggableSignatureFieldSize(model.getSize());
			
			validateFields();
		}
		
		view.check_visibleReason().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setVisibleReason(view.check_visibleReason().isSelected());
			}
		});
		
		view.check_visibleSN().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setVisibleSN(view.check_visibleSN().isSelected());
			}
		});

		view.check_visibleLocation().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setVisibleLocation(view.check_visibleLocation().isSelected());
			}
		});

		view.check_realSignature().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				model.setRealSignature(view.check_realSignature().isSelected());
			}
		});
		
		view.input_reason().getDocument().addDocumentListener(new InputListener() {
			@Override
			public void onInsertChange() {
				model.setReason(view.input_reason().getText());
			}
		});
		
		view.input_organization().getDocument().addDocumentListener(new InputListener() {
			@Override
			public void onInsertChange() {
				model.setOrganization(view.input_organization().getText());
			}
		});

		view.input_location().getDocument().addDocumentListener(new InputListener() {
			@Override
			public void onInsertChange() {
				model.setLocation(view.input_location().getText());
			}
		});

		view.input_customPage().getDocument().addDocumentListener(new InputListener() {
			@Override
			public void onInsertChange() {
				try {
				    int intVal = Integer.parseInt(view.input_customPage().getText());
				    model.setCustomPage(intVal);
				}
				catch (NumberFormatException exception) {}
			}
		});
		
		view.select_certificates().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	Certificate x = (Certificate) view.select_certificates().getSelectedItem();
		    	model.setSigningCertificate((Certificate) view.select_certificates().getSelectedItem());;
		    }
		});
		
		view.select_visibleSignature().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switch (view.select_visibleSignature().getSelectedIndex()) {
		    		case 0:
		    			model.setVisibleSignature(false);
		    			view.getPreview().draggableSignatureField.setVisible(false);
		    		break;
		    		case 1:
		    			model.setVisibleSignature(true);
		    			view.getPreview().draggableSignatureField.setVisible(true);
		    		break;
		    	}
		    	validateFields();
		    }
		});

		view.select_signingPage().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setPage((SigningPage) view.select_signingPage().getSelectedItem());
		    	validateFields();
		    }
		});

		view.select_signatureSize().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setSize((SignatureSize) view.select_signatureSize().getSelectedItem());
		    	view.getPreview().setDraggableSignatureFieldSize((SignatureSize) view.select_signatureSize().getSelectedItem());
		    }
		});

		view.select_signaturePosition().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	model.setPosition((SignaturePosition) view.select_signaturePosition().getSelectedItem());
		    	view.getPreview().setDraggableSignatureFieldPosition((SignaturePosition) view.select_signaturePosition().getSelectedItem());
		    }
		});
		/*
		view.button_preview().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (view.button_preview().getText().equals(">>"))
				{
					App.frame.setPreferredSize(new Dimension(App.frame.getWidth()*2, App.frame.getHeight()));
					view.add(view.RIGHT_PANEL);
					view.revalidate();
					view.repaint();
					App.frame.revalidate();
					App.frame.repaint();
					App.frame.pack();
					view.button_preview().setText("<<");
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
		*/
		view.getPreview().draggableSignatureField.addMouseListener(new MouseListener() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {}
	    	@Override
	    	public void mousePressed(MouseEvent e) {}
	    	@Override
	    	public void mouseReleased(MouseEvent e) {
	    		model.setPosition(view.getPreview().draggableSignatureField.getPctX(), view.getPreview().draggableSignatureField.getPctY());
	    		view.select_signaturePosition().setSelectedItem(SignaturePosition.CUSTOM);
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {}
	    	@Override
	    	public void mouseExited(MouseEvent e) {}
	    });
		
		view.getPreview().range_document.addMouseListener(new MouseListener() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {}
	    	@Override
	    	public void mousePressed(MouseEvent e) {}
	    	@Override
	    	public void mouseReleased(MouseEvent e) {
	    		try {
					view.getPreview().setPreviewDocument(view.getPreview().range_document.getValue(), false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {}
	    	@Override
	    	public void mouseExited(MouseEvent e) {}
	    });
		
		view.getPreview().range_page.addMouseListener(new MouseListener() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {}
	    	@Override
	    	public void mousePressed(MouseEvent e) {}
	    	@Override
	    	public void mouseReleased(MouseEvent e) {
	    		if (view.select_signingPage().getSelectedItem() != SigningPage.ALL_PAGES)
	    		{
	    			view.select_signingPage().setSelectedItem(SigningPage.CUSTOM_PAGE);
	    			view.input_customPage().setText(String.valueOf(view.getPreview().range_page.getValue() +1));
	    			view.input_customPage().setEnabled(view.select_visibleSignature().getSelectedIndex() != 0);
	    		}
	    		view.getPreview().setPreviewPage(view.getPreview().range_page.getValue(), false);
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {}
	    	@Override
	    	public void mouseExited(MouseEvent e) {}
	    });
	}
	
	@Override
	public ISigningService getSigningService() {
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		PAdESService service = new PAdESService(commonCertificateVerifier);
		service.setPdfObjFactory(new PdfBoxNativeObjectFactory());
		Certificate cert = model.getCertificatesHolder().getSelectedCertificate();
		System.out.println("Signing with certificate " + cert);
		
		PAdESSignatureParameters padesParameters = new PAdESSignatureParameters();
		{
			padesParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
			padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
			padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
			padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
			padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
			padesParameters.setReason(model.getReason());
			padesParameters.setLocation(model.getLocation());
		}
		
		if (!model.isVisibleSignature())
			return new PDFInvisibleSigning(model.getCertificatesHolder(), service, padesParameters);
		
		SignatureAspect signatureAspect = new AdobeLikeSignatureAspect(cert, model);
			
		if (model.getPage() == SigningPage.ALL_PAGES && model.isRealSignature())
			return new PDFVisibleAllPagesRealSigning(model.getCertificatesHolder(), service, padesParameters, signatureAspect);
		
		if (model.getPage() == SigningPage.ALL_PAGES)
			return new PDFVisibleAllPagesSigning(model.getCertificatesHolder(), service, padesParameters, signatureAspect);
		
		return new PDFVisibleSigning(model.getCertificatesHolder(), service, padesParameters, signatureAspect);
	}
	
	private void validateFields()
	{
		if (view.select_visibleSignature().getSelectedIndex() == 0) {
			view.select_signaturePosition().setEnabled(false);
			view.select_signatureSize().setEnabled(false);
			view.select_signingPage().setEnabled(false);
			view.check_visibleSN().setEnabled(false);
			view.check_realSignature().setEnabled(false);
			return;
		}
		view.select_signaturePosition().setEnabled(true);
		view.select_signatureSize().setEnabled(true);
		view.select_signingPage().setEnabled(true);
		view.check_visibleSN().setEnabled(true);
		if ((SigningPage) view.select_signingPage().getSelectedItem() == SigningPage.ALL_PAGES) {
			view.check_realSignature().setEnabled(true);
		}
	}

	@Override
	protected void onFilesChoosed(File[] selectedFiles) {
		view.getPreview().addFiles(selectedFiles);
	}
}
