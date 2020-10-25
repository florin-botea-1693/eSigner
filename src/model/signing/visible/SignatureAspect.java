package model.signing.visible;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignaturePagePlacement;
import model.PdfSigningModel;
import model.certificates.AppCertificatesValidator;
import model.certificates.Certificate;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import utils.RectangleContainer;

/*
 * DOAR LA SEMNATURILE VIZIBILE
 */

public abstract class SignatureAspect 
{
	protected final Certificate cert;
	protected SignaturePosition position;
	protected SignatureSize size;
	protected boolean isVisibleSerialNumber;
	protected String reason = "";
	protected String location = "";
	protected String organization = "";
	protected Point customPosition = new Point(0, 0);
	private int page = 1;
	
	public Certificate getCert() {
		return cert;
	}

	public SignaturePosition getPosition() {
		return position;
	}

	public SignatureSize getSize() {
		return size;
	}

	public boolean isVisibleSerialNumber() {
		return isVisibleSerialNumber;
	}

	public String getReason() {
		return reason;
	}

	public String getLocation() {
		return location;
	}

	public String getOrganization() {
		return organization;
	}

	public Point getCustomPosition() {
		return customPosition;
	}
	
	public int getPage() {
		return this.page;
	}

	public SignatureAspect(Certificate cert, PdfSigningModel model)
	{
		this.cert = cert;
		this.isVisibleSerialNumber = model.isVisibleSN();
		this.position = model.getPosition();
		this.size = model.getSize();
		this.reason = model.getReason();
		this.location = model.getLocation();
		this.organization = model.getOrganization();
		this.customPosition = model.getCustomPosition();
		this.page = model.getCustomPage();
		System.out.println("custom page is " + this.page);
	}
	
	//===============================||
	// MAIN METHOD
	//===============================||
	/**
	 * final method that construct signature aspect or refresh it. Call this method only after you build the sip
	 */
	
	abstract public SignatureImageParameters generateSignatureImageParameters(PDDocument doc, int page_index);

	/**
	 * Intoarce un rectangle xy wh cu incadrarea semnaturii in pagina specifica (wh, xy)
	 * @param page
	 */
	public Rectangle simulateInPage(PDPage pdPage)
	{
		float signatureWidth = size.getPctWidth() * pdPage.getMediaBox().getWidth() / 100;
		float signatureHeight = size.getPctHeight() * signatureWidth / 100;
		
		RectangleContainer mediabox = new RectangleContainer(0, 0, pdPage.getMediaBox().getWidth(), pdPage.getMediaBox().getHeight());
		RectangleContainer cropbox = new RectangleContainer(
			pdPage.getCropBox().getLowerLeftX(), 
			pdPage.getCropBox().getLowerLeftY(),
			pdPage.getCropBox().getWidth(),
			pdPage.getCropBox().getHeight()
		);
		RectangleContainer signature = new RectangleContainer(0, 0, signatureWidth, signatureHeight);
		
		switch (position.getHorizontalAlignment()) {
		case LEFT:
			signature.setX(0);
			break;
		case CENTER:
			signature.setX((cropbox.getWidth()-signatureWidth)/2);
			break;
		case RIGHT:
			signature.setX(cropbox.getWidth()-signatureWidth);
			break;
		case NONE:
			signature.setX(customPosition.x * (cropbox.getWidth()-signatureWidth) / 100);
			break;
		}
		
		switch (position.getVerticalAlignment()) {
		case TOP:
			signature.setY(0);
			break;
		case MIDDLE:
			signature.setY(50 * (cropbox.getHeight()-signatureHeight) / 100);
			break;
		case BOTTOM:
			signature.setY(cropbox.getHeight()-signatureHeight);
			break;
		case NONE:
			signature.setY(customPosition.y * (cropbox.getHeight()-signatureHeight) / 100);
			break;
		}
		
		mediabox.addChild(cropbox);
		cropbox.addChild(signature);
		
		return new Rectangle((int)signature.getGlobalX(), (int)signature.getGlobalY(), (int)signatureWidth, (int)signatureHeight);
	}

	/**
	 * Helper method to get date time formatted with \n based on number of splits
	 * @param splits
	 * @return new formatted date time in specific split number
	 */
	public static String getDateTime(int splits) {
		Calendar localTime = Calendar.getInstance();
		OffsetDateTime utcTime = OffsetDateTime.now(ZoneOffset.UTC);
		Date now = localTime.getTime();
		int diff = localTime.get(Calendar.HOUR_OF_DAY) + utcTime.getHour();
		String sign = diff > 0 ? "+" : ""; // are deja minus
		
		String result = "";
		switch (splits) {
		case 1: // e suficient loc sa o pun pe un singur rand
			result = "Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(localTime.getTime()) + " UTC" + sign + String.valueOf(diff);
			break;
		case 2: // merge impartita in doua
		case 3:
			result = "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(now) + "\n" + new SimpleDateFormat("HH:mm").format(now) + " UTC" + sign + String.valueOf(diff);
			break;
		default:
			result = "Date: \n" + new SimpleDateFormat("yyyy-MM-dd").format(now) + "\n" + new SimpleDateFormat("HH:mm").format(now) + " UTC" + sign + String.valueOf(diff);
		}
		return result;
	}
}
