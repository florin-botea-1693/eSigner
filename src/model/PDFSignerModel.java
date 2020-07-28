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
import model.signing.PDFSigningOptions;
import model.signing.visible.SignatureAspect;
import model.signing.visible.SignatureAspectConfig;
import model.signing.visible.SignatureImagePWrapper;

/*
 * PDFSigner este instantiat in momentul in care utilizatorul intra pe semnare PDF. 
 * El pregateste doar parametrii de semnare pentru a fi dati mai departe catre class-ele ce practic vor semna documentele
 * Class-ele ce vor semna, nu vor avea foarte multa logica de implementat, primesc totul de-a gata, deoarece totul se face centralizat in acest loc
 * */

public final class PDFSignerModel {
	
	private CertificatesHolder certificatesHolder;
	private PAdESService service;
	private PAdESSignatureParameters padesParameters = new PAdESSignatureParameters();
	private SignatureAspectConfig signatureAspectConfig;
	private SignatureAspectDelegate signatureAspectDelegate = new SignatureAspectDelegate();
	private SigningModeDelegate signingModeDelegate = new SigningModeDelegate();
	
	private boolean isVisibleSignature = false;
	private boolean isRealSignature = false;
	private int signingPage = 1;
	
	// nu cred ca voi avea neaparat options aici... nu prea ar avea rost... trec options doar ca sa ma ajut de el sa construiesc parametrii
	private PDFSigningOptions signingOptions;

	public PDFSignerModel(CertificatesHolder certificatesHolder, PDFSigningOptions options) {
		this.certificatesHolder = certificatesHolder;
		this.signingOptions = options;
		
		initFromOptions(options);
	}
	
	public void sign(File file) {
		signingModeDelegate.getMode("pdf.visible.single-page", this).performSign(file);
	}
	
	public List<Certificate> getCertificates() {
		return this.certificatesHolder.getCertificates();
	}
	
	public void setSigningCertificate(Certificate cert) {
		padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
		padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
		certificatesHolder.selectCertificate(cert);
		visualSignature.setCertificate(cert);
	}
	
	public void setSize(String size) {
		visualSignature.setSize(size);
	}
	
	public SignatureImagePWrapper getVisibleSignatureWrapper() {
		return this.visualSignature;
	}
	
	public CertificatesHolder getCertificatesHolder() {
		return this.certificatesHolder;
	}
	
	public PAdESSignatureParameters getPadesParameters() {
		return this.padesParameters;
	}
	
	public PDFSigningOptions getSigningOptions() {
		return this.signingOptions;
	}
	
	public PAdESService getPadesService() {
		return this.service;
	}
	
	public void setVisibleSignature(boolean b) {
		isVisibleSignature = b;
	}

	public void initFromOptions(PDFSigningOptions options) {
		Certificate cert = certificatesHolder.getSelectedCertificate();
		// no certificate
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		service = new PAdESService(commonCertificateVerifier);
		service.setPdfObjFactory(new PdfBoxNativeObjectFactory());
		// set page, setposition, setvisible, add extra data to signatureDesign (font, size, signatureImage)
		
		padesParameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
		padesParameters.setReason("reason");
		padesParameters.setContactInfo("setContactInfo");
		
		if (cert == null) return;
		padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
		padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
	}
	
	/**
	 * intoarce tot ce tine de grafica semnaturii, width, height, etc. si va fi folosit in PadesParameters
	 * @return SignatureAspect used in padesParameters.setImageParameter
	 */
	
	public SignatureAspect getSignatureAspect() {
		return signatureAspectDelegate.getAspect(signatureAspectConfig);
	}

	public void setVisibleSN(boolean selected) {
		//
	}

	public void setReason(String text, boolean visible) {
		padesParameters.setReason(text);
		visualSignature.setReason(text, visible);
	}

	public void setLocation(String text, boolean visible) {
		padesParameters.setLocation(text);
		visualSignature.setLocation(text, visible);
	}

	public void setIsRealSignature(boolean b) {
		isRealSignature = b;
	}

	public void setPage(int i) {
		signingPage = i;
	}
	

}
