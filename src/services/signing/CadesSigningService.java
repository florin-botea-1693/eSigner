package services.signing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.europa.esig.dss.cades.CAdESSignatureParameters;
import eu.europa.esig.dss.cades.signature.CAdESService;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import model.CadesSigningModel;
import model.certificates.CertificatesHolder;

public class CadesSigningService implements ISigningService
{
	CertificatesHolder certificatesHolder;
	CadesSigningModel m;
	
	public CadesSigningService(CadesSigningModel m)
	{
		this.certificatesHolder = m.getCertificatesHolder();
		this.m = m;
	}
	
	public void sign(File file) throws IOException
	{
		String target = file.getAbsolutePath() + "-semnat." + m.getSigningExtension();
    	
    	SignatureTokenConnection token = certificatesHolder.getToken();
    	DSSPrivateKeyEntry privateKey = certificatesHolder.getSelectedCertificate().getPrivateKey();
        
        FileDocument toSignDocument = new FileDocument(file);
        List<DSSDocument> detachedContents = new ArrayList<DSSDocument>();
        detachedContents.add(toSignDocument);
		
		CAdESSignatureParameters parameters = new CAdESSignatureParameters();
		// We choose the level of the signature (-B, -T, -LT, -LTA).
		//parameters.setSignatureLevel(SignatureLevel.CAdES_BASELINE_B);
		parameters.setSignatureLevel(SignatureLevel.CAdES_BASELINE_B);
		// We choose the type of the signature packaging (ENVELOPING, DETACHED).

		parameters.setDetachedContents(detachedContents);
		parameters.setSignaturePackaging(m.getSignaturePackaging());
		// We set the digest algorithm to use with the signature algorithm. You must use the
		// same parameter when you invoke the method sign on the token. The default value is
		// SHA256
		parameters.setDigestAlgorithm(m.getDigestAlgorithm());

		// We set the signing certificate
		parameters.setSigningCertificate(privateKey.getCertificate());
		// We set the certificate chain
		parameters.setCertificateChain(privateKey.getCertificateChain());

		// Create common certificate verifier
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		// Create CAdES xadesService for signature
		CAdESService service = new CAdESService(commonCertificateVerifier);

		
		
		// Get the SignedInfo segment that need to be signed.
		ToBeSigned dataToSign = service.getDataToSign(toSignDocument, parameters);

		// This function obtains the signature value for signed information using the
		// private key and specified algorithm
		DigestAlgorithm digestAlgorithm = parameters.getDigestAlgorithm();
		SignatureValue signatureValue = token.sign(dataToSign, digestAlgorithm, privateKey);

		// We invoke the xadesService to sign the document with the signature value obtained in
		// the previous step.
		DSSDocument signedDocument = service.signDocument(toSignDocument, parameters, signatureValue);

		signedDocument.save(target);
	}
}
