package model.signing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import model.certificates.Certificate;
import model.certificates.CertificatesHolder;

public class PDFInvisibleSigning implements SigningMode {

	private CertificatesHolder certificatesHolder;
	private PAdESService service;
	private PAdESSignatureParameters padesParameters;

	public PDFInvisibleSigning(CertificatesHolder certificatesHolder, PAdESService service, PAdESSignatureParameters padesParameters) {
		this.certificatesHolder = certificatesHolder;
		this.service = service;
		this.padesParameters = padesParameters;
		this.padesParameters.setImageParameters(null);
	}

	@Override
	public void performSign(File file) throws FileNotFoundException, IOException {
		System.out.println("Signing with invisible signature");
		SignatureTokenConnection token = certificatesHolder.getToken();
		Certificate cert = certificatesHolder.getSelectedCertificate();
		
		FileDocument toSignDocument = new FileDocument(file);
		String target = file.getAbsolutePath() + "-semnat.pdf";

		ToBeSigned dataToSign = service.getDataToSign(toSignDocument, padesParameters);
		SignatureValue signatureValue = token.sign(dataToSign, padesParameters.getDigestAlgorithm(), cert.getPrivateKey());
		DSSDocument signedDocument = service.signDocument(toSignDocument, padesParameters, signatureValue);
		signedDocument.save(target);
	}

}
