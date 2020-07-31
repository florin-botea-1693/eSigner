package main;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.PDFSigningController;
import model.PDFSignerModel;
import model.certificates.MSCAPICertificatesHolder;
import model.signing.PDFSigningOptions;
import model.signing.visible.SignaturePosition;
import view.PDFSigningView;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class App {

	private JPanel view;
	private Object model;
	private Object controller;
	
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
		frame.setBounds(100, 100, 450, 400);
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
	public void goToPDFSign() {
		MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
		//AppSettings settings = AppSettings.getInstance();
		PDFSigningOptions signingOptions = new PDFSigningOptions();
		//signingOptions.loadFromAppSettings(settings);
		
		PDFSignerModel model = new PDFSignerModel(certificatesHolder, signingOptions);
		PDFSigningView view = new PDFSigningView(model);
		controller = new PDFSigningController(model, view);
		// view listen to model
		// model listen to controller
		// controller listen to view
		//model.addListener(view); // PDFSigningView
		//controller.addListener(model); // --- controller va avea model in el, deci NU!
		//view.addListener(controller);

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
