package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;

import model.certificates.Certificate;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import model.signing.visible.SigningPage;

public class AppSettings {
	private static AppSettings instance = null;
	
	public final static String SEPARATOR = "|$|";
	private Properties props = new Properties();
	
	private int selectedIndex = -1;
	private String[] certificates;
	private String[] isVisibleSN;
	private String[] signingReason;
	private String[] isVisibleReason;
	private String[] signingLocation;
	private String[] isVisibleLocation;
	private String[] isVisibleSignature;
	private String[] signingPage;
	private String[] isRealSignature;
	private String[] customSigningPage;
	private String[] signatureSize;
	private String[] signaturePosition;
	private String[] validationToken;
	
	//=============================||
	// SINGLETHON
	//=============================||
	private AppSettings() throws IOException {
		File propFile = new File("appSettings.properties");
		if (!propFile.exists()) {
			propFile = File.createTempFile("appSettings", ".properties");
		}
		this.props.load(new FileInputStream(propFile));
		this.init();
		//props.store(new FileWriter("appSettings.properties"), "store to properties file");
	}
	
	public static AppSettings getInstance() throws IOException {
		if (AppSettings.instance == null) {
			AppSettings.instance = new AppSettings();
		}
		return AppSettings.instance;
	}
	//========================\\
	
	private void init() {
		certificates = this.props.getProperty("certificates", "").split(SEPARATOR);
		isVisibleSN = this.getPropAsArray("isVisibleSN", this.certificates.length);
		signingReason = this.getPropAsArray("signingReason", this.certificates.length);
		isVisibleReason = this.getPropAsArray("isVisibleReason", this.certificates.length);
		signingLocation = this.getPropAsArray("signingLocation", this.certificates.length);
		isVisibleLocation = this.getPropAsArray("isVisibleLocation", this.certificates.length);
		isVisibleSignature = this.getPropAsArray("isVisibleSignature", this.certificates.length);
		signingPage = this.getPropAsArray("signingPage", this.certificates.length);
		isRealSignature = this.getPropAsArray("isRealSignature", this.certificates.length);
		customSigningPage = this.getPropAsArray("customSigningPage", this.certificates.length);
		signatureSize = this.getPropAsArray("signatureSize", this.certificates.length);
		signaturePosition = this.getPropAsArray("signaturePosition", this.certificates.length);
		validationToken = this.getPropAsArray("validationToken", this.certificates.length);
	}
	
	private String[] getPropAsArray(String p, int range) {
		return Arrays.copyOfRange(this.props.getProperty(p, "").split(SEPARATOR), 0, range);
	}
	
	private boolean getPropAsBoolean(String[] prop, int index, boolean def) {
		try {
			return BooleanUtils.toBoolean(Integer.parseInt( prop[index] ));
		} catch (Exception e) {
			return def;
		}
	}
	
	private String getPropAsString(String[] prop, int index, String def) {
		try {
			return prop[index];
		} catch (Exception e) {
			return def;
		}
	}
	
	public AppSettings setCertificate(Certificate cert) {
		String certSN = cert.getPrivateKey().getCertificate().getSerialNumber().toString();
		selectedIndex = ArrayUtils.indexOf(this.certificates, certSN);
		for (String c : certificates) {
			System.out.println(c);
		}
		
		if (selectedIndex < 0) {
			// daca nu se gaseste, creeaza un camp nou la fiecare array in parte
			selectedIndex = certificates.length; // +1
			certificates = Arrays.copyOfRange(certificates, 0, certificates.length+1);
			isVisibleSN = Arrays.copyOfRange(isVisibleSN, 0, certificates.length);
			signingReason = Arrays.copyOfRange(signingReason, 0, certificates.length);
			isVisibleReason = Arrays.copyOfRange(isVisibleReason, 0, certificates.length);
			signingLocation = Arrays.copyOfRange(signingLocation, 0, certificates.length);
			isVisibleLocation = Arrays.copyOfRange(isVisibleLocation, 0, certificates.length);
			isVisibleSignature = Arrays.copyOfRange(isVisibleSignature, 0, certificates.length);
			signingPage = Arrays.copyOfRange(signingPage, 0, certificates.length);
			isRealSignature = Arrays.copyOfRange(isRealSignature, 0, certificates.length);
			customSigningPage = Arrays.copyOfRange(customSigningPage, 0, certificates.length);
			signatureSize = Arrays.copyOfRange(signatureSize, 0, certificates.length);
			signaturePosition = Arrays.copyOfRange(signaturePosition, 0, certificates.length);
			validationToken = Arrays.copyOfRange(validationToken, 0, certificates.length);
		}
		return this;
	}
	
