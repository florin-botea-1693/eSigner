package model.signing.visible;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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

// class-ul asta va avea 2 constructori, unul care accepta param File, altul care accepta param String formatat \n new line
// celalalt class-text signature va avea metode: getRequiredWidth getRequiredHeight-nu e nevoie fac teste, care ma vor ajuta ca sa definesc dimensiunea campului de semnatura

public class CustomSignatureImage {
	
	public enum BuildDirection {LTR, RTL, TTB, BTT};
	
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	
	public void addTextImage(String text, DSSJavaFont font) {
		SignatureImageTextParameters tp = new SignatureImageTextParameters();
		tp.setFont(font);
		//tp.setTextColor(textColor);
		tp.setBackgroundColor(new Color ( 0f, 0f, 0f, .1f ));
		BufferedImage img = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp, text);
		try {
			ImageIO.write(img, "gif", new File("foo.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		images.add(img);
	}
	
	public void addTextImage(String text, DSSJavaFont font, int index) {
		SignatureImageTextParameters tp = new SignatureImageTextParameters();
		tp.setFont(font);
		tp.setBackgroundColor(new Color ( 255, 255, 255, 255 ));
		BufferedImage img = ImageTextWriter.createTextImage(new SignatureImageParameters(), tp, text);
		images.add(index, img);
	}
	
	public InMemoryDocument getConcatImage(BuildDirection direction, boolean centered) {
        int totalWidth = 0;
        int totalHeight = 0;
		for (BufferedImage img : images) {
			if (direction == BuildDirection.LTR || direction == BuildDirection.RTL) {
				totalWidth += img.getWidth();
				totalHeight = totalHeight < img.getHeight() ?  img.getHeight() : totalHeight;
			}
			else {
				totalHeight += img.getHeight();
				totalWidth = totalWidth < img.getWidth() ?  img.getWidth() : totalWidth;
			}
		}
		BufferedImage concatImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = concatImage.createGraphics();
        //g2d.setBackground(new Color ( 0f, 0f, 0f, .1f ));
        g2d.setPaint ( new Color ( 0f, 0f, 0f, .1f ) );
        g2d.fillRect ( 0, 0, concatImage.getWidth(), concatImage.getHeight() );// fara asta nu face nimic
        int currentWidth = direction == BuildDirection.RTL ? totalWidth : 0;
        int currentHeight = direction == BuildDirection.BTT ? totalHeight : 0;
        for (BufferedImage img : images) {
        	switch (direction) {
        		case LTR:
        			currentHeight = centered ? (totalHeight-img.getHeight())/2 : 0;
        			g2d.drawImage(img, currentWidth, currentHeight, null);
        			currentWidth += img.getWidth();
        		break;
        		case RTL:
        			currentHeight = centered ? (totalHeight-img.getHeight())/2 : 0;
        			currentWidth -= img.getWidth();
        			g2d.drawImage(img, currentWidth, currentHeight, null);
        		break;
        		case TTB:
        			currentWidth = centered ? (totalWidth-img.getWidth())/2 : 0;
        			g2d.drawImage(img, currentWidth, currentHeight, null);
        			currentHeight += img.getHeight();
        		break;
        		case BTT:
        			currentWidth = centered ? (totalWidth-img.getWidth())/2 : 0;
        			currentHeight -= img.getHeight();
        			g2d.drawImage(img, currentWidth, currentHeight, null);
        		break;
        	}
        }
        g2d.dispose();
        
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(concatImage, "gif", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		InMemoryDocument image = new InMemoryDocument();
		image.setName("signature-image");
		
		return new InMemoryDocument(is);
	}
	
	public static void main(String[] args) throws IOException {
		SignatureImage app = new SignatureImage();
		app.addTextImage("foo bar", new DSSJavaFont("arial"));
		/*
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
        */
	}
}
