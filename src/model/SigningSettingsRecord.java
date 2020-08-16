package model;

import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import model.signing.visible.SigningPage;

public class SigningSettingsRecord {
	public String organization;
	public String signing_reason;
	public boolean is_visible_reason;
	public String signing_location;
	public boolean is_visible_location;
	public boolean is_visible_sn;
	public SignatureSize signature_size;
	public SignaturePosition signature_position;
	public SigningPage signing_page;
	public boolean is_real_signature;
	public int custom_signing_page;
}
