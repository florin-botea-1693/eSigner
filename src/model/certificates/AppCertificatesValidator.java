package model.certificates;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import utils.AES;

public class AppCertificatesValidator {
	
	private static AppCertificatesValidator instance = null;
	
	private Connection conn = null;
	
	public static AppCertificatesValidator getInstance() {
		if (instance == null)
			instance = new AppCertificatesValidator();
		return instance;
	}
	
	private AppCertificatesValidator() {
		try (Statement stmt = connect().createStatement())
		{
			stmt.execute("CREATE TABLE IF NOT EXISTS certificates("
					+ "serial_number bigint PRIMARY KEY NOT NULL,"
					+ "can_sign tinyint check(can_sign in(0, 1)) default 0,"
					+ "time_token varchar,"
					+ "message varchar"
					+ ");");
		}
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
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
    
    public ValidationResult validate(Certificate cert) {
		String sql = "SELECT * from certificates WHERE serial_number = " + cert.getPrivateKey().getCertificate().getSerialNumber().toString();
		ValidationResult result = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try (Statement stmt = connect().createStatement(); ResultSet r = stmt.executeQuery(sql))
		{
			if (!r.next()) {
				result = validateOnServer(cert);
			}
			else {
				if (r.getString("time_token") != null) {
					long expiresAt = Long.parseLong(AES.decrypt(r.getString("time_token"), "fooBar"));
					if (expiresAt > Instant.now().getEpochSecond()) {
						result = new ValidationResult(r.getBoolean("can_sign"), r.getString("message"));
					} else {
						result = validateOnServer(cert);
					}
				}
				else {
					result = validateOnServer(cert);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = new ValidationResult(false, e.getMessage());
		}
		return result;
    }
    
    private ValidationResult validateOnServer(Certificate cert) { System.out.println("validating on server");
    	ValidationResult vr = null;
        OkHttpClient httpClient = new OkHttpClient();
        Builder request = new Request.Builder().url("https://test-digisign.000webhostapp.com?certificate=" + cert.getPrivateKey().getCertificate().getSerialNumber());
        //request.addHeader(key, value);
		Request req = request.build();
		try {
			Response res = httpClient.newCall(req).execute();
			String jsonString = res.body().string();
			JSONObject resJson = new JSONObject(jsonString);
			boolean can_sign = resJson.getBoolean("can_sign");
			String message = resJson.getString("message");
			long expires_at = resJson.getLong("expires_at");
			String token = AES.encrypt(String.valueOf(expires_at), "fooBar");
			updateToDB(cert.getPrivateKey().getCertificate().getSerialNumber(), can_sign, message, token);
			vr = new ValidationResult(can_sign, message);
		} catch (IOException e) {
			vr = new ValidationResult(false, e.getMessage());
			e.printStackTrace();
		}
		return vr;
    }
	
	private void updateToDB(BigInteger serialNumber, boolean can_sign, String message, String token) {
		String statement = "DELETE from certificates where serial_number = " + serialNumber.toString();
        try (Statement stmt = connect().createStatement())
        {
        	stmt.executeUpdate(statement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		statement = "INSERT into certificates(serial_number, can_sign, time_token, message) values(" + serialNumber + ", ?, ?, ?)";
        try (PreparedStatement stmt = connect().prepareStatement(statement))
        {
        	stmt.setInt(1, can_sign ? 1 : 0);
        	stmt.setString(2, token);
        	stmt.setString(3, message);
        	stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
}
