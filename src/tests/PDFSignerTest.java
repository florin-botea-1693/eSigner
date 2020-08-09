package tests;

import java.io.File;
import java.io.IOException;

import model.AppSettings;
import model.PDFSignerModel;
import model.certificates.MSCAPICertificatesHolder;
import model.signing.PDFSigningOptions;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;

public class PDFSignerTest {
	public static void main(String[] args) {
		
		MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
		//AppSettings settings = AppSettings.getInstance();
		AppSettings signingOptions = null;
		try {
			signingOptions = AppSettings.getInstance();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//signingOptions.loadFromAppSettings(settings);
		
		PDFSignerModel model = new PDFSignerModel(certificatesHolder, signingOptions);
		model.setSigningCertificate(certificatesHolder.getSelectedCertificate());
		model.setVisibleSignature(true);
		model.setVisibleSN(true);
		model.setSignaturePosition(SignaturePosition.TOP_RIGHT);
		model.setSigningReason("Semnez acest document de test");
		model.setVisibleReason(true);
		model.setSignatureSize(SignatureSize.LARGE);
		
		File file = new File("test.pdf");
		try {
			model.sign(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}