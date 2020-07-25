package model.certificates;

import java.util.ArrayList;
import java.util.List;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.MSCAPISignatureToken;
import eu.europa.esig.dss.token.SignatureTokenConnection;

public class MSCAPICertificatesHolder implements CertificatesHolder {

	private SignatureTokenConnection token = new MSCAPISignatureToken();
	private Certificate selectedCertificate = null;
	
	@Override
	public SignatureTokenConnection getToken() {
		return this.token;
	}

	@Override
	public List<Certificate> getCertificates() {
		ArrayList<Certificate> certificates = new ArrayList<Certificate>();
        for (DSSPrivateKeyEntry entry : this.token.getKeys()) {
        	if (entry.getCertificate().getCertificate().getType() != "X.509") {
        		//continue;
        	}
            boolean[] usage = entry.getCertificate().getCertificate().getKeyUsage();
            if (usage == null || usage[0] == false) {//usage0 == signing
            	//continue;
            }
        	certificates.add(new Certificate(entry));
        }
        return certificates;
	}

	@Override
	public Certificate getSelectedCertificate() {
		if (getCertificates().get(0) == null) throw new NullPointerException("No certificate in keystore");
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
}
