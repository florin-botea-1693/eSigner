package model.signing.visible;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDPage;

import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignaturePagePlacement;
import model.certificates.Certificate;

/*
 * DOAR LA SEMNATURILE VIZIBILE
 */

public abstract class SignatureAspect {
	private boolean needsRedraw = true;
	
	protected Certificate cert;
	private SignatureImageParameters sip = new SignatureImageParameters();
	private SignaturePosition signaturePosition;
	private SignatureSize signatureSize;
	protected boolean isVisibleSerialNumber = false;
	protected boolean isVisibleReason = false;
	protected boolean isVisibleLocation = false;
	protected String reason = null;
	protected String location = null;
	
	abstract protected void refresh(); // refresh date
	abstract protected void reDraw();
	
	public int getPage() {return this.sip.getPage();}
	
	protected void setNeedsRedraw(boolean b) {
		needsRedraw = b;
	}
	
	//===============================||
	// MAIN METHOD
	//===============================||
	public SignatureImageParameters getSIP() {
		if (needsRedraw) {
			System.out.println("Constructing signature aspect");
			reDraw();
			needsRedraw = false;
		} else {
			System.out.println("Refreshing signature aspect");
			refresh();
		}
		return sip;
	}
	
	protected SignatureImageParameters _getSIP() {
		return sip;
	}
	
	
	//================================||
	// ALTE METODE MARUNTE 
	//================================||
	public void setPage(int i) {
		this.sip.setPage(i);
	}

	public void setReason(String string) {
		reason = string;
		this.needsRedraw = true;
	}

	public void setLocation(String string) {
		location = string;
		this.needsRedraw = true;
	}

	public void setVisibleSerialNumber(boolean b) {System.out.println("isVisibleSerialNumber="+String.valueOf(b));
		isVisibleSerialNumber = b;
		this.needsRedraw = true;
	}
	
	public void setVisibleReason(boolean b) {
		this.isVisibleReason = b;
	}
	
	public void setVisibleLocation(boolean b) {
		this.isVisibleLocation = b;
	}

	public void setCertificate(Certificate cert) {
		this.cert = cert;
		this.needsRedraw = true;
	}

	public void setSize(SignatureSize size) {
		this.signatureSize = size;
		this.sip.setWidth(size.getWidth());
		this.sip.setHeight(size.getHeight());
		this.needsRedraw = true;
	}
	
	public void setPosition(SignaturePosition position) {
		this.signaturePosition = position;
		this.sip.setAlignmentVertical(position.getVerticalAlignment());
		this.sip.setAlignmentHorizontal(position.getHorizontalAlignment());
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
	
	public String getDateTime(int splits) {
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
