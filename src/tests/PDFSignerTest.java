package tests;

import java.io.File;
import java.io.IOException;

import model.AppSettings;
import model.PDFSignerModel;
import model.certificates.MSCAPICertificatesHolder;
import model.signing.PDFSigningOptions;
import model.signing.Signer;

public class PDFSignerTest {
	public static void main(String[] args) {
		
		MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
		//AppSettings settings = AppSettings.getInstance();
		PDFSigningOptions signingOptions = new PDFSigningOptions();
		//signingOptions.loadFromAppSettings(settings);
		
		PDFSignerModel cfg = new PDFSignerModel(certificatesHolder, signingOptions);
		cfg.setSigningCertificate(certificatesHolder.getSelectedCertificate());
		cfg.setVisibleSignature(true);
		cfg.setSize("small");
		
		File file = new File("test.pdf");
		try {
			Signer.sign(cfg, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}