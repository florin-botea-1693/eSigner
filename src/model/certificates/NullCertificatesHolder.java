package model.certificates;

import java.util.List;

import eu.europa.esig.dss.token.SignatureTokenConnection;

public class NullCertificatesHolder implements CertificatesHolder 
{

	@Override
	public SignatureTokenConnection getToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Certificate> getCertificates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Certificate getSelectedCertificate() {
		return null;
	}

	@Override
	public void selectCertificate(Certificate cert) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectCertificate(String certSN) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Certificate findBySerialNumber(String certSN) {
		// TODO Auto-generated method stub
		return null;
	}

}
