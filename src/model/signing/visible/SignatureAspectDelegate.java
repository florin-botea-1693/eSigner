package model.signing.visible;

/*
 * DISPARE
 */

public final class SignatureAspectDelegate {
	public static SignatureAspect getAspect(String string) {
		// if at != aspectType, switch -> 
		return new BasicSignatureAspect();
	}
}
