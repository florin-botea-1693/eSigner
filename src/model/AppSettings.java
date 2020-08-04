package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;

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
		this.certificates = this.props.getProperty("certificates", "").split(SEPARATOR);
		this.isVisibleSN = this.getPropAsArray("isVisibleSN", this.certificates.length);
		this.signingReason = this.getPropAsArray("signingReason", this.certificates.length);
		this.isVisibleReason = this.getPropAsArray("isVisibleReason", this.certificates.length);
		this.signingLocation = this.getPropAsArray("signingLocation", this.certificates.length);
		this.isVisibleLocation = this.getPropAsArray("isVisibleLocation", this.certificates.length);
		this.isVisibleSignature = this.getPropAsArray("isVisibleSignature", this.certificates.length);
		this.signingPage = this.getPropAsArray("signingPage", this.certificates.length);
		this.isRealSignature = this.getPropAsArray("isRealSignature", this.certificates.length);
		this.customSigningPage = this.getPropAsArray("customSigningPage", this.certificates.length);
		this.signatureSize = this.getPropAsArray("signatureSize", this.certificates.length);
		this.signaturePosition = this.getPropAsArray("signaturePosition", this.certificates.length);
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
	
	public AppSettings setCertificate(String certSN) {
		this.selectedIndex = ArrayUtils.indexOf(this.certificates, certSN);
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
	
	/*
	this.signaturePosition = this.getPropAsArray("signaturePosition", this.certificates.length);
	*/
	//===============================\\
	
	public static void main(String[] args) {
        String[] foo = new String[2];
        String[] bar = new String[] {"1","2","3","4"};
        String[] a = Arrays.copyOfRange(bar, 0, 0);
        String[] newArr = Arrays.copyOfRange(bar, 0, 22);
        
        System.out.println( ArrayUtils.indexOf(bar, "s") );
        System.out.println( BooleanUtils.toBoolean(-1) );
        int x = 1;
        try {
        	x = Integer.parseInt("a");
        } catch(Exception e) {x = 0;}
        System.out.println(x);
        System.out.println(SigningPage.valueOf("foo"));
	}
}
