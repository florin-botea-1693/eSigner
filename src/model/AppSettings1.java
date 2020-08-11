package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AppSettings1 {// rename it SigningSettings
	private static AppSettings1 instance;

	//=============================||
	// SINGLETHON
	//=============================||
	private AppSettings1() {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:app.db");
			Statement stmt = conn.createStatement())
		{
			stmt.execute("CREATE TABLE IF NOT EXISTS certificates_signing_setting("
					+ "serial_number bigint PRIMARY KEY NOT NULL,"
					+ "is_visible_sn enum('true','false') default 'false',"
					+ "signing_reason varchar,"
					+ "is_visible_reason enum('true','false') default 'false',"
					+ ""
					+ ")");
					private String[] isVisibleSN;
	private String[] signingReason;
	private String[] isVisibleReason;
	private String[] signingLocation;
	private String[] isVisibleLocation;
	private String[] isVisibleSignature;
	private String[] signingPage;
	private String[] isRealSignature;
	private String[] customSigningPage;
	private String[] signatureSize;
	private String[] signaturePosition;
	private String[] validationToken
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	}
	
	public static AppSettings1 getInstance() throws IOException {
		if (AppSettings1.instance == null) {
			AppSettings1.instance = new AppSettings1();
		}
		return AppSettings1.instance;
	}
	//========================\\
	
	public getCertificateSigningSettings(bigint sn) {
		//
	}
	
	public void setCertificateSigningSettings(un set de parametri) {
		//
	}
}
