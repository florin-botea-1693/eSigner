package model.signing.visible;

import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentHorizontal;
import eu.europa.esig.dss.model.pades.SignatureImageParameters.VisualSignatureAlignmentVertical;

public enum SignaturePosition {
	TOP_LEFT(VisualSignatureAlignmentVertical.TOP, VisualSignatureAlignmentHorizontal.LEFT),
	TOP_CENTER(VisualSignatureAlignmentVertical.TOP, VisualSignatureAlignmentHorizontal.CENTER),
	TOP_RIGHT(VisualSignatureAlignmentVertical.TOP, VisualSignatureAlignmentHorizontal.RIGHT),
	MIDDLE_LEFT(VisualSignatureAlignmentVertical.MIDDLE, VisualSignatureAlignmentHorizontal.LEFT),
	MIDDLE_CENTER(VisualSignatureAlignmentVertical.MIDDLE, VisualSignatureAlignmentHorizontal.CENTER),
	MIDDLE_RIGHT(VisualSignatureAlignmentVertical.MIDDLE, VisualSignatureAlignmentHorizontal.RIGHT),
	BOTTOM_LEFT(VisualSignatureAlignmentVertical.BOTTOM, VisualSignatureAlignmentHorizontal.LEFT),
	BOTTOM_CENTER(VisualSignatureAlignmentVertical.BOTTOM, VisualSignatureAlignmentHorizontal.CENTER),
	BOTTOM_RIGHT(VisualSignatureAlignmentVertical.BOTTOM, VisualSignatureAlignmentHorizontal.RIGHT);
	
	private final VisualSignatureAlignmentVertical vertical;
	private final VisualSignatureAlignmentHorizontal horizontal;
	
	public VisualSignatureAlignmentVertical getVerticalAlignment() { return vertical; }
	public VisualSignatureAlignmentHorizontal getHorizontalAlignment() { return horizontal; }
	
    private SignaturePosition(VisualSignatureAlignmentVertical vertical, VisualSignatureAlignmentHorizontal horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
    }
}
