package model.signing.visible;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;

import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextHorizontalAlignment;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextPosition;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextVerticalAlignment;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import model.certificates.Certificate;
import text.FitTextInRectangle;

public class SignatureAspectWrapper {
	
	private SignatureImageParameters sip;
	//private SignatureImageTextParameters text;
	private SignatureImage signatureImage;
	//private SignatureImage image;
	private PDPage pdPage;
	private Certificate certificate;
	
	private HashMap<String, String> fields = new HashMap<String, String>();
	private String lastFittingTextFormat; //
	private String lastFittingDateFormat; //
	private Font font; //
	
	private boolean hasSignatureImage = false;
	private boolean needsRedraw = true;
	private boolean needsRepositioningInPage = true;
	
	public SignatureAspectWrapper() {
		this.sip = new SignatureImageParameters();
		this.signatureImage = new BasicSignatureImage();
		
		/*
		this.text = new SignatureImageTextParameters();
		
		
		// nu voi mai avea pre-initializari....
		//this.image = SignatureImage.fromText("No certificate selected", new SignatureImageParameters(), text);
		
		this.text.setSignerTextPosition(SignerTextPosition.RIGHT); // default
		this.text.setSignerTextHorizontalAlignment(SignerTextHorizontalAlignment.LEFT); // default
		this.text.setSignerTextVerticalAlignment(SignerTextVerticalAlignment.MIDDLE); // default

		this.setFont("arial");
		this.setSize("small");
		this.setPosition(50, 50);
		
		this.text.setPadding(2); // default
		this.text.setTextColor(Color.BLACK); // default
		this.text.setText("basic text");
		
		this.sip.setTextParameters(this.text);*/
	}
	
	public void setFont(String font) {
		DSSJavaFont _font = new DSSJavaFont(font);
		//this.text.setFont(_font);
		needsRedraw = true;
	}
	
	public void setSize(String size) {
		switch (size) {
			case "small":
				this.sip.setWidth(120);
				this.sip.setHeight(50);
			break;
			case "medium":
				this.sip.setWidth(150);
				this.sip.setHeight(70);
			break;
			case "large":
				this.sip.setWidth(240);
				this.sip.setHeight(100);
			break;
		}
		this.needsRedraw = true;
	}

	public void setPosition(int x, int y) {
		sip.setxAxis(x);
		sip.setyAxis(y);
	}
	
	public void setCertificate(Certificate cert) {
		certificate = cert;
		needsRedraw = true;
	}
	
	public void setReason(String reason, boolean visible) {
		if (reason.length() > 0 && visible) {
			this.fields.put("reason", reason);
		} else {
			this.fields.remove("reason");
		}
		needsRedraw = true;
	}
	
	public void setLocation(String location, boolean visible) {
		if (location.length() > 0 && visible) {
			this.fields.put("location", location);
		} else {
			this.fields.remove("location");
		}
		needsRedraw = true;
	}
	
	public void setPage(int page, PDPage pdPage) {
		sip.setPage(page);
		pdPage = pdPage;
		needsRepositioningInPage = true;
	}
	
	private String getText() {
		return "Digitally Signed by Botea Alexandru Mircea Ionut";//+//"Reason: semnez acest document ca asa vreau eu\n"+
			//"SN:b6sadu8dsa7sda87sda8sda78das\n"+"Location: la mine acasa\n"+"Contact:florinbotea1693@gmail.com";
	}
	
	public void setSignatureImage() {
		this.hasSignatureImage = true;
		//this.image = SignatureImage.fromText("Imaginea selectata", new SignatureImageParameters(), text);
	}
	
	public int getPage() {
		return 1;
	}
	
	private void fitInPage() {
		int adjustedX = this.getAdjustedPos(sip.getxAxis(), sip.getWidth(), pdPage.getMediaBox().getWidth());
		int adjustedY = this.getAdjustedPos(sip.getyAxis(), sip.getHeight(), pdPage.getMediaBox().getHeight());
		this.sip.setxAxis(adjustedX);
		this.sip.setyAxis(adjustedY);
	}
	
	/*
	 * @param cfg 
	 */
	
	private SignatureAspect getSignatureAspect() {
		// logica de aici intoarce din membru daca e setat deja
		// daca cfg.needsRedraw, nici nu ma mai gandesc si intorc direct una noua
		return new BasicSignatureAspect();
	}

	/*
	 * de fiecare data cand vreau sa semnez accesez sip folosind metoda de mai jos 
	 * pentru a observa daca s-a intamplat vreo modificare in grafica, inclusiv data si ora
	 */
	
	public SignatureImageParameters getSignatureImageParameters() {
		// SignatureImageConfig cfg va locui in pdfSigningConfig pentru a fi actualizat mai usor. 
		
		SignatureAspect aspect = getSignatureAspect();
		aspect.refresh();
		
		return (SignatureImageParameters) aspect; // cfg
		
		

		/*
		String sip_name = textFit.formatTextInRatio("Botea Alexandru Mircea Ionut cel Mare si Frumos", new Font("arial", Font.BOLD, 12), (sip.getWidth()/2), sip.getHeight());;
		DSSJavaFont sip_name_font = new DSSJavaFont("arial", Font.BOLD, 12);
		signatureImage.addImageFromText(sip_name, sip_name_font, 0);
		
		String tmp_sip_text = this.getText() + "\n" + "Date: 7-12-2012 16:06 +3000";
		String sip_text = textFit.formatTextInRatio(tmp_sip_text, new Font("arial", Font.PLAIN, 12), (sip.getWidth()/2), sip.getHeight());
		DSSJavaFont sip_text_font = new DSSJavaFont("arial", Font.PLAIN, 12);
		signatureImage.addImageFromText(sip_text, sip_text_font, 1);
		
		sip.setTextParameters(null);
		sip.setImage(signatureImage.getFinalImage(sip.getWidth(), sip.getHeight(), SignatureImageRenderMode.LTR));
		//sip.setImage(bsi.getFinalImage());
		sip.setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.LEFT);
		sip.setAlignmentVertical(VisualSignatureAlignmentVertical.MIDDLE);
		return sip;
		*/
	}
	
	private int getAdjustedPos(float initialPos, float signatureDim, float pageDim) {
		//if ((initialPos-signatureDim/2) < 0) initialPos = Math.round(signatureDim/2);
		if ((initialPos+signatureDim/2) > pageDim) initialPos = Math.round(pageDim-signatureDim/2);
		return Math.round(initialPos);
	}
}
