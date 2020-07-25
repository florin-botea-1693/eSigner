package model.signing;

import model.AppSettings;

public class PDFSigningOptions extends SigningOptions implements HasPDFSigningOptions {

	private boolean isVisibleSignature = true;
	private boolean isVisibleLocation = false;
	private boolean isVisibleReason = false;
	private boolean isVisibleDate = false;
	private boolean isVisibleOrganization = false;
	private boolean isVisibleSerialNumber = false;
	
	@Override
	public void setNeedVisibleSignature(boolean b) {
		this.isVisibleSignature = b;
	}
	@Override
	public boolean isVisibleSignature() {
		return this.isVisibleSignature;
	}

	@Override
	public void setNeedVisibleLocation(boolean b) {
		this.isVisibleLocation = b;
	}
	@Override
	public boolean isVisibleLocation() {
		return this.isVisibleLocation;
	}

	@Override
	public void setNeedVisibleReason(boolean b) {
		this.isVisibleReason = b;
	}
	@Override
	public boolean isVisibleReason() {
		return this.isVisibleReason;
	}

	@Override
	public void setNeedVisibleDate(boolean b) {
		this.isVisibleDate = b;
	}
	@Override
	public boolean isVisibleDate() {
		return this.isVisibleDate;
	}

	@Override
	public void setNeedVisibleOrganization(boolean b) {
		this.isVisibleOrganization = b;
	}
	@Override
	public boolean isVisibleOrganization() {
		return this.isVisibleOrganization;
	}
	
	@Override
	public void setNeedVisibleSerialNumber(boolean b) {
		this.isVisibleSerialNumber = b;
	}
	@Override
	public boolean isVisibleSerialNumber() {
		return this.isVisibleSerialNumber;
	}

	public void loadFromAppSettings(AppSettings settings) {
		super.loadFromAppSettings(settings);
		// alte chestii
	}
	@Override
	public boolean hasSignatureImage() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getCustomName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPDFSignatureDesign() {
		// TODO Auto-generated method stub
		return "BASIC";
	}
	@Override
	public String getFont() {
		// TODO Auto-generated method stub
		return "arial";
	}
	@Override
	public int getFontSize() {
		// TODO Auto-generated method stub
		return 12;
	}
	@Override
	public int getPage() {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public int getPosXPercents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getPosYPercents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
