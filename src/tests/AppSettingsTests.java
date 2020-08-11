package tests;

import java.io.IOException;

import model.AppSettings;
import model.AppSettings1;
import model.certificates.MSCAPICertificatesHolder;

public class AppSettingsTests {
	public static void main(String[] args) throws IOException {
		AppSettings1 appSettings = AppSettings1.getInstance();
		MSCAPICertificatesHolder certHolder = new MSCAPICertificatesHolder();
		//appSettings.setCertificate(certHolder.getSelectedCertificate());
		//appSettings.save();
		
		System.out.println(String.join("|", new String[] {"foo", "bar"}));
	}
}
