package storage;

import java.sql.*;
import java.util.*;

public class Storage {
    private static final String SQLITE_CLASS = "org.sqlite.JDBC";
    private static final String DB_PATH = "jdbc:sqlite:./database/data.db";
    private static final String TEST_DB_PATH = "jdbc:sqlite:./database/test.db";

    private static final String TABLE_NAME = "Data";
    private static final String URL = "url";
    private static final String HTML = "html";
    private static final String LATENCY = "latency";
    
    private static final String INSERT_COMMAND = String.format(
            "INSERT INTO %s (%s, %s, %s) VALUES(?, ?, ?)",
            TABLE_NAME, URL, HTML, LATENCY
    );

    private static final String DROP_COMMAND = String.format(
            "DROP TABLE IF EXISTS %s",
            TABLE_NAME
    );

    private static final String CREATE_COMMAND = String.format(
            "CREATE TABLE IF NOT EXISTS %s ("
          + "   %s VARCHAR(255) PRIMARY KEY,"
          + "   %s TEXT NOT NULL,"
          + "   %s DOUBLE NOT NULL"
          + ");",
          TABLE_NAME, URL, HTML, LATENCY
    );
    
    private static final String SELECT_ALL_COMMAND = String.format(
            "SELECT * FROM %s",
            TABLE_NAME
    );

    private static final String SELECT_URL_COMMAND = String.format(
            "SELECT * FROM %s "
          + "WHERE %s = ?;",
          TABLE_NAME, URL
    );

    private static final String OPEN_DB_MSG = "Opened database successfully";
    private static final String CREATE_TABLE_MSG = "Table created successfully\n";
    private static final String DROP_TABLE_MSG = "Dropped table successfully\n";
    private static final String INSERT_ROW_MSG = "Inserted row into table successfully\n";

    private static final String RETRIEVE_TABLE_MSG = "Retrieved table successfully\n";
    private static final String RETRIEVE_ROW_MSG = "Retrieved row from table successfully\n";

    private static Connection connector;
    
    private Connection openDb(boolean test) throws Exception {
        Class.forName(SQLITE_CLASS);
        String path = test ? TEST_DB_PATH : DB_PATH;
        Connection c = DriverManager.getConnection(path);
        System.out.println(OPEN_DB_MSG);
        c.setAutoCommit(true);
        return c;
    }
    
    private void executeStatic(String sql) throws Exception {
        Statement stmt = connector.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }
    
    private CrawlerResult getURLStored(ResultSet rs) throws Exception {
        String url = rs.getString(URL);
        String html = rs.getString(HTML);
        double latency = rs.getDouble(LATENCY);
        CrawlerResult urlStored = new CrawlerResult(url, html, latency);
        return urlStored;
    }
    
    /*
     * Create a table
     */
    public void createTable() {
        try {
            executeStatic(CREATE_COMMAND);
            System.out.println(CREATE_TABLE_MSG);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /*
     * Drop the table
     */
    public void dropTable() {
        try {
            executeStatic(DROP_COMMAND);
            System.out.println(DROP_TABLE_MSG);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /*
     * Insert a row into table
     */
    public void insertRowTable(CrawlerResult entry) {
        try {
            PreparedStatement stmt = connector.prepareStatement(INSERT_COMMAND);
            stmt.setString(1, entry.url);
            stmt.setString(2, entry.html);
            stmt.setDouble(3, entry.latency);
            stmt.executeUpdate();
            stmt.close();
            System.out.println(INSERT_ROW_MSG);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /*
     * Retrieve all rows in table
     */
    public ArrayList<CrawlerResult> retrieveTable() {
        ArrayList<CrawlerResult> listEntries = new ArrayList<CrawlerResult>();
        try {
            Statement stmt = connector.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_ALL_COMMAND);
            while (rs.next()) {
                listEntries.add(getURLStored(rs));
            }
            rs.close();
            stmt.close();
            System.out.println(RETRIEVE_TABLE_MSG);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return listEntries;
    }

    /*
     * Retrieve a row in table
     */
    public CrawlerResult retrieveRowTable(String url) {
        CrawlerResult res = null;
        try {
            PreparedStatement stmt = connector.prepareStatement(SELECT_URL_COMMAND);
            stmt.setString(1, url);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                res = getURLStored(rs);
            }
            rs.close();
            stmt.close();
            System.out.println(RETRIEVE_ROW_MSG);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return res;
    }
    
    public Storage(boolean test) {
        try {
            connector = openDb(test);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public Storage() {
        this(false);
    }
}
