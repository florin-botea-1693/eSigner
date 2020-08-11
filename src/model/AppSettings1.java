package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class AppSettings1 {
	private static AppSettings1 instance;

	//=============================||
	// SINGLETHON
	//=============================||
	private AppSettings1() {
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:app.db");
			Statement stmt = conn.createStatement())
		{
			stmt.execute("CREATE TABLE IF NOT EXISTS certificates("
					+ "serial_number bigint PRIMARY KEY NOT NULL"
					+ ")");
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
}
