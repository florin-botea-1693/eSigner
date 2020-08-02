package utils;

public class PropertyChangeSupportExtended extends PropertyChangeSupport {
	
	private boolean skipNextTurn = false;
	
	public PropertyChangeSupportExtended(Object subject) {
		super(subject);
	}
	
	@Override
	public void firePropertyChange(String propName, Object oldVal, Object newVal) {
		if (!this.skipNextTurn) super.firePropertyChange(propName, oldVal, newVal);
		this.skipNextTurn = false;
	}
	
	public void skipNextTurn() {
		this.skipNextTurn = true;
	}
}