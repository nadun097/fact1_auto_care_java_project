package nadunandchaula.jobschedulesystem;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import nadunandchaula.jobschedulesystem.DBUtil;

public class LoginModel {
    // Use DBUtil for connections

    // Simulated user database with hardcoded credentials (fallback)
    private static final Map<String, UserInfo> USERS = new HashMap<>();

    // Initialize with some test users
    static {
        // Format: userID, password, email
        USERS.put("admin", new UserInfo("admin", "password", "admin@example.com"));
        USERS.put("user1", new UserInfo("user1", "pass123", "user1@example.com"));
        USERS.put("test", new UserInfo("test", "test", "nadunanjana31@gmail.com"));
    }

    public static Connection getConnection() {
        return DBUtil.getConnection();
    }

    /**
     * Validate login using the `login` table. Accepts either numeric ID or Name as the identifier.
     * Falls back to the in-memory USERS map if DB connection is not available.
     */
    boolean validateLogin(String userID, String password) {
        System.out.println("Attempting login with: " + userID);

        // First try DB
        try (Connection connection = getConnection()) {
            if (connection != null) {
                // Determine whether userID looks numeric (matching ID) or not (match Name)
                String query;
                boolean isNumeric = userID != null && userID.matches("\\d+");
                if (isNumeric) {
                    query = "SELECT Password FROM login WHERE ID = ?";
                } else {
                    query = "SELECT Password FROM login WHERE Name = ?";
                }

                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    if (isNumeric) ps.setInt(1, Integer.parseInt(userID));
                    else ps.setString(1, userID);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String dbPassword = rs.getString("Password");
                            if (dbPassword != null && dbPassword.equals(password)) {
                                System.out.println("Login successful for user: " + userID);
                                return true;
                            } else {
                                System.out.println("Invalid password for user: " + userID);
                                return false;
                            }
                        } else {
                            System.out.println("No user found in DB for: " + userID);
                            // fall through to fallback
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Login DB error: " + e.getMessage());
        }

        // Fallback to simulated USERS map
        UserInfo user = USERS.get(userID);
        if (user != null && user.password.equals(password)) {
            System.out.println("Login successful (fallback) for user: " + userID);
            return true;
        } else {
            System.out.println("Login failed (fallback) for user: " + userID);
            return false;
        }
    }

    // Retrieve user info (Name/ID/Email) from DB; fallback to simulated map
    public static UserInfo getUserInfo(String userID) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                String query;
                boolean isNumeric = userID != null && userID.matches("\\d+");
                if (isNumeric) {
                    query = "SELECT Name, ID, Email FROM login WHERE ID = ?";
                } else {
                    query = "SELECT Name, ID, Email FROM login WHERE Name = ?";
                }

                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    if (isNumeric) ps.setInt(1, Integer.parseInt(userID));
                    else ps.setString(1, userID);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String name = rs.getString("Name");
                            String idStr = String.valueOf(rs.getInt("ID"));
                            String email = rs.getString("Email");
                            return new UserInfo(name != null ? name : idStr, "", email != null ? email : "");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("getUserInfo DB error: " + e.getMessage());
        }

        // Fallback
        return USERS.get(userID);
    }

    // Simple class to store user information
    public static class UserInfo {
        String id;
        String password;
        String email;

        public UserInfo(String id, String password, String email) {
            this.id = id;
            this.password = password;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }
}
