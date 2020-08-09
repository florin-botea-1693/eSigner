package model.certificates;

import java.security.Principal;
import java.text.SimpleDateFormat;

import eu.europa.esig.dss.token.DSSPrivateKeyEntry;

public class Certificate {
	private DSSPrivateKeyEntry privateKey;
	// on server
	private boolean isValidatedOnMyServer = false;
	private boolean canUseMyApp = false;
	private String validationOnMyServerResultMessage = "";
	
	public boolean isValidatedOnMyServer() {return isValidatedOnMyServer;}
	public boolean canUseMyApp() {return canUseMyApp;}
	public String getValidationOnMyServerResultMessage() {return validationOnMyServerResultMessage;}
	public void setValidatedOnMyServer(boolean b) {isValidatedOnMyServer = b;}
	public void setCanUseMyApp(boolean b) {canUseMyApp = b;}
	public void setValidationOnMyServerResultMessage(String s) {validationOnMyServerResultMessage = s;}
	
	public Certificate(DSSPrivateKeyEntry pk) {
		this.privateKey = pk;
	}
	
	@Override
	public String toString() {
    	String userName = this.getHolderNameName();
    	String sn = this.privateKey.getCertificate().getCertificate().getSerialNumber().toString(16);
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	String expirationDate = dateFormat.format(this.privateKey.getCertificate().getCertificate().getNotAfter());
    	
		return userName + expirationDate;
	}
	
	public DSSPrivateKeyEntry getPrivateKey() {
		return this.privateKey;
	}
	
	public Principal getPrincipal() {
		return privateKey.getCertificate().getCertificate().getSubjectDN();
	}
	
	public String getHolderNameName() {
		Principal principal = getPrincipal();
		int start = principal.getName().indexOf("CN");
		String tmpName, name = "";
		if (start > 0) { 
			tmpName = principal.getName().substring(start+3);
			int end = tmpName.indexOf(",");
			if (end > 0) {
				name = tmpName.substring(0, end);
			}
			else {
				name = tmpName; 
			}
		}
		return name;
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
