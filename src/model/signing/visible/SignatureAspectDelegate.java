package model.signing.visible;

public final class SignatureAspectDelegate {
	public static SignatureAspect getAspect(String string) {
		// if at != aspectType, switch -> 
		return new BasicSignatureAspect();
	}
}
