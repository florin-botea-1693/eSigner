package model.signing;

public interface HasPDFSigningOptions extends HasBasicSigningOptions {
	public void setNeedVisibleSignature(boolean b);
	public boolean isVisibleSignature();
	
	public void setNeedVisibleLocation(boolean b);
	public boolean isVisibleLocation();
	
	public void setNeedVisibleReason(boolean b);
	public boolean isVisibleReason();
	
	public void setNeedVisibleDate(boolean b);
	public boolean isVisibleDate();
	
	public void setNeedVisibleOrganization(boolean b);
	public boolean isVisibleOrganization();
	
	public void setNeedVisibleSerialNumber(boolean b);
	public boolean isVisibleSerialNumber();
	
	public boolean hasSignatureImage();
	public boolean hasCustomName();
	public String getCustomName();
	
	public String getPDFSignatureDesign();
	public String getFont();
	public int getFontSize();
	public int getPage();
	public int getPosXPercents();
	public int getPosYPercents();
}
