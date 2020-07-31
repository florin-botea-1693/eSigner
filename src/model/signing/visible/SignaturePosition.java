package model.signing.visible;

public enum SignaturePosition {
	TOP_LEFT(0, "Top-Left"),
	TOP_CENTER(1, "Top-Middle"),
	TOP_RIGHT(2, "Top-Right");
	
	public final int index;
	public final String name;
	
    private SignaturePosition(int index, String name) {
        this.index = index;
        this.name = name;
    }
}
