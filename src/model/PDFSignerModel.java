package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.pdf.pdfbox.PdfBoxNativeObjectFactory;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.signing.PDFInvisibleSigning;
import model.signing.PDFVisibleAllPagesRealSigning;
import model.signing.PDFVisibleAllPagesSigning;
import model.signing.PDFVisibleSigning;
import model.signing.SigningMode;
import model.signing.visible.SignatureAspect;
import model.signing.visible.SignatureAspectDelegate;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import model.signing.visible.SigningPage;
import utils.PropertyChangeSupportExtended;

/*
 * PDFSigner este instantiat in momentul in care utilizatorul intra pe semnare PDF. 
 * El pregateste doar parametrii de semnare pentru a fi dati mai departe catre class-ele ce practic vor semna documentele
 * Class-ele ce vor semna, nu vor avea foarte multa logica de implementat, primesc totul de-a gata, deoarece totul se face centralizat in acest loc
 * */

public final class PDFSignerModel {
	
	private PropertyChangeSupportExtended observed;
	
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
	public Certificate getSelectedCertificate() {return certificatesHolder.getSelectedCertificate();}
	public SignaturePosition getSignaturePosition() {return signatureAspect.getSignaturePosition();}
	public SignatureSize getSignatureSize() {return signatureAspect.getSize();}
	public SigningPage getSigningPage() {return this.signingPage;}
	public String getSigningReason() {return this.padesParameters.getReason();}
	public String getSigningLocation() {return this.padesParameters.getLocation();}
	public boolean isVisibleSerialNumber() {return this.signatureAspect.isVisibleSerialNumber();}
	public boolean isVisibleReason() {return this.signatureAspect.isVisibleReason();}
	public boolean isVisibleLocation() {return this.signatureAspect.isVisibleLocation();}
	public boolean isRealSignature() {return this.isRealSignature;}
	public boolean isVisibleSignature() {return this.isVisibleSignature;}
	public int getCustomSigningPage() {return this.signatureAspect.getPage();}
	
	//====================||
	// >>> CONSTRUCTOR <<<
	//====================||
	public PDFSignerModel(CertificatesHolder certificatesHolder) {
		this.observed = new PropertyChangeSupportExtended(this);
		this.certificatesHolder = certificatesHolder;
		
		this.signatureAspect = SignatureAspectDelegate.getAspect("BasicSignatureAspect");
		
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		this.service = new PAdESService(commonCertificateVerifier);
		this.service.setPdfObjFactory(new PdfBoxNativeObjectFactory());
		this.padesParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
		loadFromSettings();
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
	// OBSERVER METHODS
	//====================||

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
        observed.addPropertyChangeListener(pcl);
        updateObserver(pcl);
    }
 
