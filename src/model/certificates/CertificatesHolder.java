package model.certificates;

import java.util.List;

import eu.europa.esig.dss.token.SignatureTokenConnection;

public interface CertificatesHolder {
    public SignatureTokenConnection getToken();
    public List<Certificate> getCertificates();
    public Certificate getSelectedCertificate();
    public void selectCertificate(Certificate cert);
	public void selectCertificate(String certSN);
	public Certificate findBySerialNumber(String certSN);
}
