package model.signing.visible;

public enum SignatureSize {
	SMALL(0, "Small"),
	MEDIUM(1, "Medium"),
	LARGE(2, "Large");
	
	public final int index;
	public final String name;
	
    private SignatureSize(int index, String name) {
        this.index = index;
        this.name = name;
    }
}
