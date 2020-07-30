package model.signing.visible;

import java.awt.Font;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextHorizontalAlignment;
import model.certificates.Certificate;
import text.FitTextInRectangle;

public class BasicSignatureAspect extends SignatureAspect {
	
	private FitTextInRectangle textFit = new FitTextInRectangle();
	private SignatureImageTextParameters tp = new SignatureImageTextParameters();
	private String formattedText = "No text";
	
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
		
		tp.setPadding(4);
		_getSIP().setTextParameters(tp);
	}
	
	public String getText() {
		String text = "";
		text += ("Digitally signed by " + cert.getHolderNameName()) + "\n";
		text += isVisibleSerialNumber ? ("SN: " + cert.getSerialNumber() + "\n") : "";
		text += reason != null ? ("Reason: " + reason + "\n") : "";
		text += location != null ? ("Reason: " + location + "\n") : "";
		text += "Date: 16.06.1993";
		return text;
	}

	@Override
	protected void refresh() {
		int split = tp.getText().indexOf("\nDate:"); // va fi ultima mereu
		String text = tp.getText().substring(0, split);
		String date = tp.getText().substring(split, tp.getText().length());
		date = (date.split("\n").length > 1) ? ("Date: 16.06.1993\n09:30 +3000 UTC") : "Date: 16.06.1993 09:30 +3000 UTC";
		tp.setText(text + "\n" + date);
	}

	@Override
	protected void reDraw() {
		ArrayList<Object> font_and_text = textFit.fit(getText(), _getSIP().getWidth(), _getSIP().getHeight(), new Font("arial", Font.PLAIN, 12));
		DSSJavaFont font = new DSSJavaFont("arial", Font.PLAIN, (int) font_and_text.get(0));
		tp.setText((String) font_and_text.get(1));
		tp.setFont(font);
		_getSIP().setTextParameters(tp);
	}
}
