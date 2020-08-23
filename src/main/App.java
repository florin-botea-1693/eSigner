package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.PDFSigningController;
import model.PDFSignerModel;
import model.certificates.MSCAPICertificatesHolder;
import view.PDFSigningView;

public class App {
	
	private JFrame frame;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchProviderException 
	 * @throws UnrecoverableKeyException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, CertificateException, IOException, NoSuchProviderException, UnrecoverableKeyException {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
		catch (UnsupportedLookAndFeelException e) {e.printStackTrace();}
		catch (ClassNotFoundException e) {e.printStackTrace();}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}

		PrintStream out = new PrintStream(
		new FileOutputStream("output.txt", true), true);
		System.setOut(out);
		System.setErr(out);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App app = new App();
					app.frame.setVisible(true);
					// case 1
					app.goToPDFSign();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// MENU BAR
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		// MENU
		JMenu settings = new JMenu("Settings");
		menuBar.add(settings);
		// MENU ITEM
		JMenuItem generalSettingsBtn = new JMenuItem("General");
		generalSettingsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToGeneralSettings();
			}
		});
		settings.add(generalSettingsBtn);
	}
/*
	public void goToMainMenu() {
		view = new MainMenuView();
		
		((MainMenuView) view).getPDFSignBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToPDFSign();
			}
		});
		
		frame.setContentPane((Container) view);
		frame.repaint();
	}
	*/
	public void goToPDFSign() throws IOException {
		MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
	
		PDFSignerModel model = new PDFSignerModel(certificatesHolder);
		PDFSigningView view = new PDFSigningView(frame);// remove argument, voi avea o metoda call initial in registet ce va pune un model-view in view
		new PDFSigningController(model, view);

		frame.setContentPane(view);
		frame.repaint();
		frame.setVisible(true);
	}
	
	public void goToGeneralSettings() {
		/*
		model = new AppSettings("appSettings");
		view = new AppSettingsView(model);
		controller = new AppSettingsController((AppSettings) model, (AppSettingsView) view);

		frame.setContentPane(view);
		frame.repaint();
		frame.setVisible(true);
		*/
	}
}
