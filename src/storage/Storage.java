package storage;

import java.sql.*;
import java.util.*;

public class Storage {
	private static final String SQLITE_CLASS = "org.sqlite.JDBC";
	private static final String DB_PATH = "jdbc:sqlite:./database/data.db";

	private static final String TABLE_NAME = "CRAWLERTABLE";
	private static final String ID = "ID";
	private static final String URL = "URL";
	private static final String VISITED = "VISITED";

	private static final String INSERT_COMMAND = "INSERT INTO " + TABLE_NAME
			+ " (" + ID + "," + URL + "," + VISITED + ") ";
	private static final String RETRIEVE_TABLE = "SELECT * FROM " + TABLE_NAME
			+ ";";
	private static final String UPDATE_COMMAND = "UPDATE " + TABLE_NAME
			+ " set ";
	private static final String DELETE_COMMAND = "DELETE from " + TABLE_NAME + " where ";

	private static Statement stmt = null;
	private static Connection c = null;

	public static void main(String[] args){
		createTable();
		
		//URLStored urlStored = new URLStored(1, "fsdsafsad", true);
		//insertRowTable(urlStored);
		//retrieveTable();
		//deleteRowTable(urlStored);
		//updateRowTable(urlStored);
	}
	
	/*
	 * Create a table
	 */
	public static void createTable() {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" 
					+ ID + " INT PRIMARY KEY		NOT NULL," 
					+ URL + " 		  	 TEXT 	 	NOT NULL, " 
					+ VISITED + "    	 BOOLEAN	NOT NULL)";

			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Table created successfully");
	}

	/*
	 * Insert a row into table
	 */
	public static void insertRowTable(URLStored urlStored) {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			int id = urlStored.getId();
			String url = urlStored.getURL();
			boolean visited = urlStored.getVisited();

			/*String sql = INSERT_COMMAND + "VALUES (" + id + ", '" + url + "', '"
					+ visited + "' );";*/
			String sql = INSERT_COMMAND + "VALUES (1, sfsdafsad, false );";
			System.out.println("sql insert = " + sql);
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	/*
	 * Retrieve all rows in table
	 */
	public static ArrayList<URLStored> retrieveTable() {
		ArrayList<URLStored> listEntries = new ArrayList<URLStored>();

		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(RETRIEVE_TABLE);
			
			while (rs.next()) {
				int id = rs.getInt(ID);
				String url = rs.getString(URL);
				boolean visited = rs.getBoolean(VISITED);

				URLStored urlStored = new URLStored(id, url, visited);
				listEntries.add(urlStored);

				System.out.println("ID = " + id);
				System.out.println("URL = " + url);
				System.out.println("Visited = " + visited);
				System.out.println();
			}

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		System.out.println("Retrieve table done successfully");

		return listEntries;
	}

	/*
	 * Update new information for an existed row
	 */
	public static void updateRowTable(URLStored urlStored) {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			int id = urlStored.getId();
			String url = urlStored.getURL();
			boolean visited = urlStored.getVisited();

			stmt = c.createStatement();
			String sql = UPDATE_COMMAND;

			if (url != null) {
				sql += URL + " = " + url;
			}
			sql += " where " + ID + " = " + id;
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Update a row table successfully");
	}

	/*
	 * Delete a row in table
	 */
	public static void deleteRowTable(URLStored urlStored) {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			
			int id = urlStored.getId();

			String sql = DELETE_COMMAND + ID + " = " + id;
			stmt.executeUpdate(sql);
			c.commit();
			
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Delete a row table successfully");
	}

	public Queue<String> getURLSeeds() {
		return null;
	}
}
