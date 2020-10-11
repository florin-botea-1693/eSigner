package view;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import main.App;

public class DraggableSignatureField extends JComponent
{

	private JComponent parent;
	
	private volatile int screenX = 0;
	private volatile int screenY = 0;
	private volatile double myX = 0;
	private volatile double myY = 0;
	
	private int pctX = 0; // percents
	private int pctY = 0; // percents
	
	public int getPctX() {
		return this.pctX;
	}
	public int getPctY() {
		return this.pctY;
	}
	
	public DraggableSignatureField(JComponent parent)
	{
		this.parent = parent;
		setLocation(0, 0);
		setBorder(new LineBorder(Color.BLUE, 3));
	    setBackground(Color.white);
	    setOpaque(false);

	    this.addMouseListener(new MouseListener() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {}

	    	@Override
	    	public void mousePressed(MouseEvent e) {
	    		screenX = e.getXOnScreen();
	    		screenY = e.getYOnScreen();
	    		myX = getX();
	    		myY = getY();
	    	}
	    	@Override
	    	public void mouseReleased(MouseEvent e) {
	    		pctX = getX() * 100 / (parent.getWidth() - getWidth()); // calculam procentul
	    		pctY = getY() * 100 / (parent.getHeight() - getHeight()); // calculam procentul
	    	}
	    	@Override
	    	public void mouseEntered(MouseEvent e) {}
	    	@Override
	    	public void mouseExited(MouseEvent e) {}
	    });
	    
	    this.addMouseMotionListener(new MouseMotionListener() {
	    	@Override
	    	public void mouseDragged(MouseEvent e) {
	    		int deltaX = e.getXOnScreen() - screenX;
	    		int deltaY = e.getYOnScreen() - screenY;
	    		setLocation((int)myX+deltaX, (int)myY+deltaY);
	    	}
	    	@Override
	    	public void mouseMoved(MouseEvent e) {}
		});
	    
		this.parent.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				keepProportionsAndPosition();
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
        });
	}
	
	public void setLocationPct(int pctX, int pctY)
	{
		this.pctX = pctX;
		this.pctY = pctY;
		int x = pctX * (this.parent.getWidth()-this.getWidth()) / 100;
		int y = pctY * (this.parent.getHeight()-this.getHeight()) / 100;
		this.setLocation(x, y);
	}
	
	private void keepProportionsAndPosition() 
	{
		int w = parent.getWidth()*30/100;
		int h = parent.getHeight()*15/100;
		int x = this.pctX * (this.parent.getWidth()-w) / 100;
		int y = this.pctY * (this.parent.getHeight()-h) / 100;
		
		this.setBounds(x, y, w, h);
	}
}