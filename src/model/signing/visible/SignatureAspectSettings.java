package model.signing.visible;

import java.awt.Point;

import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;

public interface SignatureAspectSettings 
{

	SignaturePosition getPosition();

	SignatureSize getSize();

	String getReason();

	String getLocation();

	Point getCustomPosition();

	boolean isVisibleSN();

	int getCustomPage();

}
