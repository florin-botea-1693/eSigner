package model.signing.visible;

import java.awt.image.BufferedImage;

import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.pades.DSSJavaFont;
import model.signing.visible.options.SignatureImageRenderMode;

public interface SignatureImage {
	
	public void addImage(BufferedImage img);
	public void addImage(BufferedImage img, int index);
	public void addImageFromText(String text, DSSJavaFont font);
	public void addImageFromText(String text, DSSJavaFont font, int index);
	public InMemoryDocument getFinalImage(int width, int height, SignatureImageRenderMode renderMode);
	public int countImages();
	public void setMaxImages(int n);
}
