package view;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;

public class SigningMessage2 extends JInternalFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SigningMessage2 frame = new SigningMessage2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SigningMessage2() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

	}

}
