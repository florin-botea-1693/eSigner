package main;

import java.awt.EventQueue;
import java.awt.GridLayout;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.CadesSigningController;
import controller.PdfSigningController;
import model.CadesSigningModel;
import model.PdfSigningModel;
import model.certificates.MSCAPICertificatesHolder;
import tests.CPTest;
import view.CadesSigningView;
import view.MainMenuView;
import view.PdfSigningView;

public class App {
	
	public static final JFrame frame = new JFrame("eSigner - v.1.0");
	private JFrame _frame;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws NoSuchAlgorithmException 
	 * @throws NoSuchProviderException 
	 * @throws UnrecoverableKeyException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, CertificateException, IOException, NoSuchProviderException, UnrecoverableKeyException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
		catch (UnsupportedLookAndFeelException e) {e.printStackTrace();}
		catch (ClassNotFoundException e) {e.printStackTrace();}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		/*
		PrintStream out = new PrintStream(
		new FileOutputStream("output.txt", true), true);
		System.setOut(out);
		System.setErr(out);
		*/
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App app = new App();
					App.frame.setVisible(true);
					// case 1
					App.goToMainMenu();
					//app.goToPDFSign();
					//app.goToCadesSign();
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
		App.frame.setBounds(100, 100, 500, 500);
		App.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// MENU BAR
		JMenuBar menuBar = new JMenuBar();
		App.frame.setJMenuBar(menuBar);

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

	
	public static void goToMainMenu() {
		MainMenuView view = new MainMenuView();

		frame.setContentPane(view);
		//frame.repaint();
	}
	
	public static void goToPDFSign() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
	
		PdfSigningModel model = new PdfSigningModel(certificatesHolder);
		PdfSigningView view = new PdfSigningView();// remove argument, voi avea o metoda call initial in registet ce va pune un model-view in view
		new PdfSigningController(model, view);

		/*
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(1, 2));
		contentPane.add(view);
		
		frame.setContentPane(contentPane);
		*/
		//frame.setLayout(new GridLayout(1, 2)); // <--
		frame.setContentPane(view); //frame.setContentPane(view);
		//frame.add(new JPanel());
		
		frame.repaint();
		frame.setVisible(true);
	}
	
	public static void goToCadesSign() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MSCAPICertificatesHolder certificatesHolder = new MSCAPICertificatesHolder();
	
		CadesSigningModel model = new CadesSigningModel(certificatesHolder);
		CadesSigningView view = new CadesSigningView();// remove argument, voi avea o metoda call initial in registet ce va pune un model-view in view
		new CadesSigningController(model, view);

		/*
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(1, 2));
		contentPane.add(view);
		
		frame.setContentPane(contentPane);
		*/
		//frame.setLayout(new GridLayout(1, 2)); // <--
		
		frame.setContentPane(view); //frame.setContentPane(view);
		//frame.add(new JPanel());
		
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
