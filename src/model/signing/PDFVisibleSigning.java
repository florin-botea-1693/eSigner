package model.signing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import model.signing.visible.SignatureAspect;

/*
 * ia totul de-a gata configurat si semneaza
 */

public class PDFVisibleSigning implements SigningMode {// class asta se instantiaza o singura data in momentul in care intru pe semnare pdf si semnez ceva, nu si la semnare multi/semnare intre documente.
	
	private CertificatesHolder certificatesHolder;
	private PAdESService service;
	private PAdESSignatureParameters padesParameters;
	private SignatureAspect signatureAspect;

	
	public PDFVisibleSigning(CertificatesHolder certificatesHolder, PAdESService service, PAdESSignatureParameters padesParameters, SignatureAspect signatureAspect) {
		this.certificatesHolder = certificatesHolder;
		this.service = service;
		this.padesParameters = padesParameters;
		this.signatureAspect = signatureAspect;
	}
	
	@Override
	public void performSign(File file) throws FileNotFoundException, IOException { // aici as putea cel mult sa calculez pozitia semnaturii
		System.out.println("Signing with visible signature");
		InputStream resource = new FileInputStream(file);
		PDDocument pdDocument = PDDocument.load(resource);
		SignatureTokenConnection token = this.certificatesHolder.getToken();
		Certificate cert = this.certificatesHolder.getSelectedCertificate();
		
		if (pdDocument.getNumberOfPages() < this.signatureAspect.getPage())
			this.signatureAspect.setPage(pdDocument.getNumberOfPages());
		System.out.println(this.signatureAspect.getPage());
		FileDocument toSignDocument = new FileDocument(file);
		padesParameters.setImageParameters(signatureAspect.getSIP());
		ToBeSigned dataToSign = service.getDataToSign(toSignDocument, padesParameters);
		SignatureValue signatureValue = token.sign(dataToSign, padesParameters.getDigestAlgorithm(), cert.getPrivateKey());
		DSSDocument signedDocument = service.signDocument(toSignDocument, padesParameters, signatureValue);
		signedDocument.save(file.getAbsolutePath() + "-semnat.pdf");
	}
}
