package model.signing.visible.options;

/**
 * Signature size in percents raported to pdPage.mediabox.width. Width is calculated based on page, 
 * then height is calculated based on width
 */
public enum SignatureSize 
{
	SMALL(13, 40),
	MEDIUM(20, 40),
	LARGE(30, 40);
	
	private final int pctWidth;
	private final int pctHeight;
	
	public int getPctWidth() {return this.pctWidth;}
	public int getPctHeight() {return this.pctHeight;}
	
    private SignatureSize(int width, int height) {
        this.pctWidth = width;
        this.pctHeight = height;
    }
}
