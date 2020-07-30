package tests;

import java.io.File;
import java.io.IOException;

import model.PDFSignerModel;
import model.certificates.MSCAPICertificatesHolder;
import model.signing.PDFSigningOptions;

public class PDFSignerTest {
	public static void main(String[] args) {
		
		MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
		//AppSettings settings = AppSettings.getInstance();
		PDFSigningOptions signingOptions = new PDFSigningOptions();
		//signingOptions.loadFromAppSettings(settings);
		
		PDFSignerModel model = new PDFSignerModel(certificatesHolder, signingOptions);
		model.setSigningCertificate(certificatesHolder.getSelectedCertificate());
		model.setVisibleSignature(true);
		model.setSize("large");
		model.setReason("Voi face aceasta aplicatie super smechera care sa ajute oamenii la semnarea documentelor.", true);
		model.setLocation("la DigiSait", true);
		model.setReason(null, false);
		model.setLocation(null, false);
		model.setVisibleSN(false);
		
		File file = new File("test.pdf");
		try {
			model.sign(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}