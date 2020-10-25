package view;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import model.certificates.Certificate;
import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;
import view.components.SigningPreview;

public interface IPdfSigningView  extends ISigningView
{

	JCheckBox check_visibleReason();

	JCheckBox check_visibleSN();

	JCheckBox check_visibleLocation();

	JCheckBox check_realSignature();

	JTextField input_reason();

	JTextField input_organization();

	JTextField input_location();

	JTextField input_customPage();

	JComboBox<String> select_visibleSignature();

	JComboBox<SigningPage> select_signingPage();

	JComboBox<SignatureSize> select_signatureSize();

	JComboBox<SignaturePosition> select_signaturePosition();

	SigningPreview getPreview();

}
