package model.signing;

public interface HasBasicSigningOptions {
	public void setNeedOrganizationInDescription(boolean b);
	public boolean isOrganizationInDescription();
	
	public void setNeedFunctionInDescription(boolean b);
	public boolean isFunctionInDescription();
}
