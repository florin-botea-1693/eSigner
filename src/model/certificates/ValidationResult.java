package model.certificates;

public class ValidationResult {
	public final boolean canSign;
	public final String message;
	
	public ValidationResult(boolean canSign, String message) {
		this.canSign = canSign;
		this.message = message;
	}
}