	//============================||
	// MAIN METHODS --- GETTERS
	//============================||
	public String getPreferredCertificate() {
		return this.props.getProperty("preferred");
	}
	
	public boolean isVisibleSerialNumber() {
		return this.getPropAsBoolean(this.isVisibleSN, this.selectedIndex, false);
	}
	
	public String getSigningReason() {
		return this.getPropAsString(this.signingReason, this.selectedIndex, "");
	}
	
	public boolean isVisibleReason() {
		return this.getPropAsBoolean(this.isVisibleReason, this.selectedIndex, false);
	}
	
	public String getSigningLocation() {
		return this.getPropAsString(this.signingLocation, this.selectedIndex, "");
	}
	
	public boolean isVisibleLocation() {
		return this.getPropAsBoolean(this.isVisibleLocation, this.selectedIndex, false);
	}
	
	public boolean isVisibleSignature() {
		return this.getPropAsBoolean(this.isVisibleSignature, this.selectedIndex, true);
	}
	
	public SigningPage getSigningPage() {
		try {
			return SigningPage.valueOf(this.signingPage[this.selectedIndex]);
		} catch (Exception e) {
			return SigningPage.FIRST_PAGE;
		}
	}
	
	public boolean isRealSignature() {
		return this.getPropAsBoolean(this.isRealSignature, this.selectedIndex, true);
	}
	
	public int getCustomSigningPage() {
		try {
			return Integer.parseInt(this.customSigningPage[this.selectedIndex]);
		} catch (Exception e) {
			return 1;
		}
	}
	
	public SignatureSize getSignatureSize() {
		try {
			return SignatureSize.valueOf(this.signatureSize[this.selectedIndex]);
		} catch (Exception e) {
			return SignatureSize.MEDIUM;
		}
	}
	
	public SignaturePosition getSignaturePosition() {
		try {
			return SignaturePosition.valueOf(this.signaturePosition[this.selectedIndex]);
		} catch (Exception e) {
			return SignaturePosition.TOP_LEFT;
		}
	}
	
	public String getToken() {
		System.out.println(selectedIndex);
		return validationToken[selectedIndex];
	}
	
	
	//====================================||
	// SETTERS
	//====================================||
	public void setValidationToken(String token) {
		validationToken[selectedIndex] = token;
	}
	
	
	
	public void save() throws FileNotFoundException, IOException {
		props.setProperty("certificates", String.join(SEPARATOR, certificates)); 
		props.setProperty("isVisibleSN", String.join(SEPARATOR, isVisibleSN));
		props.setProperty("signingReason", String.join(SEPARATOR, signingReason));
		props.setProperty("isVisibleReason", String.join(SEPARATOR, isVisibleReason));
		props.setProperty("signingLocation", String.join(SEPARATOR, signingLocation));
		props.setProperty("isVisibleLocation", String.join(SEPARATOR, isVisibleLocation));
		props.setProperty("isVisibleSignature", String.join(SEPARATOR, isVisibleSignature));
		props.setProperty("signingPage", String.join(SEPARATOR, signingPage));
		props.setProperty("isRealSignature", String.join(SEPARATOR, isRealSignature));
		props.setProperty("customSigningPage", String.join(SEPARATOR, customSigningPage));
		props.setProperty("signatureSize", String.join(SEPARATOR, signatureSize)); 
		props.setProperty("signaturePosition", String.join(SEPARATOR, signaturePosition)); 
		props.setProperty("validationToken", String.join(SEPARATOR, validationToken)); 
		props.setProperty("certificates", String.join(SEPARATOR, certificates));
		props.store(new FileOutputStream("appSettings.properties"), null);
	}
	
	
	/*
	this.signaturePosition = this.getPropAsArray("signaturePosition", this.certificates.length);
	*/
	//===============================\\
}
