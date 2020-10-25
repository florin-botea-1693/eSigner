package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import main.App;
import model.certificates.Certificate;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import net.miginfocom.swing.MigLayout;
import utils.PropertyChangeSupportExtended;
import view.components.ChooseFileAndCertificate;
import view.components.SigningLog;
import view.components.SigningPreview;

import java.awt.Component;
import javax.swing.Box;
import java.awt.Font;

public class PdfSigningView extends JPanel implements IPdfSigningView
{
	
	private ChooseFileAndCertificate chooseFileAndCertificate;
	private JLabel label_organization;
	private JTextField input_organization;
	private JLabel label_reason;
	private JTextField input_reason;
	private JCheckBox check_visibleReason;
	private JLabel label_location;
	private JTextField input_location;
	private JCheckBox check_visibleLocation;
	private JLabel label_signingMode;
	private JComboBox select_visibleSignature;
	private JComboBox select_signingPage;
	private JCheckBox check_realSignature;
	private JLabel label_customPage;
	private JTextField input_customPage;
	private JLabel label_aspectAndPosition;
	private JButton button_togglePreview;
	private JComboBox select_signatureSize;
	private JComboBox select_signaturePosition;
	private JButton button_sign;
	private SigningLog signingLog;
	private SigningPreview preview;
	private JPanel LEFT_PANEL;
	private JPanel RIGHT_PANEL;
	private JButton button_preview;
	private JButton button_back;


	public PdfSigningView()
	{
		this.setLayout(new GridLayout(1, 2));
		
		this.LEFT_PANEL = new JPanel();
		this.RIGHT_PANEL = new JPanel();
		
		//=========================================||
		// LEFT PANEL
		//=========================================||
		LEFT_PANEL.setLayout(new MigLayout("", "[grow]", "[][69.00,top][top][grow]"));
		
		button_back = new JButton("<");
		button_back.setFont(new Font("Segoe Script", Font.BOLD, 10));
		button_back.setBorder(new EmptyBorder(0, 10, 0, 10));
		
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
		
		this.button_preview = new JButton("<<");
		
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
			//signingOptions.add(this.check_visibleReason, "cell 0 3");
			signingOptions.add(this.label_location, "cell 0 4");
			signingOptions.add(this.input_location, "cell 0 5,grow");
			//signingOptions.add(this.check_visibleLocation, "cell 0 5");
			signingOptions.add(this.label_signingMode, "cell 0 6");
			signingOptions.add(this.select_visibleSignature, "cell 0 7,grow");
			signingOptions.add(this.select_signingPage, "cell 0 7,grow");
			signingOptions.add(this.check_realSignature, "cell 0 7,grow");
			signingOptions.add(this.label_customPage, "cell 0 7");
			signingOptions.add(this.input_customPage, "cell 0 7,grow");
			signingOptions.add(this.label_aspectAndPosition, "flowx,cell 0 8");
			signingOptions.add(Box.createGlue(), "cell 0 8,grow");
			//signingOptions.add(button_preview, "cell 0 8");
			signingOptions.add(this.select_signatureSize, "flowx,cell 0 9");
			signingOptions.add(this.select_signaturePosition, "cell 0 9");
			signingOptions.add(Box.createGlue(), "cell 0 9,grow");
			signingOptions.add(this.button_sign, "cell 0 9");
			

			LEFT_PANEL.add(button_back, "cell 0 0");
			LEFT_PANEL.add(chooseFileAndCertificate, "cell 0 1,growx");
			LEFT_PANEL.add(signingOptions, "cell 0 2,growx");
			LEFT_PANEL.add(signingLog, "cell 0 3,grow");
		}
		
		
		//=========================================||
		// RIGHT PANEL
		//=========================================||
		RIGHT_PANEL.setLayout(new MigLayout("", "[354px,grow,fill]", "[106px,grow,fill]"));
		this.preview = new SigningPreview();
		SignaturePosition pos = (SignaturePosition) select_signaturePosition.getSelectedItem();
		preview.setDraggableSignatureFieldPosition((SignaturePosition) select_signaturePosition().getSelectedItem());
		RIGHT_PANEL.add(preview, "cell 0 0,alignx left,aligny top");
		
		this.add(LEFT_PANEL);
		this.add(RIGHT_PANEL);
		App.frame.setPreferredSize(new Dimension(App.frame.getWidth()*2, App.frame.getHeight()));
		App.frame.revalidate();
		App.frame.repaint();
		App.frame.pack();
		
		button_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				App.goToMainMenu();
				App.frame.setPreferredSize(new Dimension(App.frame.getWidth()/2, App.frame.getHeight()));
				App.frame.repaint();
				App.frame.pack();
			}
		});
	}


	@Override
	public SigningLog getLog() {
		return this.signingLog;
	}


	@Override
	public JButton button_chooseFile() {
		return this.chooseFileAndCertificate.button_chooseFile;
	}


	@Override
	public JTextField input_choosedFiles() {
		return this.chooseFileAndCertificate.input_choosedFiles;
	}


	@Override
	public JComboBox<Certificate> select_certificates() {
		return this.chooseFileAndCertificate.select_certificates;
	}


	@Override
	public JButton button_sign() {
		return this.button_sign;
	}


	@Override
	public JCheckBox check_visibleReason() {
		return this.check_visibleReason;
	}


	@Override
	public JCheckBox check_visibleSN() {
		return this.chooseFileAndCertificate.check_visibleSN;
	}


	@Override
	public JCheckBox check_visibleLocation() {
		return this.check_visibleLocation;
	}


	@Override
	public JCheckBox check_realSignature() {
		return this.check_realSignature;
	}


	@Override
	public JTextField input_reason() {
		return this.input_reason;
	}


	@Override
	public JTextField input_organization() {
		return this.input_organization;
	}


	@Override
	public JTextField input_location() {
		return this.input_location;
	}


	@Override
	public JTextField input_customPage() {
		return this.input_customPage;
	}


	@Override
	public JComboBox<String> select_visibleSignature() {
		return this.select_visibleSignature;
	}


	@Override
	public JComboBox<SigningPage> select_signingPage() {
		return this.select_signingPage;
	}


	@Override
	public JComboBox<SignatureSize> select_signatureSize() {
		return this.select_signatureSize;
	}


	@Override
	public JComboBox<SignaturePosition> select_signaturePosition() {
		return this.select_signaturePosition;
	}


	@Override
	public SigningPreview getPreview() {
		return this.preview;
	}
	
	@Override
	public JLabel label_serialNumber() {
		return this.chooseFileAndCertificate.label_serialNumber;
	}
}
