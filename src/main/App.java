package main;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
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
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.json.JSONObject;

import controller.PDFSigningController;
import model.AppSettings;
import model.PDFSignerModel;
import model.certificates.Certificate;
import model.certificates.MSCAPICertificatesHolder;
import model.signing.PDFSigningOptions;
import model.signing.visible.SignaturePosition;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import view.PDFSigningView;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
		AppSettings settings = AppSettings.getInstance();
	
		PDFSignerModel model = new PDFSignerModel(certificatesHolder, settings);
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

	public static void validate(Certificate selectedCertificate) {
		if (selectedCertificate.isValidatedOnMyServer())
			return;
		
        OkHttpClient httpClient = new OkHttpClient();
        Builder request = new Request.Builder().url("https://test-digisign.000webhostapp.com?certificate=" + selectedCertificate.getPrivateKey().getCertificate().getSerialNumber());
        //request.addHeader(key, value);
		Request req = request.build();
		try {
			Response res = httpClient.newCall(req).execute();
			JSONObject resJson = new JSONObject(res.body().string());
			selectedCertificate.setValidatedOnMyServer(true);
			selectedCertificate.setCanUseMyApp(resJson.getBoolean("can_sign"));
			selectedCertificate.setValidationOnMyServerResultMessage(resJson.getString("validation_message"));
		} catch (IOException e) {
			selectedCertificate.setValidationOnMyServerResultMessage(e.getMessage() + ". Do you have an active internet connection?");
		}
	}
}
