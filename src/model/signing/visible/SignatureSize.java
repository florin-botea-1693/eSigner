package model.signing.visible;

public enum SignatureSize {
	SMALL(120, 50),
	MEDIUM(150, 70),
	LARGE(240, 100);
	
	private final int width;
	private final int height;
	
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
    private SignatureSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
