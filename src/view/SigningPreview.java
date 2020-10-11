package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import model.signing.visible.options.SignaturePosition;
import net.miginfocom.swing.MigLayout;

public class SigningPreview extends JPanel
{

	public final BackgroundPanel backgroundPanel;
	public final DraggableSignatureField draggableSignatureField;
	public final JSlider range_document;
	public final JSlider range_page;
	public final JLabel label_document;
	public final JLabel label_page;
	public final JLabel label_documentName;
	
	private File[] files = new File[] {};
	private PDFRenderer previewedPdf;

	public SigningPreview()
	{

		ImageIcon imageIcon = new ImageIcon();
		Image image = imageIcon.getImage();
		this.backgroundPanel = new BackgroundPanel(image);
		this.backgroundPanel.setBackground(Color.white);
		
		this.draggableSignatureField = new DraggableSignatureField(this.backgroundPanel);
		this.draggableSignatureField.setVisible(false);
		
		this.backgroundPanel.setLayout(null);
		this.backgroundPanel.add(draggableSignatureField);
		
		this.label_document = new JLabel("document");
		
		this.range_document = new JSlider();
		this.range_document.setMaximum(0);
		
		this.label_page = new JLabel("page");
		
		this.range_page = new JSlider();
		this.range_page.setMaximum(0);
		
		this.label_documentName = new JLabel("document_name");
		
		JPanel fileAndPage = new JPanel();
		{
			fileAndPage.setLayout(new MigLayout("", "[122.00][grow,fill]", "[grow][][]"));
			fileAndPage.add(label_document, "cell 0 0");
			fileAndPage.add(range_document, "cell 1 0,grow");
			fileAndPage.add(label_page, "cell 0 1");
			fileAndPage.add(range_page, "cell 1 1,grow");
			fileAndPage.add(label_documentName, "cell 1 2,alignx right");
		}
		
		this.setLayout(new MigLayout("", "[grow]", "[grow,fill][]"));
		this.add(backgroundPanel, "cell 0 0,grow");
		this.add(fileAndPage, "cell 0 1,growx");
	}
	
	public void addFiles(File[] files)
	{
		this.files = files;
		// add range, set file to 1
		try {
			this.setPreviewDocument(files[0]);
			this.setPreviewPage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setPreviewDocument(File file) throws IOException
	{
		PDDocument document = PDDocument.load(file);
		previewedPdf = new PDFRenderer(document);
	}
	
	private void setPreviewPage(int page) throws IOException
	{
		if (previewedPdf == null)
			return;
		BufferedImage bim = previewedPdf.renderImageWithDPI(page, 300, ImageType.RGB);
		backgroundPanel.setImage(bim);
	}

	public void setDraggableSignatureFieldPosition(SignaturePosition selectedItem)
	{	
		switch (selectedItem) {
			case TOP_LEFT:
				this.draggableSignatureField.setLocationPct(0, 0);
				break;
			case TOP_CENTER:
				this.draggableSignatureField.setLocationPct(50, 0);
				break;
			case TOP_RIGHT:
				this.draggableSignatureField.setLocationPct(100, 0);
				break;
			case MIDDLE_LEFT:
				this.draggableSignatureField.setLocationPct(0, 50);
				break;
			case MIDDLE_CENTER:
				this.draggableSignatureField.setLocationPct(50, 50);
				break;
			case MIDDLE_RIGHT:
				this.draggableSignatureField.setLocationPct(100, 50);
				break;
			case BOTTOM_LEFT:
				this.draggableSignatureField.setLocationPct(0, 100);
				break;
			case BOTTOM_CENTER:
				this.draggableSignatureField.setLocationPct(50, 100);
				break;
			case BOTTOM_RIGHT:
				this.draggableSignatureField.setLocationPct(100, 100);
				break;
		}
	}
}
