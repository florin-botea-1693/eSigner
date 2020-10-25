package model.signing.visible;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextVerticalAlignment;
import eu.europa.esig.dss.pdf.pdfbox.visible.defaultdrawer.ImageMerger;
import eu.europa.esig.dss.pdf.pdfbox.visible.defaultdrawer.ImageTextWriter;
import model.PdfSigningModel;
import model.certificates.AppCertificatesValidator;
import model.certificates.Certificate;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import text.FitTextInRectangle;
import text.FormattableString;

public class AdobeLikeSignatureAspect extends SignatureAspect 
{	
	
	// static cache for better performance
	private static Certificate _cert;
	private static SignaturePosition _position;
	private static SignatureSize _size;
	private static boolean _isVisibleSerialNumber;
	private static String _reason = "";
	private static String _location = "";
	private static String _organization = "";
	
	private static String formattedLeftText = null;
	private static String formattedRightText = null;
	private static int leftFontSize = 1;
	private static int rightFontSize = 1;
	
	private SignatureImageTextParameters tp = new SignatureImageTextParameters();
	private FormattableString stringFormatter;
	
	public AdobeLikeSignatureAspect(Certificate cert, PdfSigningModel model) 
	{
		super(cert, model);
		
		stringFormatter = new FormattableString();
		stringFormatter.setDelimiters(new String[] {" ", "-", ",", ".", "@"});
		stringFormatter.setFont(new Font("arial", Font.PLAIN, 12));
		
		tp.setBackgroundColor(new Color ( 0f, 0f, 0f, .1f ));
		tp.setFont(new DSSJavaFont("arial"));
	}
	
	@Override
	public SignatureImageParameters generateSignatureImageParameters(PDDocument doc, int page_index)
	{
		PDPage page = doc.getPage(page_index -1);
		SignatureImageParameters sip = new SignatureImageParameters();
		Rectangle r = this.simulateInPage(page);
		sip.setWidth((int) r.getWidth());
		sip.setHeight((int) r.getHeight());
		sip.setxAxis((float) r.getX());
		sip.setyAxis((float) r.getY());
		
		if (this.needsRedraw()) {
			System.out.println("Constructing signature aspect");
			try {
				AppCertificatesValidator v = AppCertificatesValidator.getInstance();
				if (! v.validate(cert).canSign)
					System.exit(1);
				//nu ai ce cauta aici
			} catch (Exception e) {}
			
			this.reDraw(sip);
		} else {
			System.out.println("Refreshing signature aspect");
			this.refresh();
		}
		
		tp.setFont(new DSSJavaFont("arial"));// font namme
		// build left img
		tp.setSize(leftFontSize);
		BufferedImage left = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp, formattedLeftText);
		// build right img
		tp.setSize(rightFontSize);
		BufferedImage right = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp, formattedRightText);
		BufferedImage img = ImageMerger.mergeOnRight(left, right, new Color ( 0f, 0f, 0f, .1f ), SignerTextVerticalAlignment.MIDDLE);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "gif", baos);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte[] bytes = baos.toByteArray();

		InMemoryDocument image = new InMemoryDocument(new ByteArrayInputStream(bytes));
		sip.setImage(image);
		sip.setPage(page_index);

		return sip;
	}

	private void reDraw(SignatureImageParameters sip)
	{
		String leftText = getLeftText();
		String rightText = getRightText();
		
		formattedLeftText = stringFormatter.fitInRatio(leftText, sip.getWidth()/2, sip.getHeight());
		formattedRightText = stringFormatter.fitInRatio(rightText, sip.getWidth()/2, sip.getHeight());
		leftFontSize = stringFormatter.getFontSizeToMatchHeight(formattedLeftText, sip.getHeight());
		rightFontSize = stringFormatter.getFontSizeToMatchHeight(formattedRightText, sip.getHeight());
		
		AdobeLikeSignatureAspect._cert = this.cert;
		AdobeLikeSignatureAspect._isVisibleSerialNumber = this.isVisibleSerialNumber;
		AdobeLikeSignatureAspect._location = this.location;
		AdobeLikeSignatureAspect._organization = this.organization;
		AdobeLikeSignatureAspect._reason = this.reason;
		AdobeLikeSignatureAspect._size = this.size;
	}
	
	protected void refresh()
	{
		int split = formattedRightText.indexOf("\nDate:"); // va fi ultima mereu
		String text = formattedRightText.substring(0, split);
		String date = formattedRightText.substring(split, formattedRightText.length());
		date = this.getDateTime(date.split("\n").length);
		formattedRightText = text + "\n" + date;
	}
	
	private boolean needsRedraw()
	{
		if (AdobeLikeSignatureAspect._cert != this.cert)
			return true;
		if (AdobeLikeSignatureAspect._position != this.position)
			return true;
		if (AdobeLikeSignatureAspect._size != this.size)
			return true;
		if (AdobeLikeSignatureAspect._isVisibleSerialNumber != this.isVisibleSerialNumber)
			return true;
		if (!AdobeLikeSignatureAspect._reason.equals(this.reason))
			return true;
		if (!AdobeLikeSignatureAspect._location.equals(this.location))
			return true;
		if (!AdobeLikeSignatureAspect._organization.equals(this.organization))
			return true;
		
		return false;
	}
	
	private String getLeftText() {
		return this.cert.getIssuedTo();
	}
	
	private String getRightText() {
		String text = "";
		text += ("Digitally signed by " + cert.getIssuedTo()) + "\n";
		text += this.organization != null && this.organization.trim().length() > 1 ? ("O: " + this.organization.trim() + "\n") : "";
		text += this.isVisibleSerialNumber ? ("SN: " + cert.getSerialNumber().trim() + "\n") : "";
		text += this.reason != null && this.reason.length() > 1 ? ("Reason: " + reason.trim() + "\n") : "";
		text += this.location != null && this.location.length() > 1 ? ("Location: " + location.trim() + "\n") : "";
		text += this.getDateTime(1);
		return text;
	}
}
