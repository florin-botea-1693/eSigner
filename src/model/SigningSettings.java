package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.signing.visible.options.SignaturePosition;
import model.signing.visible.options.SignatureSize;
import model.signing.visible.options.SigningPage;

public class SigningSettings {
	private static SigningSettings instance;
	
	private Connection conn = null;

	//=============================||
	// SINGLETHON
	//=============================||
	private SigningSettings() {
		/*
		try (Statement stmt = connect().createStatement())
		{
			stmt.execute("CREATE TABLE IF NOT EXISTS certificates_signing_setting("
					+ "serial_number bigint PRIMARY KEY NOT NULL,"
					+ "is_visible_sn varchar check(is_visible_sn in('true', 'false')) default 'false',"
					+ "signing_reason varchar,"
					+ "is_visible_reason varchar check(is_visible_reason in('true', 'false')) default 'false',"
					+ "signing_location varchar,"
					+ "is_visible_location varchar check(is_visible_location in('true', 'false')) default 'false',"
					+ "is_visible_signature varchar check(is_visible_signature in('true', 'false')) default 'false',"
					+ "signing_page  varchar check(signing_page in('FIRST_PAGE', 'LAST_PAGE')) default 'FIRST_PAGE',"
					+ "is_real_signature varchar check(is_real_signature in('true', 'false')) default 'false',"
					+ "custom_signing_page int default null,"
					+ "signature_size varchar check(signature_size in('SMALL', 'MEDIUM', 'LARGE')) default 'MEDIUM',"
					+ "signature_position varchar check(signature_position in('TOP_LEFT', 'TOP_CENTER', 'TOP_RIGHT')) default 'TOP_RIGHT',"
					+ "validation_token varchar default null,"
					+ "validation_message varchar default null"
					+ ");");
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        */
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try (Statement stmt = connect().createStatement())
		{
			stmt.execute("CREATE TABLE IF NOT EXISTS signing_settings("
					+ "signing_settings tinyint PRIMARY KEY NOT NULL check(signing_settings = 1),"
					+ "is_visible_sn varchar check(is_visible_sn in('true', 'false')) default 'false',"
					+ "signing_reason varchar,"
					+ "is_visible_reason varchar check(is_visible_reason in('true', 'false')) default 'false',"
					+ "signing_location varchar,"
					+ "is_visible_location varchar check(is_visible_location in('true', 'false')) default 'false',"
					+ "is_visible_signature varchar check(is_visible_signature in('true', 'false')) default 'false',"
					+ "signing_page  varchar check(signing_page in('FIRST_PAGE', 'LAST_PAGE')) default 'FIRST_PAGE',"
					+ "is_real_signature varchar check(is_real_signature in('true', 'false')) default 'false',"
					+ "custom_signing_page int default null,"
					+ "signature_size varchar check(signature_size in('SMALL', 'MEDIUM', 'LARGE')) default 'MEDIUM',"
					+ "signature_position varchar check(signature_position in('TOP_LEFT', 'TOP_CENTER', 'TOP_RIGHT')) default 'TOP_RIGHT'"
					+ ");");
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public static SigningSettings getInstance() throws IOException {
		if (SigningSettings.instance == null) {
			SigningSettings.instance = new SigningSettings();
		}
		return SigningSettings.instance;
	}
	//========================\\
	
    private Connection connect() {
        if (conn != null)
        	return conn;
        try {
        	conn = DriverManager.getConnection("jdbc:sqlite:app.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    //=================================||
    // QUERY
    //=================================||
    
    public SigningSettingsRecord getSigningSettings() {
		String sql = "SELECT * from signing_settings WHERE signing_settings = 1";
		SigningSettingsRecord record = new SigningSettingsRecord();
		try (Statement stmt = connect().createStatement(); ResultSet s = stmt.executeQuery(sql))
		{
			if (!s.next()) {
				setSigningSettings();
				return getSigningSettings();
			}
			record.signing_reason = s.getString("signing_reason");
			record.is_visible_reason = s.getBoolean("is_visible_reason");
			record.signing_location = s.getString("signing_location");
			record.is_visible_location = s.getBoolean("is_visible_location");
			record.is_visible_sn = s.getBoolean("is_visible_sn");
			record.signature_size = SignatureSize.valueOf(s.getString("signature_size"));
			record.signature_position = SignaturePosition.valueOf(s.getString("signature_position"));
			record.signing_page = SigningPage.valueOf(s.getString("signing_page"));
			record.is_real_signature = s.getBoolean("is_real_signature");
			record.custom_signing_page = s.getInt("custom_signing_page");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return record;
    }
    
	private void setSigningSettings() {
		String statement = "INSERT into signing_settings(signing_settings) values (1);";
	    try (Statement stmt = connect().createStatement())
	    {
	    	stmt.executeUpdate(statement);
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}
	/*
	public SigningSettingsRecord getCertificateSigningSettings(BigInteger sn) {
		String sql = "SELECT * from certificates_signing_setting WHERE serial_number = " + sn;
		SigningSettingsRecord record = new SigningSettingsRecord();
		try (Statement stmt = connect().createStatement(); ResultSet s = stmt.executeQuery(sql))
		{
			if (!s.next()) {
				setCertificateSigningSettings(sn);
				return getCertificateSigningSettings(sn);
			}
			record.signing_reason = s.getString("signing_reason");
			record.is_visible_reason = s.getBoolean("is_visible_reason");
			record.signing_location = s.getString("signing_location");
			record.is_visible_location = s.getBoolean("is_visible_location");
			record.is_visible_sn = s.getBoolean("is_visible_sn");
			record.signature_size = SignatureSize.valueOf(s.getString("signature_size"));
			record.signature_position = SignaturePosition.valueOf(s.getString("signature_position"));
			record.signing_page = SigningPage.valueOf(s.getString("signing_page"));
			record.is_real_signature = s.getBoolean("is_real_signature");
			record.custom_signing_page = s.getInt("custom_signing_page");
			record.validation_token = s.getString("validation_token");
			record.validation_message = s.getString("validation_message");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return record;
	}
	
	private void setCertificateSigningSettings(BigInteger sn) {System.out.println(3);
		String statement = "INSERT into certificates_signing_setting(serial_number) values ("+sn+");";
        try (Statement stmt = connect().createStatement())
        {
        	stmt.executeUpdate(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void setValidationResult(BigInteger serialNumber, String token, String string) {
		String statement = "UPDATE certificates_signing_setting SET validation_token=?, validation_message=? WHERE serial_number=?";
		System.out.println(statement);
        try (PreparedStatement pstmt = connect().prepareStatement(statement))
        {
        	pstmt.setString(1, token);
        	pstmt.setString(2, string);
        	pstmt.setString(3, serialNumber.toString());
        	pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	}
	*/
}
