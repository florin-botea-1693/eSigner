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
	private SignaturePosition signaturePosition = SignaturePosition.TOP_LEFT; // default pentru a nu cauza erori
	private SignatureSize signatureSize = SignatureSize.MEDIUM;
	private SignatureImageParameters sip = new SignatureImageParameters();
	
	protected int page = 1;
	protected PDPage pdPage = null;
	protected boolean isVisibleSerialNumber = false;
	protected String reason = null;
	protected String location = null;
	protected Certificate cert = null; 
	
	abstract protected void refresh(); // refresh date
	abstract protected void reDraw();
	
	public int getPage() {return this.page;}
	
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

	public void setPage(int i, PDPage page) {
		this.page = i;
		pdPage = page;
	}

	public void setPage(int i) {
		page = i;
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

	public void setCertificate(Certificate cert) {
		this.cert = cert;
		this.needsRedraw = true;
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
	
	public void setPosition(SignaturePosition position) {
		this.signaturePosition = position;
		// switch aici
		this.sip.setAlignmentHorizontal(VisualSignatureAlignmentHorizontal.CENTER);
		this.sip.setAlignmentVertical(VisualSignatureAlignmentVertical.MIDDLE);
	}
	public SignaturePosition getSignaturePosition() {
		return this.signaturePosition;
	}
	public SignatureSize getSize() {
		return this.signatureSize;
	}
	
	
}
