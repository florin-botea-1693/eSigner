package model.signing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.signing.visible.SignatureAspect;

public class PDFVisibleAllPagesRealSigning implements SigningMode {

	private CertificatesHolder certificatesHolder;
	private PAdESService service;
	private PAdESSignatureParameters padesParameters;
	private SignatureAspect signatureAspect;

	public PDFVisibleAllPagesRealSigning(CertificatesHolder certificatesHolder, PAdESService service, PAdESSignatureParameters padesParameters, SignatureAspect signatureAspect) 
	{
		this.certificatesHolder = certificatesHolder;
		this.service = service;
		this.padesParameters = padesParameters;
		this.signatureAspect = signatureAspect;
	}
	
	@Override
	public void performSign(File file) throws FileNotFoundException, IOException {
		System.out.println("Signing with visible signature for real on all pages");
		InputStream resource = new FileInputStream(file);
		String result = file.getAbsolutePath() + "-semnat.pdf";
		PDDocument pdDocument = PDDocument.load(resource);
		
		SignatureTokenConnection token = certificatesHolder.getToken();
		Certificate cert = this.certificatesHolder.getSelectedCertificate();
		
		String currentPath = file.getAbsolutePath();
		for (int i=1; i <= pdDocument.getNumberOfPages(); i++) {
			FileDocument toSignDocument = new FileDocument(new File(currentPath));
			SignatureImageParameters sip = signatureAspect.generateSignatureImageParameters(pdDocument, i);
			this.padesParameters.setImageParameters(sip);
			ToBeSigned dataToSign = this.service.getDataToSign(toSignDocument, this.padesParameters);
			SignatureValue signatureValue = token.sign(dataToSign, this.padesParameters.getDigestAlgorithm(), cert.getPrivateKey());
			DSSDocument signedDocument = this.service.signDocument(toSignDocument, this.padesParameters, signatureValue);
			signedDocument.save(result);
			currentPath = result;
		}
	}

}
