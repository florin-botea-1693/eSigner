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
import model.signing.visible.AdobeLikeSignatureAspect;
import model.signing.visible.SignatureAspect;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import services.signing.PDFInvisibleSigning;
import services.signing.PDFVisibleAllPagesRealSigning;
import services.signing.PDFVisibleAllPagesSigning;
import services.signing.PDFVisibleSigning;
import utils.PropertyChangeSupportExtended;

public class PdfSigningModel extends SigningModel
{
	private boolean isVisibleSignature = false;
	private boolean isVisibleSN = false;
	private String organization = "";
	private String reason = "";
	private boolean isVisibleReason = false;
	private String location = "";
	private boolean isVisibleLocation = false;
	private SignatureSize size = SignatureSize.MEDIUM;
	private SignaturePosition position = SignaturePosition.TOP_LEFT;
	private Point customPosition = new Point(0, 0);
	private SigningPage page = SigningPage.FIRST_PAGE;
	/**
	 * atunci cand se seteaza pagina, se seteaza si customPage, variabila care va fi pusa in sip.setPage(p)
	 */
	private int customPage = 1;
	private boolean isRealSignature = false;
	
	public boolean isVisibleSignature() {
		return isVisibleSignature;
	}

	public boolean isVisibleSN() {
		return isVisibleSN;
	}

	public String getOrganization() {
		return organization;
	}

	public String getReason() {
		return reason;
	}

	public boolean isVisibleReason() {
		return isVisibleReason;
	}

	public String getLocation() {
		return location;
	}

	public boolean isVisibleLocation() {
		return isVisibleLocation;
	}

	public SignatureSize getSize() {
		return size;
	}

	public SignaturePosition getPosition() {
		return position;
	}
	
	public Point getCustomPosition() {
		return customPosition;
	}

	public SigningPage getPage() {
		return page;
	}

	public int getCustomPage() {
		return customPage;
	}

	public boolean isRealSignature() {
		return isRealSignature;
	}

	public PdfSigningModel(CertificatesHolder certificatesHolder)
	{
		super(certificatesHolder);
		
		// load from settings
		try {
			SigningSettings settings = SigningSettings.getInstance();
			SigningSettingsRecord s = settings.getSigningSettings();
			this.setOrganization(s.organization);
			this.setReason(s.signing_reason);
			this.setVisibleReason(s.is_visible_reason);
			this.setLocation(s.signing_location);
			this.setVisibleLocation(s.is_visible_location);
			this.setVisibleSN(s.is_visible_sn);
			this.setSize(s.signature_size);
			this.setPosition(s.signature_position);
			this.setPage(s.signing_page);
			this.setRealSignature(s.is_real_signature);
			//this.setCustomPage(s.custom_signing_page);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setVisibleSignature(boolean isVisibleSignature) {
		this.isVisibleSignature = isVisibleSignature;
	}

	public void setVisibleSN(boolean isVisibleSN) {
		this.isVisibleSN = isVisibleSN;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setReason(String reason) {
		String oldVal = this.reason;
		this.reason = reason;
		System.out.println(oldVal + " -> " + reason);
	}

	public void setVisibleReason(boolean isVisibleReason) {
		this.isVisibleReason = isVisibleReason;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setVisibleLocation(boolean isVisibleLocation) {
		this.isVisibleLocation = isVisibleLocation;
	}

	public void setSize(SignatureSize size) {
		this.size = size;
	}

	public void setPosition(SignaturePosition position) {
		this.position = position;
	}

	public void setPage(SigningPage page) {
		this.page = page;
		switch (page) {
			case FIRST_PAGE:
				this.customPage = 1;
			break;
			case LAST_PAGE:
				System.out.println("selected last page");
				this.customPage = 9999;
			break;
			case CUSTOM_PAGE:
			break;
		}
	}

	public void setCustomPage(int customPage) {
		this.customPage = customPage;
	}

	public void setRealSignature(boolean isRealSignature) {
		this.isRealSignature = isRealSignature;
	}

	public void setPosition(int pctX, int pctY) {
		this.customPosition.setLocation(pctX, pctY);
		this.setPosition(SignaturePosition.CUSTOM);
	}
}
