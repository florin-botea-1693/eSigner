package controller.listeners;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface InputListener extends DocumentListener
{
	@Override
	public default void insertUpdate(DocumentEvent e) {
		onInsertChange();
	}
	@Override
	public default void removeUpdate(DocumentEvent e) {
		onInsertChange();
	}
	@Override
	public default void changedUpdate(DocumentEvent e) {
		onInsertChange();
	}
	
	public void onInsertChange();
}
