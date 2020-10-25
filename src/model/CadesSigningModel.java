package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

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
import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.certificates.MSCAPICertificatesHolder;
import utils.PropertyChangeSupportExtended;

public class CadesSigningModel extends SigningModel
{

	private String signingExtension;
	private DigestAlgorithm digestAlgorithm;
	private SignaturePackaging signaturePackaging;
	
	public String getSigningExtension() {
		return this.signingExtension;
	}
	
	public DigestAlgorithm getDigestAlgorithm() {
		return this.digestAlgorithm;
	}
	
	public SignaturePackaging getSignaturePackaging() {
		return this.signaturePackaging;
	}

	public CadesSigningModel(CertificatesHolder certificatesHolder) {
		super(certificatesHolder);
		// config from settings
		signingExtension = "p7m";
		digestAlgorithm = DigestAlgorithm.SHA256;
		signaturePackaging = SignaturePackaging.ENVELOPING;
	}

	public void setSigningExtension(String selectedItem) {
		this.signingExtension = selectedItem;
	}

	public void setDigestAlgorithm(DigestAlgorithm valueOf) {
		this.digestAlgorithm = valueOf;
	}

	public void setSignaturePackaging(SignaturePackaging valueOf) {
		this.signaturePackaging = valueOf;
	}
}
