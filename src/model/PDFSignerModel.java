package model;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.event.SwingPropertyChangeSupport;

import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.pdf.pdfbox.PdfBoxNativeObjectFactory;
import eu.europa.esig.dss.spi.x509.CommonCertificateSource;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.signing.PDFInvisibleSigning;
import model.signing.PDFVisibleAllPagesRealSigning;
import model.signing.PDFVisibleAllPagesSigning;
import model.signing.PDFVisibleSigning;
import model.signing.SigningMode;
import model.signing.options.PdfSigningOptions;
import model.signing.visible.AdobeLikeSignatureAspect;
import model.signing.visible.SignatureAspect;
import model.signing.visible.SignatureAspectSettings;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import utils.PropertyChangeSupportExtended;

public class PdfSignerModel
{
	private PropertyChangeSupportExtended observed = new PropertyChangeSupportExtended(this);
	
	private CertificatesHolder certificatesHolder;
	private PdfSigningOptions signingOptions = new PdfSigningOptions();

	public PdfSignerModel(CertificatesHolder certificatesHolder)
	{
		this.certificatesHolder = certificatesHolder;
		
		this.loadFromSettings();
	}
	
	public CertificatesHolder getCertificatesHolder() {
		return certificatesHolder;
	}
	
	public PdfSigningOptions getSigningOptions() {
		return this.signingOptions;
	}
	
	public void setCertificatesHolder(CertificatesHolder certificatesHolder) {
		CertificatesHolder oldVal = this.certificatesHolder;
		certificatesHolder = certificatesHolder;
		observed.firePropertyChange("certificatesHolder", oldVal, certificatesHolder);
	}
	
	public void setSigningCertificate(Certificate selectedItem) {
		Certificate oldVal = this.certificatesHolder.getSelectedCertificate();
		certificatesHolder.selectCertificate(selectedItem);
		observed.firePropertyChange("selectedCertificate", oldVal, selectedItem);
	}
	
	public void loadFromSettings()
	{
		try {
			SigningSettings settings = SigningSettings.getInstance();
			SigningSettingsRecord s = settings.getSigningSettings();
			this.signingOptions.setOrganization(s.organization);
			this.signingOptions.setReason(s.signing_reason);
			this.signingOptions.setVisibleReason(s.is_visible_reason);
			this.signingOptions.setLocation(s.signing_location);
			this.signingOptions.setVisibleLocation(s.is_visible_location);
			this.signingOptions.setVisibleSN(s.is_visible_sn);
			this.signingOptions.setSize(s.signature_size);
			this.signingOptions.setPosition(s.signature_position);
			this.signingOptions.setPage(s.signing_page);
			this.signingOptions.setRealSignature(s.is_real_signature);
			this.signingOptions.setCustomPage(s.custom_signing_page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SigningMode getPdfSigner() 
	{
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		PAdESService service = new PAdESService(commonCertificateVerifier);
		service.setPdfObjFactory(new PdfBoxNativeObjectFactory());
		Certificate cert = this.certificatesHolder.getSelectedCertificate();
		System.out.println("Signing with certificate " + cert);
		
		PAdESSignatureParameters padesParameters = new PAdESSignatureParameters();
		{
			padesParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
			padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
			padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
			padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
			padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
			padesParameters.setReason(signingOptions.getReason());
			padesParameters.setLocation(signingOptions.getLocation());
		}
		
		if (!signingOptions.isVisibleSignature())
			return new PDFInvisibleSigning(certificatesHolder, service, padesParameters);
		
		SignatureAspect signatureAspect = new AdobeLikeSignatureAspect(cert, signingOptions);
			
		if (signingOptions.getPage() == SigningPage.ALL_PAGES && signingOptions.isRealSignature())
			return new PDFVisibleAllPagesRealSigning(certificatesHolder, service, padesParameters, signatureAspect);
		
		if (signingOptions.getPage() == SigningPage.ALL_PAGES)
			return new PDFVisibleAllPagesSigning(certificatesHolder, service, padesParameters, signatureAspect);
		
		return new PDFVisibleSigning(certificatesHolder, service, padesParameters, signatureAspect);
	}
	
	public void addPropertyChangeListener(String prop, PropertyChangeListener propertyChangeListener) 
	{
		observed.addPropertyChangeListener(prop, propertyChangeListener);
		propertyChangeListener.propertyChange(new PropertyChangeEvent(this, prop, null, null));
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		observed.removePropertyChangeListener(pcl);
	}
}
