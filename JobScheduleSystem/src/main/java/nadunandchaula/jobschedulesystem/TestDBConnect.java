package nadunandchaula.jobschedulesystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestDBConnect {
    public static void main(String[] args) {
        System.out.println("Starting DB connection test...");
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.err.println("DBUtil.getConnection() returned null — check DB server, credentials, or errors shown earlier.");
            return;
        }
        System.out.println("Connected to database successfully.");
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                System.out.println("Tables in database:");
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    System.out.println(" - " + rs.getString(1));
                }
                if (!any) System.out.println(" (no tables found)");
            }
        } catch (SQLException e) {
            System.err.println("Error querying tables: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
