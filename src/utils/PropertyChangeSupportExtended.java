package utils;

import java.beans.PropertyChangeSupport;

public class PropertyChangeSupportExtended extends PropertyChangeSupport {
	
	private boolean pauseNextTurn = false;
	private boolean disabled = false;
	
	public PropertyChangeSupportExtended(Object subject) {
		super(subject);
	}
	
	@Override
	public void firePropertyChange(String propName, Object oldVal, Object newVal) {
		if (!pauseNextTurn && !disabled) super.firePropertyChange(propName, oldVal, newVal);
		pauseNextTurn = false;
	}
	
	public void ignoreNextEvent() {
		pauseNextTurn = true;
	}

	public void silencedEvents(boolean b) {
		disabled = b;
	}

}