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
import model.signing.PDFVisibleSigning;
import model.signing.SigningMode;
import model.signing.visible.SignatureAspect;
import model.signing.visible.SignatureAspectDelegate;

/*
 * PDFSigner este instantiat in momentul in care utilizatorul intra pe semnare PDF. 
 * El pregateste doar parametrii de semnare pentru a fi dati mai departe catre class-ele ce practic vor semna documentele
 * Class-ele ce vor semna, nu vor avea foarte multa logica de implementat, primesc totul de-a gata, deoarece totul se face centralizat in acest loc
 * */

public final class PDFSignerModel {
	
	private CertificatesHolder certificatesHolder;
	private PAdESService service;
	private PAdESSignatureParameters padesParameters = new PAdESSignatureParameters();
	private SignatureAspect signatureAspect; // signature aspect nu se va modifica niciodata in momentul semnarii, sau pe meniul de semnare, ci doar in setari
	
	
	/* in baza membrilor de mai jos voi sti in getMode ce class sa intorc */
	private boolean isVisibleSignature = false;
	private boolean isRealSignature = false;
	private int signingPage = 1;
	
	// nu cred ca voi avea neaparat options aici... nu prea ar avea rost... trec options doar ca sa ma ajut de el sa construiesc parametrii
	private PDFSigningOptions signingOptions;

	public CertificatesHolder getCertificatesHolder() {return certificatesHolder;}
	public PAdESSignatureParameters getPadesParameters() {return padesParameters;}
	public PDFSigningOptions getSigningOptions() {return signingOptions;}
	public PAdESService getPadesService() {return service;}
	public List<Certificate> getCertificates() {return certificatesHolder.getCertificates();}
	
	
	/**
	 * CONSTRUCTOR - creates pdf signing model, a wrapper around multiple similar signing modes that needs same parameters
	 * @param certificatesHolder
	 * @param options - a SQL-lite poate
	 */
	public PDFSignerModel(CertificatesHolder certificatesHolder, PDFSigningOptions options) {
		this.certificatesHolder = certificatesHolder;
		this.signingOptions = options;
		
		initFromOptions(options);
	}
	
	/**
	 * MAIN METHOD
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void sign(File file) throws FileNotFoundException, IOException {
		getSigningMode("pdf.visible.single-page").performSign(file);
	}
	private SigningMode getSigningMode(String m) {
		return new PDFVisibleSigning(certificatesHolder, service, padesParameters, signatureAspect);
	}
	
	/* SETTERS */
	
	public void setSigningCertificate(Certificate cert) {
		padesParameters.setSigningCertificate(cert.getPrivateKey().getCertificate());
		padesParameters.setCertificateChain(cert.getPrivateKey().getCertificateChain());
		certificatesHolder.selectCertificate(cert);
		signatureAspect.setCertificate(cert);
	}
	
	public void setSize(String size) {
		signatureAspect.setSize(size);
	}

	public void setVisibleSignature(boolean b) {
		isVisibleSignature = b;
	}
	
	public void setVisibleSN(boolean b) {
		signatureAspect.setVisibleSerialNumber(b);
	}

	public void setReason(String text, boolean visible) {
		padesParameters.setReason(text);
		String visibleReason = visible ? text : null;
		signatureAspect.setReason(visibleReason);
	}

	public void setLocation(String text, boolean visible) {
		padesParameters.setLocation(text);
		String visibleLocation = visible ? text : null;
		signatureAspect.setLocation(visibleLocation);
	}

	public void setIsRealSignature(boolean b) {
		isRealSignature = b;
	}

	public void setPage(int i) {
		signingPage = i;
		signatureAspect.setPage(i);
	}
	
	public void setSignaturePosition() {
		signatureAspect.setPosition();
	}

	public void initFromOptions(PDFSigningOptions options) {
		Certificate cert = certificatesHolder.getSelectedCertificate();
		// no certificate
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		service = new PAdESService(commonCertificateVerifier);
		service.setPdfObjFactory(new PdfBoxNativeObjectFactory());
		
		signatureAspect = SignatureAspectDelegate.getAspect("BasicSignatureAspect");
		
		System.out.println(signatureAspect);
		
		/* configure aspect based on app config data */
		setPage(1);
		setReason("asa vreau eu", true);
		setLocation("la DigiSait", true);
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