    private void updateObserver(PropertyChangeListener pcl) {
    	pcl.propertyChange(new PropertyChangeEvent(this, "certificates", null, certificatesHolder.getCertificates()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "selectedCertificate", null, certificatesHolder.getSelectedCertificate()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "isVisibleSN", null, this.isVisibleSerialNumber()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "signingReason", null, this.getSigningReason()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "isVisibleSigningReason", null, this.isVisibleReason()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "signingLocation", null, this.getSigningLocation()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "isVisibleLocation", null, this.isVisibleLocation()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "signatureVisibility", null, this.isVisibleSignature));
    	pcl.propertyChange(new PropertyChangeEvent(this, "signingPage", null, this.getSigningPage()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "isRealSignature", null, this.isRealSignature()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "customSigningPage", null, this.getCustomSigningPage()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "signaturePosition", null, this.getSignaturePosition()));
    	pcl.propertyChange(new PropertyChangeEvent(this, "signatureSize", null, this.getSignatureSize()));
    }
    
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
    	observed.removePropertyChangeListener(pcl);
    }
	
	public PDFSignerModel ignoreNextEvent() {
		observed.ignoreNextEvent();
		return this;
	}
  
    //========================================================================================================
	
	//====================||
	// OBSERVABLE SETTERS
    // !!! - daca oldVal == newVal, asta nu mai emite nimic, am testat eu
	//====================||
	public void refreshCertificatesList() {
		observed.firePropertyChange("certificates", null, certificatesHolder.getCertificates());
	}
	
	public void setSigningCertificate(Certificate cert) {
		observed.firePropertyChange("selectedCertificate", null, cert);
		if (cert == null) return;
		padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
		padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
		padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
		padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
		certificatesHolder.selectCertificate(cert);
		signatureAspect.setCertificate(cert);
	}
	
	public void setSignatureSize(SignatureSize size) {
		signatureAspect.setSize(size);
		observed.firePropertyChange("signatureSize", null, size);
	}

	public void setVisibleSignature(boolean b) {
		isVisibleSignature = b;
		this.observed.firePropertyChange("signatureVisibility", null, b);
	}
	
	public void setVisibleSN(boolean b) {
		this.signatureAspect.setVisibleSerialNumber(b);
		this.observed.firePropertyChange("isVisibleSN", false, b);
	}

	public void setSigningReason(String text) {
		padesParameters.setReason(text);
		signatureAspect.setReason(text);
		this.observed.firePropertyChange("signingReason", null, text);
	}

	public void setSigningLocation(String text) {
		padesParameters.setLocation(text);
		signatureAspect.setLocation(text);
		observed.firePropertyChange("signingLocation", null, text);
	}

	public void setIsRealSignature(boolean b) {
		this.isRealSignature = b;
		this.observed.firePropertyChange("isRealSignature", null, b);
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
				//
			break;
		}
		observed.firePropertyChange("signingPage", null, sp);
	}
	// for custom page
	public void setSigningPage(int sp) {
		signatureAspect.setPage(sp);
		observed.firePropertyChange("signingPage", null, sp);
	}
	
	public void setSignaturePosition(SignaturePosition position) {
		signatureAspect.setPosition(position);
		observed.firePropertyChange("signaturePosition", null, position);
	}
	
	public void setVisibleReason(boolean b) {
		boolean oldVal = signatureAspect.isVisibleReason();
		signatureAspect.setVisibleReason(b);
		observed.firePropertyChange("isVisibleReason", oldVal, b);
	}
	
	public void setVisibleLocation(boolean b) {
		boolean oldVal = signatureAspect.isVisibleLocation();
		signatureAspect.setVisibleLocation(b);
		observed.firePropertyChange("isVisibleLocation", oldVal, b);
	}
	
	public void setSigningOrganization(String text) {
		signatureAspect.setOrganization(text);
		observed.firePropertyChange("organization", null, text);
	}
	//==================================\\

	//===============================================================||
	// Model can save that stats in a prop file for preferences
	// now, we setup model from preferences
	//===============================================================||
	public void loadFromSettings() {
		try {
			SigningSettings settings = SigningSettings.getInstance();
			SigningSettingsRecord s = settings.getSigningSettings();
			setSigningOrganization(s.organization);
			setSigningReason(s.signing_reason);
			setVisibleReason(s.is_visible_reason);
			setSigningLocation(s.signing_location);
			setVisibleLocation(s.is_visible_location);
			setVisibleSN(s.is_visible_sn);
			setSignatureSize(s.signature_size);
			setSignaturePosition(s.signature_position);
			setSigningPage(s.signing_page);
			setIsRealSignature(s.is_real_signature);
			setSigningPage(s.custom_signing_page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	public void loadFromSettings() {
		signatureAspect = SignatureAspectDelegate.getAspect("BasicSignatureAspect");
		Certificate cert = certificatesHolder.getSelectedCertificate();
		if (cert != null)
			setSigningCertificate(cert);
	}

	
	private void loadFromSettings(String certSN) {
		// aici ar trebui sa setez si in certificateholder certificatul implicit
		
		// options get based on prefered certificate
		// set with view listening
		
		/* configure signature presentation data */

		//certificatesHolder.selectCertificate(cert);
		/* !!cel mai important!! nu pot construi grafica semnatura fara un certificat 
		//signatureAspect.setCertificate(cert);
	}
	
	private void loadFromSettings(Certificate cert) {
		// poate si o validare aici
		SigningSettingsRecord s = null;
		try {
			s = SigningSettings.getInstance().getCertificateSigningSettings(cert.getPrivateKey().getCertificate().getSerialNumber());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (s == null)
			return;

		setSigningReason(s.signing_reason);
		setVisibleReason(s.is_visible_reason);
		setSigningLocation(s.signing_location);
		setVisibleLocation(s.is_visible_location);
		setVisibleSN(s.is_visible_sn);
		setSignatureSize(s.signature_size);
		setSignaturePosition(s.signature_position);
		setSigningPage(s.signing_page);
		setIsRealSignature(s.is_real_signature);
		setSigningPage(s.custom_signing_page);
	}
	*/
}
