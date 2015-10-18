package storage;

import java.sql.*;

public class Storage {
	private static final String SQLITE_CLASS = "org.sqlite.JDBC";
	private static final String DB_PATH = "jdbc:sqlite:./database/data.db";
	
	public static void main(String[] args) {
		Connection c = null;
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Opened database successfully");
	}
}
