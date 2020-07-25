package model.signing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.PDFSigningConfig;

public class Signer {

	/**
	 * Creeaza o instanta SigningMode.instance (PDFVisibleSigner) trecandu-i in constructor membrii lui PDFSigningConfig.class.
	 * Instanta creata este depinde de cum a fost configurat PDFSigningConfig.class.
	 * Avand acei membri deja configurati, SigningMode.instance nu are de facut decat sa aplice semnatura.
	 * Ca urmare, SigningMode.instance are doar o singura metoda -> sign(File file).
	 * 
	 * @param cfg - obiect configuratie folosit pentru a decide cum semnez
	 * @param file - fisierul sursa pentru semnare
	 */
	
	public static void sign(PDFSigningConfig cfg, File file) throws FileNotFoundException, IOException {
		// case case case
		if (true)
			new PDFVisibleSigner(cfg.getCertificatesHolder(), cfg.getPadesService(), cfg.getPadesParameters(), cfg.getVisibleSignatureWrapper()).sign(file);;
	}
}
