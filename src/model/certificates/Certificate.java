package model.certificates;

import java.security.Principal;
import java.security.cert.CertificateParsingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;

public class Certificate {
	private DSSPrivateKeyEntry privateKey;
	
	// certificate's info
	String issuedTo = null;
	String issuedBy = null;
	String email = null;
	
	public Certificate(DSSPrivateKeyEntry pk) {
		this.privateKey = pk;
	}
	
	@Override
	public String toString() {
    	String userName = this.getIssuedTo();
    	String sn = this.privateKey.getCertificate().getCertificate().getSerialNumber().toString(16);
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	String expirationDate = dateFormat.format(this.privateKey.getCertificate().getCertificate().getNotAfter());
    	
		return userName + " " + getEmailAdress() + " " + expirationDate;
	}
	
	public DSSPrivateKeyEntry getPrivateKey() {
		return this.privateKey;
	}
	
	public Principal getPrincipal() {
		return privateKey.getCertificate().getCertificate().getSubjectDN();
	}
	
	public String getIssuedTo() {
		if (issuedTo != null)
			return issuedTo;
		issuedTo = "Certificate";
		String n = getPrivateKey().getCertificate().getSubjectX500Principal().getName();
		Pattern r = Pattern.compile("CN=([\\w- ]*)");
		Matcher m = r.matcher(n);
		if (m.find()) {
			issuedTo = m.group(1);
		}
		return issuedTo;
	}
	
	public String getEmailAdress() {
		if (email != null)
			return email;
		email = "";
		try {
			Collection<List<?>> an = getPrivateKey().getCertificate().getCertificate().getSubjectAlternativeNames();
			if (an != null) {
				Iterator<List<?>> foo = an.iterator();
				foo.forEachRemaining( x -> {
					x.forEach(el -> {
						if (el.toString().contains("@") && el instanceof String) {
							email = el.toString();
						}
					});
				});
			}
		} catch (CertificateParsingException e) {
			e.printStackTrace();
		}
		return email;
	}
	
	public String getIssuedBy() {
		if (issuedBy != null)
			return issuedBy;
		issuedTo = "User";
		String n = getPrivateKey().getCertificate().getIssuerX500Principal().getName();
		Pattern r = Pattern.compile("CN=([\\w- ]*)");
		Matcher m = r.matcher(n);
		if (m.find()) {
			issuedBy = m.group(1);
		}
		return issuedBy;
	}

	public String getSerialNumber() {
		String sn = privateKey.getCertificate().getCertificate().getSerialNumber().toString(16).toUpperCase();
		String formattedSN = "";
		for (int i=0; i<sn.length(); i++) {
			formattedSN += (i%2 == 0 && i > 0 ? " " : "") + sn.charAt(i);
		}
		return formattedSN;
	}
}
