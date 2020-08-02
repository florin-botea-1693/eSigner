package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextHorizontalAlignment;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextPosition;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextVerticalAlignment;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.pdf.pdfbox.PdfBoxNativeObjectFactory;
import eu.europa.esig.dss.pdf.pdfbox.visible.defaultdrawer.ImageTextWriter;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.signing.PDFInvisibleSigning;
import model.signing.PDFSigningOptions;
import model.signing.PDFVisibleAllPagesRealSigning;
import model.signing.PDFVisibleAllPagesSigning;
import model.signing.PDFVisibleSigning;
import model.signing.SigningMode;
import model.signing.visible.SignatureAspect;
import model.signing.visible.SignatureAspectDelegate;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import model.signing.visible.SigningPage;

/*
 * PDFSigner este instantiat in momentul in care utilizatorul intra pe semnare PDF. 
 * El pregateste doar parametrii de semnare pentru a fi dati mai departe catre class-ele ce practic vor semna documentele
 * Class-ele ce vor semna, nu vor avea foarte multa logica de implementat, primesc totul de-a gata, deoarece totul se face centralizat in acest loc
 * */

public final class PDFSignerModel {
	
	private PropertyChangeSupport observed;
	
	private CertificatesHolder certificatesHolder;
	private PAdESService service;
	private PAdESSignatureParameters padesParameters = new PAdESSignatureParameters();
	private SignatureAspect signatureAspect;
	
	//===================================================||
	// USED FOR DECIDE WHICH SIGNER TO INSTANTIATE
	//===================================================||
	private boolean isVisibleSignature = true;
	private boolean isRealSignature = false;
	private SigningPage signingPage = SigningPage.FIRST_PAGE;
	//========================\\
	
	public CertificatesHolder getCertificatesHolder() {return certificatesHolder;}
	public PAdESSignatureParameters getPadesParameters() {return padesParameters;}
	public PAdESService getPadesService() {return service;}
	public List<Certificate> getCertificates() {return certificatesHolder.getCertificates();}
	
	//====================||
	// >>> CONSTRUCTOR <<<
	//====================||
	public PDFSignerModel(CertificatesHolder certificatesHolder) {
		this.observed = new PropertyChangeSupportExtended(this);
		this.certificatesHolder = certificatesHolder;
	}
	//===================\\
	
	//=============||
	// MAIN METHOD
	//=============||
	public void sign(File file) throws FileNotFoundException, IOException {
		String mode = "pdf";
		mode += (this.isVisibleSignature ? ".visible" : ".invisible");
		mode += (this.signingPage == SigningPage.ALL_PAGES && this.isVisibleSignature ? ".all-pages" : "");
		mode += (this.signingPage == SigningPage.ALL_PAGES && this.isVisibleSignature && this.isRealSignature ? "-real" : "");
		
		getSigningMode(mode).performSign(file);
	}
	
