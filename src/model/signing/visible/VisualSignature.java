package model.signing.visible;

import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import model.signing.HasPDFSigningOptions;

public interface VisualSignature {
	public PAdESSignatureParameters build(DSSPrivateKeyEntry privateKey, HasPDFSigningOptions options);
}
