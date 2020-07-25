package model.signing;

import model.AppSettings;

public class SigningOptions implements HasBasicSigningOptions {

	private boolean isOrganizationInDescription = false;
	private boolean isFunctionInDescription = false;
	
	@Override
	public void setNeedOrganizationInDescription(boolean b) {
		this.isOrganizationInDescription = b;
	}

	@Override
	public boolean isOrganizationInDescription() {
		return this.isOrganizationInDescription;
	}

	@Override
	public void setNeedFunctionInDescription(boolean b) {
		this.isFunctionInDescription = b;
	}

	@Override
	public boolean isFunctionInDescription() {
		return this.isFunctionInDescription;
	}

	public void loadFromAppSettings(AppSettings settings) {
		
	}
}
