package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.certificates.Certificate;
import view.components.SigningLog;

public interface ISigningView 
{
	public void setOpaque(boolean b);
	public SigningLog getLog();
	public JButton button_chooseFile();
	public JTextField input_choosedFiles();
	public JComboBox<Certificate> select_certificates();
	public JButton button_sign();
	public JLabel label_serialNumber();
	public JComponent spinner_loadingCertificates();
}
