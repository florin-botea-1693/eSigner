package model.signing.visible;

import java.awt.Font;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import model.certificates.Certificate;
import text.FitTextInRectangle;

public class BasicSignatureAspect extends SignatureAspect {
	
	private FitTextInRectangle textFit = new FitTextInRectangle();
	private String formattedText = "No text";
	private SignatureImageTextParameters tp = new SignatureImageTextParameters();
	
	public BasicSignatureAspect() {
		super();
		_getSIP().setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.LEFT);
		_getSIP().setAlignmentVertical(VisualSignatureAlignmentVertical.MIDDLE);
		
		String unformattedText = "Digitally Signed by Botea Florin\nDate: 23.07.2020 +3000 gtm";
		Font font = new Font("arial", Font.PLAIN, 1);
		ArrayList<Object> content = textFit.fit(unformattedText, 100, 50, font);
		
		this.formattedText = (String) content.get(1);
		tp.setText((String) content.get(1));
		tp.setFont(new DSSJavaFont("arial"));
		tp.setSize((int) content.get(0));
		
		_getSIP().setTextParameters(tp);
	}
	
	public String getText() {
		String text = "";
		text += ("Digitally signed by " + cert.getName()) + "\n";
		text += isVisibleSerialNumber ? ("SN: " + cert.getSerialNumber() + "\n") : "";
		text += reason != null ? ("Reason: " + reason + "\n") : "";
		text += location != null ? ("Reason: " + location + "\n") : "";
		text += "Date: 16.06.1993";
		return text;
	}

	@Override
	protected void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void reDraw() {
		// TODO Auto-generated method stub
		// getText
	}
}
