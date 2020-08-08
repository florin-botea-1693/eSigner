package model.signing.visible;

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
			reDraw();
		} else {
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

	public void setVisibleSerialNumber(boolean b) {
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
		switch (size) {
			case SMALL:
				this.sip.setWidth(120);
				this.sip.setHeight(50);
			break;
			case MEDIUM:
				this.sip.setWidth(150);
				this.sip.setHeight(70);
			break;
			case LARGE:
				this.sip.setWidth(240);
				this.sip.setHeight(100);
			break;
		}
		this.needsRedraw = true;
	}
	
	public void setPosition(SignaturePosition position) {
		this.signaturePosition = position;
		switch (position) {
			case TOP_LEFT:
				this.sip.setAlignmentVertical(VisualSignatureAlignmentVertical.TOP);
				this.sip.setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.LEFT);
			break;
			case TOP_CENTER:
				this.sip.setAlignmentVertical(VisualSignatureAlignmentVertical.TOP);
				this.sip.setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.CENTER);
			break;
			case TOP_RIGHT:
				this.sip.setAlignmentVertical(VisualSignatureAlignmentVertical.TOP);
				this.sip.setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.RIGHT);
			break;
		}
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
	
}
