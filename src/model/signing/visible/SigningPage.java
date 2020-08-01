package model.signing.visible;

public enum SigningPage {
	FIRST_PAGE(0),
	LAST_PAGE(1),
	ALL_PAGES(2),
	CUSTOM_PAGE(3);
	
	public final int index;
	
    private SigningPage(int index) {
        this.index = index;
    }
}