	//====================||
	// OBSERVER METHODS
	//====================||
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observed.addPropertyChangeListener(pcl);
        pcl.propertyChange(new PropertyChangeEvent(this, "*",  null, newVal));
    }
 
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	support.removePropertyChangeListener(pcl);
    }
    
    public PDFSignerModel stealthThisTime() {
		this.observed.skipNextTurn();
		return this;
	}
    //====================\\
	
	private SigningMode getSigningMode(String m) {
		SigningMode sm = null;
		switch (m) {
			case "pdf.visible":
				sm = new PDFVisibleSigning(certificatesHolder, service, padesParameters, signatureAspect);
			break;
			case "pdf.visible.all-pages":
				sm = new PDFVisibleAllPagesSigning(certificatesHolder, service, padesParameters, signatureAspect);
			break;
			case "pdf.invisible":
				sm = new PDFInvisibleSigning(certificatesHolder, service, padesParameters);
			break;
			case "pdf.visible.all-pages-real":
				sm = new PDFVisibleAllPagesRealSigning(certificatesHolder, service, padesParameters, signatureAspect);
			break;
		}
		return sm;
	}
	
	//====================||
	// OBSERVABLE SETTERS
	//====================||
	public void setSigningCertificate(Certificate cert) {
		padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
		padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
		certificatesHolder.selectCertificate(cert);
		signatureAspect.setCertificate(cert);
		this.observable.firePropertyChange("news", this.news, value);
	}
	
	public void setSignatureSize(SignatureSize size) {
		signatureAspect.setSize(size);
	}

	public void setVisibleSignature(boolean b) { System.out.println("setting visible signature to " + b);
		isVisibleSignature = b;
	}
	
	public void setVisibleSN(boolean b) {
		this.signatureAspect.setVisibleSerialNumber(b);
	}

	public void setSigningReason(String text, boolean visible) {
		padesParameters.setReason(text);
		String visibleReason = visible ? text : null;
		signatureAspect.setReason(visibleReason);
	}

	public void setSigningLocation(String text, boolean visible) {
		padesParameters.setLocation(text);
		String visibleLocation = visible ? text : null;
		signatureAspect.setLocation(visibleLocation);
	}

	public void setIsRealSignature(boolean b) {
		isRealSignature = b;
	}

	public void setSigningPage(SigningPage sp) {
		this.signingPage = sp;
		switch (sp) {
			case FIRST_PAGE:
				this.signatureAspect.setPage(1);
			break;
			case LAST_PAGE:
				this.signatureAspect.setPage(9999);
			break;
			case CUSTOM_PAGE:
				// enable custom input
			break;
		}
	}
	// for custom page
	public void setSigningPage(int sp) {
		this.signatureAspect.setPage(sp);
	}
	
	public void setSignaturePosition(SignaturePosition position) {
		signatureAspect.setPosition(position);
	}
	
	public boolean isRealSignature() {
		return this.isRealSignature;
	}
	
	public int isVisibleSignature() {
		if (this.isVisibleSignature) {
			return 1;
		}
		return 0;
	}
	//====================\\
	
	//===============================================||
	// GETTERS FOR SIGNATURE PRESENTATION
	//===============================================||
	public String getSigningReason() {
		return this.padesParameters.getReason();
	}
	public String getSigningLocation() {
		return this.padesParameters.getLocation();
	}
	
	//===============================================||
	// GETTERS FOR SIGNATURE ASPECT AND POSITION
	//===============================================||
	public SignaturePosition getSignaturePosition() {
		return signatureAspect.getSignaturePosition();
	}
	public SignatureSize getSignatureSize() {
		return signatureAspect.getSize();
	}
	public SigningPage getSigningPage() {
		return this.signingPage;
	}
	public boolean isVisibleReason() {
		return this.isVisibleReason;
	}
	public boolean isVisibleLocation() {
		return this.isVisibleLocation;
	}
	public boolean isVisibleSerialNumber() {
		return this.signatureAspect.isVisibleSerialNumber();
	}

	//===============================================================||
	// Model can save that stats in a prop file for preferences
	// now, we setup model from preferences
	//===============================================================||
	public void loadFromOptions(PDFSigningOptions options) {
		// options get prefered certificate
		// set prefered certificate with view listening
	}

	public void loadFromOptions(PDFSigningOptions options, Certificate cert) {
		// options get based on prefered certificate
		// set with view listening
		Certificate cert = certificatesHolder.getSelectedCertificate();
		// no certificate
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		service = new PAdESService(commonCertificateVerifier);
		service.setPdfObjFactory(new PdfBoxNativeObjectFactory());
		
		signatureAspect = SignatureAspectDelegate.getAspect("BasicSignatureAspect");
		/* configure aspect based on app config data */
		setSigningReason("asa vreau eu", true);
		setSigningLocation("la DigiSait", true);
		signatureAspect.setVisibleSerialNumber(true);
		
		/* configure signature presentation data */
		padesParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
		if (cert == null) return;
		padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
		padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
		certificatesHolder.selectCertificate(cert);
		/* !!cel mai important!! nu pot construi grafica semnatura fara un certificat */
		signatureAspect.setCertificate(cert);
	}
}
