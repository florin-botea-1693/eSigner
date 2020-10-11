package model.signing.options;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import model.certificates.Certificate;
import model.certificates.CertificatesHolder;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import utils.PropertyChangeSupportExtended;

public class PdfSigningOptions 
{
	private PropertyChangeSupportExtended observed = new PropertyChangeSupportExtended(this);
	
	private boolean isVisibleSignature = false;
	private boolean isVisibleSN = false;
	private String organization = "";
	private String reason = "";
	private boolean isVisibleReason = false;
	private String location = "";
	private boolean isVisibleLocation = false;
	private SignatureSize size = SignatureSize.MEDIUM;
	private SignaturePosition position = SignaturePosition.TOP_LEFT;
	private Point customPosition = new Point(0, 0);
	private SigningPage page = SigningPage.FIRST_PAGE;
	/**
	 * atunci cand se seteaza pagina, se seteaza si customPage, variabila care va fi pusa in sip.setPage(p)
	 */
	private int customPage = 0;
	private boolean isRealSignature = false;
	
	public boolean isVisibleSignature() {
		return isVisibleSignature;
	}

	public boolean isVisibleSN() {
		return isVisibleSN;
	}

	public String getOrganization() {
		return organization;
	}

	public String getReason() {
		return reason;
	}

	public boolean isVisibleReason() {
		return isVisibleReason;
	}

	public String getLocation() {
		return location;
	}

	public boolean isVisibleLocation() {
		return isVisibleLocation;
	}

	public SignatureSize getSize() {
		return size;
	}

	public SignaturePosition getPosition() {
		return position;
	}
	
	public Point getCustomPosition() {
		return customPosition;
	}

	public SigningPage getPage() {
		return page;
	}

	public int getCustomPage() {
		return customPage;
	}

	public boolean isRealSignature() {
		return isRealSignature;
	}

	public void setVisibleSignature(boolean isVisibleSignature) {
		boolean oldVal = this.isVisibleSignature;
		this.isVisibleSignature = isVisibleSignature;
		observed.firePropertyChange("isVisibleSignature", oldVal, isVisibleSignature);
	}

	public void setVisibleSN(boolean isVisibleSN) {
		boolean oldVal = this.isVisibleSN;
		this.isVisibleSN = isVisibleSN;
		observed.firePropertyChange("isVisibleSN", oldVal, isVisibleSN);
	}

	public void setOrganization(String organization) {
		String oldVal = this.organization;
		this.organization = organization;
		observed.firePropertyChange("organization", oldVal, organization);
	}

	public void setReason(String reason) {
		String oldVal = this.reason;
		this.reason = reason;
		System.out.println(oldVal + " -> " + reason);
		observed.firePropertyChange("reason", oldVal, reason);
	}

	public void setVisibleReason(boolean isVisibleReason) {
		boolean oldVal = this.isVisibleReason;
		this.isVisibleReason = isVisibleReason;
		observed.firePropertyChange("isVisibleReason", oldVal, isVisibleReason);
	}

	public void setLocation(String location) {
		String oldVal = this.location;
		this.location = location;
		observed.firePropertyChange("location", oldVal, location);
	}

	public void setVisibleLocation(boolean isVisibleLocation) {
		boolean oldVal = this.isVisibleLocation;
		this.isVisibleLocation = isVisibleLocation;
		observed.firePropertyChange("isVisibleLocation", oldVal, isVisibleLocation);
	}

	public void setSize(SignatureSize size) {
		SignatureSize oldVal = this.size;
		this.size = size;
		observed.firePropertyChange("size", oldVal, size);
	}

	public void setPosition(SignaturePosition position) {
		SignaturePosition oldVal = this.position;
		this.position = position;
		observed.firePropertyChange("position", oldVal, position);
	}

	public void setPage(SigningPage page) {
		SigningPage oldVal = this.page;
		this.page = page;
		switch (page) {
			case FIRST_PAGE:
				this.customPage = 1;
			break;
			case LAST_PAGE:
				System.out.println("selected last page");
				this.customPage = 9999;
			break;
			case CUSTOM_PAGE:
			break;
		}
		observed.firePropertyChange("page", oldVal, page);
	}

	public void setCustomPage(int customPage) {
		int oldVal = this.customPage;
		this.customPage = customPage;
		observed.firePropertyChange("customPage", oldVal, customPage);
	}

	public void setRealSignature(boolean isRealSignature) {
		boolean oldVal = this.isRealSignature;
		this.isRealSignature = isRealSignature;
		observed.firePropertyChange("isRealSignature", oldVal, isRealSignature);
	}

	public void setPosition(int pctX, int pctY) {
		this.customPosition.setLocation(pctX, pctY);
		this.setPosition(SignaturePosition.CUSTOM);
	}
	
	public void addPropertyChangeListener(String prop, PropertyChangeListener propertyChangeListener) 
	{
		observed.addPropertyChangeListener(prop, propertyChangeListener);
		propertyChangeListener.propertyChange(new PropertyChangeEvent(this, prop, null, null));
	}
	
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		observed.removePropertyChangeListener(pcl);
	}
	
	public PdfSigningOptions ignoreNextEvent() {
		observed.ignoreNextEvent();
		return this;
	}
}
