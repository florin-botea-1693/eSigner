package view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class SigningMessage3 extends JPanel {

	/**
	 * Create the panel.
	 */
	public SigningMessage3() {
		
	}
	
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D)g;
        g2D.setColor(Color.black);
        g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }
}
