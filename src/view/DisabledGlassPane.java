package view;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;

public class DisabledGlassPane extends JPanel {

	/**
	 * Create the panel.
	 */
	public DisabledGlassPane() {
		setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Please wait...");
		lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
		add(lblNewLabel);

	}
}
