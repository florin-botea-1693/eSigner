package model.certificates;

import java.util.ArrayList;
import java.util.List;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.MSCAPISignatureToken;
import eu.europa.esig.dss.token.SignatureTokenConnection;

public class MSCAPICertificatesHolder implements CertificatesHolder {

	private SignatureTokenConnection token = new MSCAPISignatureToken();
	private ArrayList<Certificate> certificates = new ArrayList<Certificate>();
	private Certificate selectedCertificate = null;
	
	public MSCAPICertificatesHolder() {
        for (DSSPrivateKeyEntry entry : this.token.getKeys()) {
        	if (entry.getCertificate().getCertificate().getType() != "X.509") {
        		continue;
        	}
            boolean[] usage = entry.getCertificate().getCertificate().getKeyUsage();
            if (usage == null || usage[0] == false) {//usage0 == signing
            	continue;
            }
        	certificates.add(new Certificate(entry));
        }
	}
	
	@Override
	public SignatureTokenConnection getToken() {
		return this.token;
	}

	@Override
	public List<Certificate> getCertificates() {
        return certificates;
	}

	@Override
	public Certificate getSelectedCertificate() {
		if (getCertificates().size() == 0)
			return null;
		if (selectedCertificate == null) {
			selectedCertificate = getCertificates().get(0);
		}
		if (selectedCertificate == null)
			throw new NullPointerException("No certificate has been selected");
		return selectedCertificate;
	}

	@Override
	public void selectCertificate(Certificate cert) {
		selectedCertificate = cert;
	}

	@Override
	public void selectCertificate(String certSN) {
		certificates.forEach( certificate -> {
			if (certificate.getPrivateKey().getCertificate().getSerialNumber().toString().equals(certSN))
				selectedCertificate = certificate;
		});
		if (selectedCertificate == null && certificates.size() > 0) {
			selectedCertificate = certificates.get(0);
		}
	}

	@Override
	public Certificate findBySerialNumber(String certSN) {
		Certificate result = null;
		for (Certificate certificate : certificates) {
			if (certificate.getPrivateKey().getCertificate().getSerialNumber().toString().equals(certSN))
				return certificate;
		}
		if (certificates.size() > 0)
			return certificates.get(0);
		return null;
	}
}
