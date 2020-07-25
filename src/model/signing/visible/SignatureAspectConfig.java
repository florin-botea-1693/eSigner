package model.signing.visible;

import java.awt.Font;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDPage;

import model.certificates.Certificate;

/**
 * 
 * @author Botea Florin
 *
 * Va primi input direct de la client referitor la cum va arata semnatura ca si aspect.
 * Va fi analizat de SignatureAspectWrapper care va intoarce SignatureAspect.instance configurat.
 * SignatureAspect.instance va avea o metoda refresh() folosita pentru a se actualiza data.
 * 
 */

public class SignatureAspectConfig {
	
	private Certificate certificate;
	private boolean hasSignatureImage;
	private File signatureImage;
	private int page;
	private Font font;
	private String size;
	
	private String reason;
	private String location;
	private String name;
	private String SN;
	
	private boolean needsRepositioningInPage = true;
	private boolean needsRedraw = true;
	
	public static SignatureAspectConfig loadFromGlobalConfig(/* settings file */) {
		// build build build
		
		return null;
	}
	
	// setCertificate
	
	// getText()
}
