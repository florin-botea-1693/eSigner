package model.signing.visible;

public final class SignatureAspectDelegate {
	private String aspectType;
	private SignatureAspect aspect;
	
	public SignatureAspect getAspect(String at, SignatureAspectConfig cfg) {
		// if at != aspectType, switch -> 
		aspect = new BasicSignatureAspect();
		return aspect.withConfig(cfg);
	}
}
