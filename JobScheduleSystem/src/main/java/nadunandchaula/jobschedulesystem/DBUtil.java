package nadunandchaula.jobschedulesystem;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
    private static final String DEFAULT_DB_NAME = "vehicalapplicationdb";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static String DB_NAME = DEFAULT_DB_NAME;
    private static String USER = DEFAULT_USER;
    private static String PASSWORD = DEFAULT_PASSWORD;

    static {
        // Load overrides from resources/db.properties if present
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                DB_NAME = p.getProperty("db.name", DB_NAME);
                USER = p.getProperty("db.user", USER);
                PASSWORD = p.getProperty("db.password", PASSWORD);
            }
        } catch (IOException e) {
            System.err.println("Could not load db.properties, using defaults: " + e.getMessage());
        }
    }

    private static final String URL_DB = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String URL_SERVER = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // SQL to create the two tables if they don't exist (matching vehicalapplicationdb.sql)
    private static final String CREATE_JOBTABLE = "CREATE TABLE IF NOT EXISTS jobtable (" +
            "ID INT(11) NOT NULL AUTO_INCREMENT, " +
            "Customer_Name VARCHAR(100) NOT NULL, " +
            "Customer_Mail VARCHAR(100) NOT NULL, " +
            "Phone VARCHAR(100) NOT NULL, " +
            "Times VARCHAR(100) NOT NULL, " +
            "VehivalNo VARCHAR(20) NOT NULL, " +
            "Date VARCHAR(50) NOT NULL, " +
            "PRIMARY KEY (ID)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";

    private static final String CREATE_LOGIN = "CREATE TABLE IF NOT EXISTS login (" +
            "Name VARCHAR(50) NOT NULL, " +
            "ID INT(50) NOT NULL, " +
            "Password VARCHAR(50) NOT NULL, " +
            "Position VARCHAR(50) NOT NULL, " +
            "Email VARCHAR(100) NOT NULL" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";

    public static Connection getConnection() {
        try {
            // Try connecting directly to the DB
            return DriverManager.getConnection(URL_DB, USER, PASSWORD);
        } catch (SQLException e) {
            String msg = e.getMessage();
            System.err.println("Initial DB connection error: " + msg);
            // If database doesn't exist, attempt to create it
            if (msg != null && msg.toLowerCase().contains("unknown database")) {
                System.out.println("Database '" + DB_NAME + "' not found — attempting to create it.");
                try (Connection serverConn = DriverManager.getConnection(URL_SERVER, USER, PASSWORD);
                     Statement stmt = serverConn.createStatement()) {
                    // Create the database if not exists
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;");
                    // Create tables in the newly created database
                    stmt.executeUpdate("USE " + DB_NAME + ";");
                    stmt.executeUpdate(CREATE_JOBTABLE);
                    stmt.executeUpdate(CREATE_LOGIN);
                    System.out.println("Database and tables created (or already existed). Trying to connect to DB.");
                    return DriverManager.getConnection(URL_DB, USER, PASSWORD);
                } catch (SQLException ex) {
                    System.err.println("Failed to create database or tables: " + ex.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText("Unable to create database/tables");
                    alert.setContentText(ex.toString());
                    alert.showAndWait();
                    ex.printStackTrace();
                    return null;
                }
            }

            // Other SQL errors — show alert and return null
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Connection Error");
            alert.setContentText(String.valueOf(e));
            alert.showAndWait();
            e.printStackTrace();
            return null;
        }
    }
}
