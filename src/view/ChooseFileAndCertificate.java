package view;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class ChooseFileAndCertificate extends JPanel
{
	
	public final JTextField input_choosedFiles;
	public final JButton button_chooseFile;
	public final JComboBox select_certificates;
	public final JLabel label_serialNumber;
	public final JCheckBox check_visibleSN;
	

	public ChooseFileAndCertificate() 
	{
		this.setLayout(new MigLayout("", "[grow]", "[][][]"));
		
		this.input_choosedFiles = new JTextField();
		
		this.input_choosedFiles.setColumns(10);
		
		this.input_choosedFiles.setEnabled(false);
		
		this.button_chooseFile = new JButton("File(s) to sign");
		
		this.select_certificates = new JComboBox();//signingModel.getCertificates().toArray()
		
		this.label_serialNumber = new JLabel("Serial number");
		
		this.check_visibleSN = new JCheckBox("Visible SN");
		
		
		this.add(input_choosedFiles, "cell 0 0,growx");
		this.add(button_chooseFile, "cell 0 0");
		this.add(select_certificates, "cell 0 1,growx");
		this.add(label_serialNumber, "cell 0 2");
		this.add(Box.createGlue(), "cell 0 2,grow");
		this.add(this.check_visibleSN, "cell 0 2");
	}
}
