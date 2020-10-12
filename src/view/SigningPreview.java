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
import model.signing.visible.options.SignatureSize;
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
	private PDDocument previewedPdf;
	
	private JPanel mainPanel = new JPanel();
	private JPanel loadingPanel = new JPanel();
	
	private int file_index = 0;
	private int page_index = 0;
	private SignaturePosition signaturePosition;
	

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
		
		mainPanel.setLayout(new MigLayout("", "[grow]", "[grow,fill][]"));
		mainPanel.add(backgroundPanel, "cell 0 0,grow, hidemode 3");
		mainPanel.add(loadingPanel, "cell 0 0,grow, hidemode 3");
		mainPanel.add(fileAndPage, "cell 0 1,growx, hidemode 3");
		
		ImageIcon loading = new ImageIcon("ajax-loader.gif");
		loadingPanel.add(new JLabel("loading... ", loading, JLabel.CENTER));

		this.setLayout(new MigLayout("", "[grow]", "[grow,fill]"));
		this.add(mainPanel, "cell 0 0, grow, hidemode 3");
		//this.add(loadingPanel, "cell 0 0, grow, hidemode 3");
		loadingPanel.setVisible(false);
	}
	
	public void addFiles(File[] files)
	{
		this.files = files;
		// add range, set file to 1
		try {
			setLoading(true);
			this.range_document.setMaximum(files.length -1);
			this.setPreviewDocument(0, true);
			this.setPreviewPage(0, true);
		} catch (IOException e) {
			e.printStackTrace();
			ImageIcon imageIcon = new ImageIcon();
			Image image = imageIcon.getImage();
			backgroundPanel.setImage(image);
			setLoading(false);
		}
	}
	
	public void setPreviewDocument(int file_index, boolean force) throws IOException
	{
		if (this.file_index == file_index && !force)
			return;
		
		this.file_index = file_index;
		this.label_document.setText((file_index + 1) + "/" + this.files.length);
		File file = files[file_index];
		this.previewedPdf = PDDocument.load(file);
		this.range_page.setEnabled(false);
		this.range_page.setMaximum(previewedPdf.getNumberOfPages() -1);
		this.setPreviewPage(0, true);
	}
	
	public void setPreviewPage(int page, boolean force)
	{
		if (this.page_index  == page && !force)
			return;
		
		this.label_page.setText((page + 1) + "/" + this.previewedPdf.getNumberOfPages());
		this.page_index = page;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					setLoading(true);
					PDFRenderer reader = new PDFRenderer(previewedPdf);
					BufferedImage bim = reader.renderImageWithDPI(page, 300, ImageType.RGB);
					backgroundPanel.setImage(bim);
					setLoading(false);
				} catch (IOException e) {
					e.printStackTrace();
					ImageIcon imageIcon = new ImageIcon();
					Image image = imageIcon.getImage();
					backgroundPanel.setImage(image);
					setLoading(false);
				}
			}
		}).start();
	}

	public void setDraggableSignatureFieldPosition(SignaturePosition selectedItem)
	{
		this.signaturePosition = selectedItem;
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
	
	public void setDraggableSignatureFieldSize(SignatureSize size) {
		this.draggableSignatureField.setSize(size);
		this.setDraggableSignatureFieldPosition(signaturePosition);
	}
	
	private void setLoading(boolean b)
	{
		backgroundPanel.setVisible(!b);
		loadingPanel.setVisible(b);
		range_document.setEnabled(!b);
		range_page.setEnabled(!b);
	}
}
