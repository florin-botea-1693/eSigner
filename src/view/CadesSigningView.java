package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import main.App;
import model.certificates.Certificate;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import net.miginfocom.swing.MigLayout;
import view.components.ChooseFileAndCertificate;
import view.components.SigningLog;

public class CadesSigningView extends JPanel implements ICadesSigningView
{
	private ChooseFileAndCertificate chooseFileAndCertificate;
	private JPanel LEFT_PANEL;
	private JPanel RIGHT_PANEL;
	private JButton button_sign;
	private SigningLog signingLog;
	private JComboBox select_signingExtension;
	private JLabel label_signingExtension;
	private JLabel label_signaturePackaging;
	private JComboBox select_digestAlgorithm;
	private JLabel label_digestAlgorithm;
	private JComboBox select_signaturePackaging;
	private JButton button_back;
	
	public CadesSigningView() 
	{
		this.setLayout(new GridLayout(1, 2));
		
		this.LEFT_PANEL = new JPanel();
		this.RIGHT_PANEL = new JPanel();
		
		//=========================================||
		// LEFT PANEL
		//=========================================||
		LEFT_PANEL.setLayout(new MigLayout("", "[grow]", "[][69.00,top][top][grow]"));
		
		this.chooseFileAndCertificate = new ChooseFileAndCertificate();
		chooseFileAndCertificate.check_visibleSN.setVisible(false);
		
		this.signingLog = new SigningLog();
		
		button_back = new JButton("<");
		button_back.setFont(new Font("Segoe Script", Font.BOLD, 10));
		button_back.setBorder(new EmptyBorder(0, 10, 0, 10));
		
		label_signingExtension = new JLabel("Signing Extension");
		
		label_signaturePackaging = new JLabel("Signature Packaging");
		
		label_digestAlgorithm = new JLabel("Digest Algorithm");
		
		// build-left-panel
		{
			JPanel signingOptions = new JPanel();
			signingOptions.setLayout(new MigLayout("", "[grow][grow]", "[][][][][][]"));
			signingOptions.add(label_signingExtension, "flowx,cell 0 0");
			
			select_signingExtension = new JComboBox(new String[] {"p7m", "p7s"});
			signingOptions.add(select_signingExtension, "cell 1 0,growx");
			signingOptions.add(label_signaturePackaging, "flowx,cell 0 1");
			
			select_signaturePackaging = new JComboBox(new String[] {SignaturePackaging.ENVELOPING.toString(), SignaturePackaging.DETACHED.toString()});
			signingOptions.add(select_signaturePackaging, "cell 1 1,growx");
			signingOptions.add(label_digestAlgorithm, "flowx,cell 0 2");
			
			select_digestAlgorithm = new JComboBox(new String[] {DigestAlgorithm.SHA256.toString(), DigestAlgorithm.SHA1.toString()});
			signingOptions.add(select_digestAlgorithm, "cell 1 2,growx");
			signingOptions.add(Box.createGlue(), "cell 0 5,grow");
			
			LEFT_PANEL.add(button_back, "cell 0 0");
			LEFT_PANEL.add(chooseFileAndCertificate, "cell 0 1,growx");
			LEFT_PANEL.add(signingOptions, "cell 0 2,growx");
			
			this.button_sign = new JButton("Sign");
			signingOptions.add(this.button_sign, "cell 1 5,alignx right");
			LEFT_PANEL.add(signingLog, "cell 0 3,grow");
		}
		
		
		//=========================================||
		// RIGHT PANEL
		//=========================================||
		RIGHT_PANEL.setLayout(new MigLayout("", "[354px,grow,fill]", "[106px,grow,fill]"));
		//this.preview = new SigningPreview();
		//RIGHT_PANEL.add(preview, "cell 0 0,alignx left,aligny top");
		
		this.add(LEFT_PANEL);
		//this.add(RIGHT_PANEL);
		App.frame.setPreferredSize(new Dimension(App.frame.getWidth(), App.frame.getHeight()));
		App.frame.revalidate();
		App.frame.repaint();
		App.frame.pack();
		
		button_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				App.goToMainMenu();
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
	public JComboBox select_signingExtension() {
		return this.select_signingExtension;
	}

	@Override
	public JComboBox select_digestAlgorithm() {
		return this.select_digestAlgorithm;
	}

	@Override
	public JComboBox select_signaturePackaging() {
		return this.select_signaturePackaging;
	}

	@Override
	public JLabel label_serialNumber() {
		return this.chooseFileAndCertificate.label_serialNumber;
	}

	@Override
	public JComponent spinner_loadingCertificates() {
		return this.chooseFileAndCertificate.spinner_loadingCertificates;
	}

}
