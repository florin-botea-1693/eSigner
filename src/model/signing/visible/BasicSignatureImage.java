package model.signing.visible;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.pades.DSSJavaFont;
import eu.europa.esig.dss.model.pades.SignatureImageParameters;
import eu.europa.esig.dss.model.pades.SignatureImageTextParameters;
import eu.europa.esig.dss.pdf.pdfbox.visible.defaultdrawer.ImageTextWriter;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import model.signing.HasPDFSigningOptions;
import model.signing.visible.CustomSignatureImage.BuildDirection;

// class-ul asta va avea 2 constructori, unul care accepta param File, altul care accepta param String formatat \n new line
// celalalt class-text signature va avea metode: getRequiredWidth getRequiredHeight-nu e nevoie fac teste, care ma vor ajuta ca sa definesc dimensiunea campului de semnatura

public class BasicSignatureImage implements SignatureImage {
	
	private BufferedImage[] images = new BufferedImage[2];
	
	private BufferedImage createFromText(String text, DSSJavaFont font) {
		SignatureImageTextParameters tp = new SignatureImageTextParameters();
		tp.setFont(new DSSJavaFont("arial"));
		//tp.setTextColor(Color.RED);
		tp.setSize(8);
		tp.setBackgroundColor(new Color ( 0f, 0f, 0f, .1f ));
		BufferedImage img = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp, text);
		try {
			ImageIO.write(img, "gif", new File("foo.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return img;
	}

	@Override
	public void addImage(BufferedImage img) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addImage(BufferedImage img, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addImageFromText(String text, DSSJavaFont font) {
		images[nextFreeSlot()] = createFromText(text, font);
	}

	@Override
	public void addImageFromText(String text, DSSJavaFont font, int index) {
		images[index] = createFromText(text, font);
	}

	@Override
	public InMemoryDocument getFinalImage(int width, int height, SignatureImageRenderMode renderMode) {
		BufferedImage concatImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = concatImage.createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        //g2d.setBackground(new Color ( 0f, 0f, 0f, .1f ));
        g2d.setPaint ( new Color ( 0f, 0f, 0f, .1f ) );
        g2d.fillRect ( 0, 0, concatImage.getWidth(), concatImage.getHeight() );// fara asta nu face nimic
        
        int currentWidth = renderMode == SignatureImageRenderMode.RTL ? width : 0;
        int currentHeight = renderMode == SignatureImageRenderMode.BTT ? height : 0;
		for (BufferedImage img : images) {
			int imgW, imgH;
			if (img.getHeight() >= img.getWidth()) {// portret
				imgH = height; //(width/2) * leftImage.getHeight() / leftImage.getWidth();
				imgW = imgH * img.getWidth() / img.getHeight();
			}
			else { // landescape
				imgW = width/2; //height * leftImage.getWidth() / leftImage.getHeight();
				imgH = imgW * img.getHeight() / img.getWidth();
			}
	    	switch (renderMode) {
				case LTR:
					g2d.drawImage( img, currentWidth, 0, null );
					currentWidth += width/2;
				break;
				case RTL:
					currentWidth -= width/2;
					//g2d.drawImage( img, currentWidth, 0, imgW, imgH, null );
				break;
				case TTB:
					//g2d.drawImage( img, 0, currentHeight, imgW, imgH, null );
					currentHeight += currentHeight/2;
				break;
				case BTT:
					currentHeight -= currentHeight/2;
					//g2d.drawImage( img, 0, currentHeight, imgW, imgH, null );
				break;
	    	}
		}
        g2d.dispose();
        
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(images[0], "gif", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		InMemoryDocument image = new InMemoryDocument();
		image.setName("signature-image");
		
		return new InMemoryDocument(is);
	}

	@Override
	public int countImages() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxImages(int n) {
		// TODO Auto-generated method stub
		
	}
	
	private int nextFreeSlot() {
		int i = 0;
		for(int j = 0; j<images.length; j++) {
		    if(images[j] == null) {
		        i = j; break;
		    }
		}
		return i;
	}
	
	/*
	private BufferedImage createImage(String text, int width, int height, Font font) {
	    _g2d.setFont(font);
	    FontMetrics fm = _g2d.getFontMetrics();
	    String[] rows = text.split("\n");
	    int w = 0;
	    int h = rows.length * fm.getHeight();
	    for (String row : rows) {
	    	int rw = fm.stringWidth(row);
	    	w = w < rw ? rw : w;
	    }

	    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = img.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
	        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
	        RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
	        RenderingHints.VALUE_DITHER_ENABLE);
	    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
	        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
	        RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
	        RenderingHints.VALUE_STROKE_PURE);
	    g2d.setFont(font);
	    fm = g2d.getFontMetrics();
	    g2d.setColor(Color.BLACK);
	    int ch = 0;
	    for (String row : rows) {
	    	g2d.drawString(row, 0, ch);
	    	ch += fm.getHeight();
	    }
	    g2d.dispose();
	    try {
			ImageIO.write(img, "gif", new File("foo.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return img;
	}
	
	public void setLeftImageFromText(String text, Font font) {
		leftImage = createImage(text, font);
	}
	
	public void setRightImageFromText(String text, Font font) {
		rightImage = createImage(text, font);
	}

	public InMemoryDocument getFinalImage() {
		int leftImgHeight, leftImgWidth;
		if (leftImage.getHeight() >= leftImage.getWidth()) {// portret
			leftImgHeight = height; //(width/2) * leftImage.getHeight() / leftImage.getWidth();
			leftImgWidth = leftImgHeight * leftImage.getWidth() / leftImage.getHeight();
		}
		else { // landescape
			leftImgWidth = width/2; //height * leftImage.getWidth() / leftImage.getHeight();
			leftImgHeight = leftImgWidth * leftImage.getHeight() / leftImage.getWidth();
		}
		int leftImgPosY = (height-leftImage.getHeight()) > 0 ? height-leftImage.getHeight()/2 : 0;

		BufferedImage img = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D tGraphics2D = img.createGraphics(); //create a graphics object to paint to
		tGraphics2D.setBackground( Color.WHITE );
		tGraphics2D.setPaint( Color.WHITE );
		tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		tGraphics2D.drawImage( leftImage, 0, leftImgPosY, leftImgWidth, leftImgHeight, null );
        
		tGraphics2D.drawImage( rightImage, width/2, 0, width/2, height, null );
		
		tGraphics2D.dispose();
        
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "gif", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		InMemoryDocument image = new InMemoryDocument();
		image.setName("signature-image");
		
		return new InMemoryDocument(is);
	}
	
	public static void main(String[] args) throws IOException {
		SignatureImageTextParameters tp1 = new SignatureImageTextParameters();
		SignatureImageTextParameters tp2 = new SignatureImageTextParameters();
		tp1.setFont(new DSSJavaFont("arial", Font.PLAIN, 12));
		tp2.setFont(new DSSJavaFont("arial"));
		//tp1.setSize(8);
		tp2.setSize(8);
        int imagesCount = 4;
        BufferedImage images[] = new BufferedImage[imagesCount];
        for(int j = 0; j < images.length; j++) {
            images[j] = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp1, "foo bar");
        }
        int heightTotal = 0;
        for(int j = 0; j < images.length; j++) {
            heightTotal += images[j].getHeight();
        }
        int heightCurr = 0;
        BufferedImage concatImage = new BufferedImage(100, heightTotal, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = concatImage.createGraphics();
        g2d.setPaint ( new Color ( 255, 255, 255 ) );
        g2d.fillRect ( 0, 0, concatImage.getWidth(), concatImage.getHeight() );
        for(int j = 0; j < images.length; j++) {
            g2d.drawImage(images[j], 0, heightCurr, null);
            heightCurr += images[j].getHeight();
        }
        g2d.dispose();
        ImageIO.write(concatImage, "png", new File("concat.png")); // export concat image
	}
	*/
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
	public BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight,Object hint, boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage)img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}
		
		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}
			
			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}
			
			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();
			
			ret = tmp;
		} while (w != targetWidth || h != targetHeight);
		
		return ret;
	}
}
