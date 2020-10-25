package view;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import main.App;

public class MainMenuView extends JPanel 
{

	private JLabel label_logo;
	private JButton button_pdfSign;
	private JButton button_cadesSign;
	
	public MainMenuView() 
	{
		label_logo = new JLabel("eSigner");
		label_logo.setHorizontalAlignment(SwingConstants.CENTER);
		label_logo.setFont(new Font("Ink Free", Font.BOLD, 32));

		button_pdfSign = new JButton("PDF Sign");
		button_pdfSign.setFont(new Font("Calibri", Font.BOLD, 20));
		
		button_cadesSign = new JButton("CAdES Sign (p7m/p7s)");
		button_cadesSign.setFont(new Font("Calibri", Font.BOLD, 20));
		
		setLayout(new MigLayout("", "[grow]", "[][][]"));
		add(label_logo, "cell 0 0,alignx center,aligny center");
		add(button_pdfSign, "cell 0 1,growx,aligny center");
		add(button_cadesSign, "cell 0 2,growx,aligny center");
		
		button_pdfSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					App.goToPDFSign();
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		button_cadesSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					App.goToCadesSign();
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
