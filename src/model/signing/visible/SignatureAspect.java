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
import model.certificates.AppCertificatesValidator;
import model.certificates.Certificate;
import model.signing.options.PdfSigningOptions;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;

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

	public SignatureAspect(Certificate cert, PdfSigningOptions s)
	{
		this.cert = cert;
		this.isVisibleSerialNumber = s.isVisibleSN();
		this.position = s.getPosition();
		this.size = s.getSize();
		this.reason = s.getReason();
		this.location = s.getLocation();
		this.organization = s.getLocation();
		this.customPosition = s.getCustomPosition();
		this.page = s.getCustomPage();
		System.out.println("custom page is " + this.page);
	}
	
	//===============================||
	// MAIN METHOD
	//===============================||
	/**
	 * final method that construct signature aspect or refresh it. Call this method only after you build the sip
	 */
	
	abstract public SignatureImageParameters generateSignatureImageParameters(PDDocument doc, int page_index);
	
	/*
	public SignatureImageParameters generateSignatureImageParameters() {
		if (needsRedraw) {
			System.out.println("Constructing signature aspect");
			try {
				AppCertificatesValidator v = AppCertificatesValidator.getInstance();
				if (! v.validate(cert).canSign)
					System.exit(1);
				//nu ai ce cauta aici
			} catch (Exception e) {}
			
			reDraw();
			needsRedraw = false;
		} else {
			System.out.println("Refreshing signature aspect");
			refresh();
		}
		// as putea aici sa incadrez in pagina, dupa ce initial am setat documentul. Plus ca am la dispozitie pctX si pctY si sip.align
		if (pdDocument == null) throw new Error("No document has been selected for signinature fit");
		
		PDPage p = pdDocument.getPage(this.getPage() -1);
		sip.setxAxis(this.getSignaturePosX(p));
		sip.setyAxis(p.getMediaBox().getHeight() - sip.getHeight() - this.getSignaturePosY(p));

		return sip;
	}
	
	//================================||
	// ALTE METODE MARUNTE 
	//================================||
	
	public void setPage(int i) {
		this.sip.setPage(i);
	}

	public void setReason(String string)
	{
		reason = string;
		this.needsRedraw = true;
	}

	public void setLocation(String string) 
	{
		location = string;
		this.needsRedraw = true;
	}

	public void setVisibleSerialNumber(boolean b)
	{
		isVisibleSerialNumber = b;
		this.needsRedraw = true;
	}
	
	public void setVisibleReason(boolean b) {
		this.isVisibleReason = b;
	}
	
	public void setVisibleLocation(boolean b) {
		this.isVisibleLocation = b;
	}

	public void setCertificate(Certificate cert) 
	{
		this.cert = cert;
		this.needsRedraw = true;
	}

	public void setSize(SignatureSize size)
	{
		this.signatureSize = size;
		this.sip.setWidth(size.getWidth());
		this.sip.setHeight(size.getHeight());
		this.needsRedraw = true;
	}
	
	public void setPosition(SignaturePosition position) 
	{
		this.signaturePosition = position;
		// this.sip.setAlignmentVertical(position.getVerticalAlignment());
		// this.sip.setAlignmentHorizontal(position.getHorizontalAlignment());
	}
	
	public void setPosition(int pctX, int pctY) {
		this.sip.setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.NONE);
		this.sip.setAlignmentVertical(VisualSignatureAlignmentVertical.NONE);
		this.pctX = pctX;
		this.pctY = pctY;
	}
	
	public void setOrganization(String o) {
		this.organization = o;
		this.needsRedraw = true;
	}
	
	public SignaturePosition getSignaturePosition() {
		return this.signaturePosition;
	}
	public SignatureSize getSize() {
		return this.signatureSize;
	}
	public boolean isVisibleReason() {
		return this.isVisibleReason;
	}
	public boolean isVisibleLocation() {
		return this.isVisibleLocation;
	}
	public boolean isVisibleSerialNumber() {
		return this.isVisibleSerialNumber;
	}
	public String getOrganization() {
		return this.organization;
	}
	*/
	/**
	 * Intoarce un rectangle xy wh cu incadrarea semnaturii in pagina specifica (wh, xy)
	 * @param page
	 */
	public Rectangle simulateInPage(PDPage page) { 
		return simulateInPage(page, false); 
	}
	public Rectangle simulateInPage(PDPage page, boolean revertedY)
	{
		float signatureWidth = size.getPctWidth() * page.getMediaBox().getWidth() / 100;
		float signatureHeight = size.getPctHeight() * signatureWidth / 100;
		float posX = getSignaturePosX(page, signatureWidth);
		float posY = getSignaturePosY(page, signatureWidth, revertedY);
		return new Rectangle((int)posX, (int)posY, (int)signatureWidth, (int)signatureHeight);
	}
	
	private float getSignaturePosX(PDPage pdPage, float signatureWidth)
	{
		VisualSignatureAlignmentHorizontal alignment = position.getHorizontalAlignment();
		float visibleRange = pdPage.getMediaBox().getWidth() - signatureWidth;
		
		if (alignment == VisualSignatureAlignmentHorizontal.LEFT)
			return 0;
		
		if (alignment == VisualSignatureAlignmentHorizontal.CENTER)
			return visibleRange/2;
		
		if (alignment == VisualSignatureAlignmentHorizontal.RIGHT)
			return visibleRange;
			
		float x = this.customPosition.x * visibleRange / 100;
		x = x >= visibleRange ? visibleRange - signatureWidth : x;
		
		return x;
	}
	
	private float getSignaturePosY(PDPage pdPage, float signatureHeight, boolean revertedY)
	{
		VisualSignatureAlignmentVertical alignment = position.getVerticalAlignment();
		float visibleRange = pdPage.getMediaBox().getHeight() - signatureHeight;

		if (alignment == VisualSignatureAlignmentVertical.TOP)
			return revertedY ? pdPage.getMediaBox().getHeight() - signatureHeight : 0;
					
		if (alignment == VisualSignatureAlignmentVertical.MIDDLE)
			return revertedY ? pdPage.getMediaBox().getHeight()/2 : visibleRange/2;
		
		if (alignment == VisualSignatureAlignmentVertical.BOTTOM)
			return revertedY ? signatureHeight : visibleRange;
		
		float y = this.customPosition.y * visibleRange / 100;
		y = y >= visibleRange ? visibleRange : y;
		y = revertedY ? pdPage.getMediaBox().getHeight() - y : y;
				
		return y;
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
