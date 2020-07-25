package model.signing.visible;

import java.awt.Font;
import java.util.ArrayList;

import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import text.FitTextInRectangle;

public class BasicSignatureAspect extends SignatureImageParameters implements SignatureAspect {
	
	private FitTextInRectangle textFit = new FitTextInRectangle();
	private String formattedText = "No text";
	private SignatureImageTextParameters tp = new SignatureImageTextParameters();
	
	public BasicSignatureAspect() { // cfg
		super();
		setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.LEFT);
		setAlignmentVertical(VisualSignatureAlignmentVertical.MIDDLE);
		
		String unformattedText = "Digitally Signed by Botea Florin\nDate: 23.07.2020 +3000 gtm";
		Font font = new Font("arial", Font.PLAIN, 1);
		ArrayList<Object> content = textFit.fit(unformattedText, 100, 50, font);
		
		this.formattedText = (String) content.get(1);
		tp.setText((String) content.get(1));
		tp.setFont(new DSSJavaFont("arial"));
		tp.setSize((int) content.get(0));
		
		this.setTextParameters(tp);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	
	
}
