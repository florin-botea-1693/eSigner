package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class AppSettings {
	private static AppSettings instance = null;
	public final static String SEPARATOR = "|$|";
	
	private Properties props = new Properties();
	
	private String[] certificates;
	private String[] isVisibleSN;
	
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
		this.isVisibleSN = Arrays.copyOfRange(this.props.getProperty("isVisibleSN", "").split(SEPARATOR), 0, this.certificates.length);
		==//==
	}
	
	//============================||
	// MAIN METHODS --- GETTERS
	//============================||
	public int getPreferredCertificate() {
		return Integer.parseInt(this.props.getProperty("preferred"));
	}
	
	public void setCertificate() {
		
	}
	
	public static void main(String[] args) {
        String[] foo = new String[2];
        String[] bar = new String[] {"1","2","3","4"};
        String[] a = Arrays.copyOfRange(bar, 0, 0);
        String[] newArr = Arrays.copyOfRange(bar, 0, 22);
        for (int i=0; i<newArr.length; i++) {
            System.out.println(newArr[i]);
        }
	}
}
