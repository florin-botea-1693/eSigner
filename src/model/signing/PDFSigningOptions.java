package model.signing;

public class PDFSigningOptions {

	private boolean isVisibleSignature = true;
	private boolean isVisibleLocation = false;
	private boolean isVisibleReason = false;
	private boolean isVisibleDate = false;
	private boolean isVisibleOrganization = false;
	private boolean isVisibleSerialNumber = false;
	
	public void setNeedVisibleSignature(boolean b) {
		this.isVisibleSignature = b;
	}
	
	public boolean isVisibleSignature() {
		return this.isVisibleSignature;
	}

	public void setNeedVisibleLocation(boolean b) {
		this.isVisibleLocation = b;
	}
	
	public boolean isVisibleLocation() {
		return this.isVisibleLocation;
	}

	public void setNeedVisibleReason(boolean b) {
		this.isVisibleReason = b;
	}
	
	public boolean isVisibleReason() {
		return this.isVisibleReason;
	}

	public void setNeedVisibleDate(boolean b) {
		this.isVisibleDate = b;
	}
	
	public boolean isVisibleDate() {
		return this.isVisibleDate;
	}

	public void setNeedVisibleOrganization(boolean b) {
		this.isVisibleOrganization = b;
	}

	public boolean isVisibleOrganization() {
		return this.isVisibleOrganization;
	}
	

	public void setNeedVisibleSerialNumber(boolean b) {
		this.isVisibleSerialNumber = b;
	}

	public boolean isVisibleSerialNumber() {
		return this.isVisibleSerialNumber;
	}

	//public void loadFromAppSettings(AppSettings settings) {
		//super.loadFromAppSettings(settings);
		// alte chestii
	//}

	public boolean hasSignatureImage() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCustomName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPDFSignatureDesign() {
		// TODO Auto-generated method stub
		return "BASIC";
	}

	public String getFont() {
		// TODO Auto-generated method stub
		return "arial";
	}

	public int getFontSize() {
		// TODO Auto-generated method stub
		return 12;
	}

	public int getPage() {
		// TODO Auto-generated method stub
		return 1;
	}

	public int getPosXPercents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPosYPercents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
