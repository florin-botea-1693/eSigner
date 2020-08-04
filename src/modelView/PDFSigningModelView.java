package modelView;

import model.PDFSignerModel;
import model.certificates.Certificate;
import model.signing.visible.SignaturePosition;
import model.signing.visible.SignatureSize;
import model.signing.visible.SigningPage;

public class PDFSigningModelView {
	
	public final Object[] certificates;
	public final boolean isVisibleSerialNumber;
	public final String signingReason;
	public final boolean isVisibleReason;
	public final String signingLocation;
	public final boolean isVisibleLocation;
	public final boolean isVisibleSignature;
	public final SigningPage signingPage;
	public final boolean isRealSignature;
	public final int customSigningPage;
	public final SignatureSize signatureSize;
	public final SignaturePosition signaturePosition;
	
	public PDFSigningModelView(PDFSignerModel model) {
		this.certificates = model.getCertificates().toArray();
		this.isVisibleSerialNumber = model.isVisibleSerialNumber();
		this.signingReason = model.getSigningReason();
		this.isVisibleReason = model.isVisibleReason();
		this.signingLocation = model.getSigningLocation();
		this.isVisibleLocation = model.isVisibleLocation();
		this.isVisibleSignature = model.isVisibleSignature();
		this.signingPage = model.getSigningPage();
		this.isRealSignature = model.isRealSignature();
		this.customSigningPage = model.getCustomSigningPage();
		this.signatureSize = model.getSignatureSize();
		this.signaturePosition = model.getSignaturePosition();
	}
}
