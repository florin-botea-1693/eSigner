package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.SigningModel;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import net.miginfocom.swing.MigLayout;
import utils.PropertyChangeSupportExtended;
import java.awt.Component;
import javax.swing.Box;

public class PDFSigningView extends JPanel// implements SigningView
{
	
	public final ChooseFileAndCertificate chooseFileAndCertificate;
	public final JLabel label_organization;
	public final JTextField input_organization;
	public final JLabel label_reason;
	public final JTextField input_reason;
	public final JCheckBox check_visibleReason;
	public final JLabel label_location;
	public final JTextField input_location;
	public final JCheckBox check_visibleLocation;
	public final JLabel label_signingMode;
	public final JComboBox select_visibleSignature;
	public final JComboBox select_signingPage;
	public final JCheckBox check_realSignature;
	public final JLabel label_customPage;
	public final JTextField input_customPage;
	public final JLabel label_aspectAndPosition;
	public final JButton button_togglePreview;
	public final JComboBox select_signatureSize;
	public final JComboBox select_signaturePosition;
	public final JButton button_sign;
	public final SigningLog signingLog;
	public final SigningPreview preview;
	public final JPanel LEFT_PANEL;
	public final JPanel RIGHT_PANEL;
	public final JButton button_preview;


	public PDFSigningView()
	{
		this.setLayout(new GridLayout(1, 2));
		
		this.LEFT_PANEL = new JPanel();
		this.RIGHT_PANEL = new JPanel();
		
		//=========================================||
		// LEFT PANEL
		//=========================================||
		LEFT_PANEL.setLayout(new MigLayout("", "[grow]", "[69.00,top][top][grow]"));
		
		this.chooseFileAndCertificate = new ChooseFileAndCertificate();
		
		this.label_organization = new JLabel("Organization");
		
		this.input_organization = new JTextField();

		this.label_reason = new JLabel("Reason");
		
		this.input_reason = new JTextField();

		this.check_visibleReason = new JCheckBox("Visible");
		
		this.label_location = new JLabel("Location");
		
		this.input_location = new JTextField();

		this.check_visibleLocation = new JCheckBox("Visible");

		this.label_signingMode = new JLabel("Signing mode");
		
		this.select_visibleSignature = new JComboBox(new String[]{"Invisible signature", "Visible signature"});
		
		this.select_signingPage = new JComboBox(SigningPage.values());
		
		this.check_realSignature = new JCheckBox("Real signature");
		
		this.label_customPage = new JLabel("page");

		this.input_customPage = new JTextField();
		this.input_customPage.setEditable(false);
		
		this.label_aspectAndPosition = new JLabel("Aspect and position");

		this.button_togglePreview = new JButton(">>");
		
		this.select_signatureSize = new JComboBox(SignatureSize.values());
		
		this.select_signaturePosition = new JComboBox(SignaturePosition.values());
		
		this.button_preview = new JButton(">>");
		
		this.button_sign = new JButton("Sign");
		
		this.signingLog = new SigningLog();
		
		// build-left-panel
		{
			JPanel signingOptions = new JPanel();
			signingOptions.setLayout(new MigLayout("", "[grow]", "[][][][][][][][][][][]"));
			signingOptions.add(this.label_organization, "cell 0 0");
			signingOptions.add(this.input_organization, "cell 0 1,grow");
			signingOptions.add(this.label_reason, "cell 0 2");
			signingOptions.add(this.input_reason, "cell 0 3,grow");
			signingOptions.add(this.check_visibleReason, "cell 0 3");
			signingOptions.add(this.label_location, "cell 0 4");
			signingOptions.add(this.input_location, "cell 0 5,grow");
			signingOptions.add(this.check_visibleLocation, "cell 0 5");
			signingOptions.add(this.label_signingMode, "cell 0 6");
			signingOptions.add(this.select_visibleSignature, "cell 0 7,grow");
			signingOptions.add(this.select_signingPage, "cell 0 7,grow");
			signingOptions.add(this.check_realSignature, "cell 0 7,grow");
			signingOptions.add(this.label_customPage, "cell 0 7");
			signingOptions.add(this.input_customPage, "cell 0 7,grow");
			signingOptions.add(this.label_aspectAndPosition, "flowx,cell 0 8");
			signingOptions.add(Box.createGlue(), "cell 0 8,grow");
			signingOptions.add(button_preview, "cell 0 8");
			signingOptions.add(this.select_signatureSize, "flowx,cell 0 9");
			signingOptions.add(this.select_signaturePosition, "cell 0 9");
			signingOptions.add(Box.createGlue(), "cell 0 9,grow");
			signingOptions.add(this.button_sign, "cell 0 9");
			
			LEFT_PANEL.add(chooseFileAndCertificate, "cell 0 0,growx");
			LEFT_PANEL.add(signingOptions, "cell 0 1,growx");
			LEFT_PANEL.add(signingLog, "cell 0 2,grow");
		}
		
		
		//=========================================||
		// RIGHT PANEL
		//=========================================||
		RIGHT_PANEL.setLayout(new MigLayout("", "[354px,grow,fill]", "[106px,grow,fill]"));
		this.preview = new SigningPreview();
		RIGHT_PANEL.add(preview, "cell 0 0,alignx left,aligny top");
		
		this.add(LEFT_PANEL);
		// this.add(RIGHT_PANEL);
	}
}
