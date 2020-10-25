package view;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

public interface ICadesSigningView extends ISigningView
{

	JComboBox select_signingExtension();

	JComboBox select_digestAlgorithm();

	JComboBox select_signaturePackaging();



	

	

}
