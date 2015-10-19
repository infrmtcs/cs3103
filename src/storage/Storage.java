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
	private static final String DROP_COMMAND = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
	private static final String RETRIEVE_TABLE_COMMAND = "SELECT * FROM "
			+ TABLE_NAME;
	private static final String DELETE_TABLE_COMMAND = "DELETE FROM " + TABLE_NAME;

	private static final String OPEN_DB_MSG = "Opened database successfully";
	private static final String CREATE_TABLE_MSG = "Table created successfully\n";
	private static final String DROP_TABLE_MSG = "Dropped table successfully\n";
	private static final String INSERT_ROW_MSG = "Inserted row into table successfully\n";
	private static final String UPDATE_ROW_MSG = "Updated row table successfully\n";

	private static final String RETRIEVE_TABLE_MSG = "Retrieved table successfully\n";
	private static final String RETRIEVE_ROW_MSG = "Retrieved row from table successfully\n";

	private static final String DELETE_TABLE_MSG = "Deleted table successfully\n";
	private static final String DELETE_ROW_MSG = "Delete a row table successfully\n";

	private static Statement stmt = null;
	private static Connection c = null;

	/*
	 * Create a table
	 */
	public static void createTable() {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			System.out.println(OPEN_DB_MSG);

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID
					+ " INT PRIMARY KEY		NOT NULL," + URL
					+ " 		  	 TEXT 	 	NOT NULL, " + VISITED
					+ "    	 INT		NOT NULL)";

			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println(CREATE_TABLE_MSG);
	}

	/*
	 * Drop the table
	 */
	public static void dropTable() {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			System.out.println(OPEN_DB_MSG);

			stmt = c.createStatement();
			String sql = DROP_COMMAND;

			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println(DROP_TABLE_MSG);
	}

	/*
	 * Insert a row into table
	 */
	public static void insertRowTable(URLStored urlStored) {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println(OPEN_DB_MSG);

			int id = urlStored.getId();
			String url = urlStored.getURL();
			int visited = urlStored.getVisited();

			stmt = c.createStatement();
			String sql = INSERT_COMMAND + "VALUES (" + id + ", \"" + url
					+ "\", " + visited + " );";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println(INSERT_ROW_MSG);
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
			System.out.println(OPEN_DB_MSG);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(RETRIEVE_TABLE_COMMAND);

			while (rs.next()) {
				int id = rs.getInt(ID);
				String url = rs.getString(URL);
				int visited = rs.getInt(VISITED);

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

		System.out.println(RETRIEVE_TABLE_MSG);

		return listEntries;
	}

	/*
	 * Retrieve a row in table
	 */
	public static URLStored retrieveRowTable(URLStored urlStored) {
		URLStored rowTable = new URLStored();

		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println(OPEN_DB_MSG);

			stmt = c.createStatement();
			int id = urlStored.getId();
			String command = RETRIEVE_TABLE_COMMAND + " WHERE " + ID + "=" + id
					+ ";";
			ResultSet rs = stmt.executeQuery(command);

			String url = rs.getString(URL);
			int visited = rs.getInt(VISITED);
			rowTable = new URLStored(id, url, visited);

			System.out.println("ID = " + id);
			System.out.println("URL = " + url);
			System.out.println("Visited = " + visited);
			System.out.println();

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		System.out.println(RETRIEVE_ROW_MSG);

		return rowTable;
	}

	/*
	 * Update new information for an existed row
	 */
	public static void updateRowTable(URLStored urlStored) {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println(OPEN_DB_MSG);

			int id = urlStored.getId();
			String url = urlStored.getURL();
			int visited = urlStored.getVisited();

			stmt = c.createStatement();
			String sql = "UPDATE " + TABLE_NAME + " SET " + URL + "= \"" + url
					+ "\", " + VISITED + "=" + visited + " WHERE " + ID + "="
					+ id + ";";

			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println(UPDATE_ROW_MSG);
	}

	/*
	 * Delete a row in table
	 */
	public static void deleteRowTable(URLStored urlStored) {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println(OPEN_DB_MSG);

			int id = urlStored.getId();

			stmt = c.createStatement();
			String sql = DELETE_TABLE_COMMAND + " WHERE " + ID + "="
					+ id + ";";

			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println(DELETE_ROW_MSG);
	}

	/*
	 * Delete all rows in table
	 */
	public static void deleteTable() {
		try {
			Class.forName(SQLITE_CLASS);
			c = DriverManager.getConnection(DB_PATH);
			c.setAutoCommit(false);
			System.out.println(OPEN_DB_MSG);

			stmt = c.createStatement();
			String sql = DELETE_TABLE_COMMAND;
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println(DELETE_TABLE_MSG);
	}

	public Queue<String> getURLSeeds() {
		return null;
	}
}
