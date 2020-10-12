package utils;

import java.util.ArrayList;

public class RectangleContainer
{
	private RectangleContainer parent = null;
	private ArrayList<RectangleContainer> childs = new ArrayList<RectangleContainer>();
	
	private float x;
	private float y;
	private float width;
	private float height;

	public RectangleContainer(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void addChild(RectangleContainer rect)
	{
		//childs.add(rect);
		rect.setParent(this);
	}
	
	public void setParent(RectangleContainer rect)
	{
		parent = rect;
		//rect.addChild(this);
	}
	
	
	public float getGlobalX() {
		return ForceBetween.number(x + (parent != null ? parent.getX() : 0), 0, parent.getWidth()-this.width);
	}
	
	public float getGlobalY() {
		return ForceBetween.number(y + (parent != null ? parent.getY() : 0), 0, parent.getHeight()-this.height);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
}