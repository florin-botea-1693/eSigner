package model.signing.visible;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextHorizontalAlignment;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextPosition;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters.SignerTextVerticalAlignment;
import eu.europa.esig.dss.pdf.pdfbox.visible.defaultdrawer.ImageMerger;
import eu.europa.esig.dss.pdf.pdfbox.visible.defaultdrawer.ImageTextWriter;
import model.certificates.Certificate;
import text.FitTextInRectangle;
import text.FormattableString;

public class AdobeLikeSignatureAspect extends SignatureAspect {
	
	private SignatureImageTextParameters tp = new SignatureImageTextParameters();
	private FormattableString formattableString;
	private String formattedLeftText = "No text";
	private String formattedRightText = "No text";
	private int leftFontSize = 1;
	private int rightFontSize = 1;
	
	public AdobeLikeSignatureAspect() {
		super();

		this._getSIP().setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.LEFT);
		this._getSIP().setAlignmentVertical(VisualSignatureAlignmentVertical.MIDDLE);
		
		formattableString = new FormattableString();
		formattableString.setDelimiters(new String[] {" ", "-", ",", ".", "@"});
		formattableString.setFont(new Font("arial", Font.PLAIN, 12));
		
		tp.setBackgroundColor(new Color ( 0f, 0f, 0f, .1f ));
		tp.setFont(new DSSJavaFont("arial"));
		
		InMemoryDocument IMDImage = null;
		
		String leftText = "Gaius Iulius Chaesar Maximus Augustus";
		String rightText = "Digitally signed by Botea Florin\nReason: Semnez acest document in calitate de administrator\nLocation: Bucuresti\nDate 23-03-2020 +3000 gtm";
		
		FitTextInRectangle ftr = new FitTextInRectangle();
		leftText = ftr.formatTextInRatio(leftText, new Font("arial", Font.PLAIN, 1), 60, 50);
		rightText = ftr.formatTextInRatio(rightText, new Font("arial", Font.PLAIN, 1), 60, 50);
		
		int leftFontSize = ftr.sizeInHeight(leftText, new Font("arial", Font.PLAIN, 1), 60);
		int rightFontSize = ftr.sizeInHeight(rightText, new Font("arial", Font.PLAIN, 1), 60);
		
		SignatureImageTextParameters tp = new SignatureImageTextParameters();
		tp.setFont(new DSSJavaFont("arial"));
		//tp.setTextColor(Color.RED);
		tp.setSize(24);
		tp.setBackgroundColor(new Color ( 0f, 0f, 0f, .1f ));
		tp.setSize(leftFontSize);
		BufferedImage left = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp, leftText);
		tp.setSize(rightFontSize);
		BufferedImage right = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp, rightText);
		BufferedImage img = ImageMerger.mergeOnRight(left, right, new Color ( 0f, 0f, 0f, .1f ), SignerTextVerticalAlignment.MIDDLE);
		try {
			ImageIO.write(img, "gif", new File("foo.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			IMDImage = new InMemoryDocument(new FileInputStream("foo.gif"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this._getSIP().setImage(IMDImage);
		
		// fa asta neaparat daca nu vrei sa ai o eroare
		this.setSize(SignatureSize.MEDIUM);
		this.setPosition(SignaturePosition.TOP_LEFT);
	}
	
	public String getText() {
		String text = "";
		text += ("Digitally signed by " + cert.getHolderNameName()) + "\n";
		text += isVisibleSerialNumber ? ("SN: " + cert.getSerialNumber() + "\n") : "";
		text += this.isVisibleReason ? ("Reason: " + reason + "\n") : "";
		text += this.isVisibleLocation ? ("Location: " + location + "\n") : "";
		text += this.getDateTime(1);
		return text;
	}

	@Override
	protected void refresh() {System.out.println(formattedRightText);
		int split = formattedRightText.indexOf("\nDate:"); // va fi ultima mereu
		String text = formattedRightText.substring(0, split);
		String date = formattedRightText.substring(split, formattedRightText.length());
		date = this.getDateTime(date.split("\n").length);
		formattedRightText = text + "\n" + date;
		
		
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
		this._getSIP().setImage(image);
	}

	@Override
	protected void reDraw() {
		String leftText = this.cert.getHolderNameName();
		String rightText = getText();
		
		formattedLeftText = formattableString.fitInRatio(leftText, this.getSize().getWidth()/2, this.getSize().getHeight());
		formattedRightText = formattableString.fitInRatio(rightText, this.getSize().getWidth()/2, this.getSize().getHeight());
		leftFontSize = formattableString.getFontSizeToMatchHeight(formattedLeftText, this.getSize().getHeight());
		rightFontSize = formattableString.getFontSizeToMatchHeight(formattedRightText, this.getSize().getHeight());
		System.out.println(formattedRightText);
		refresh();
	}
}
