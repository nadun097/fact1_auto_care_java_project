package nadunandchaula.jobschedulesystem.FrontPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.sql.*;
import java.time.LocalDate;

import nadunandchaula.jobschedulesystem.DBUtil;

public class FrontModel {
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        return DBUtil.getConnection();
    }

    public boolean dataBaseConnect() {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Successfully connected to the database.");
                return true;
            } else {
                System.err.println("Failed to connect to the database.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertData(String name, String email, String tel, String vehicalNo, String selectedJobs, LocalDate selectedDate) {
        String query = "INSERT INTO jobtable (Customer_Name, Customer_Mail, Phone, VehivalNo, Times, Date) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("Insertion skipped: no DB connection available.");
            return;
        }
        try (Connection conn = connection;
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, tel);
            preparedStatement.setString(4, vehicalNo);
            preparedStatement.setString(5, selectedJobs);
            preparedStatement.setDate(6, Date.valueOf(selectedDate));
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new job was inserted successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Insertion error: " + e.getMessage());
        }
    }
    public ObservableList<Job> searchJobData(String searchText) {
        ObservableList<Job> jobList = FXCollections.observableArrayList();
        String query = "SELECT * FROM jobtable WHERE Customer_Name LIKE ? OR Customer_Mail LIKE ? OR Phone LIKE ? OR VehivalNo LIKE ? OR Times LIKE ? OR Date LIKE ?";
        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("Search skipped: no DB connection available.");
            return jobList;
        }
        try (Connection conn = connection;
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            String searchPattern = "%" + searchText + "%";
            for (int i = 1; i <= 6; i++) {
                preparedStatement.setString(i, searchPattern);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("Customer_Name");
                    String email = resultSet.getString("Customer_Mail");
                    String phone = resultSet.getString("Phone");
                    String vehicalNo = resultSet.getString("VehivalNo");
                    String times = resultSet.getString("Times");
                    String dateString = resultSet.getString("Date");
                    LocalDate date = null;
                    if (dateString != null && !dateString.isEmpty()) {
                        date = LocalDate.parse(dateString);
                    }
                    Job job = new Job(id, name, email, phone, vehicalNo, times, date);
                    jobList.add(job);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
        return jobList;
    }

    public ObservableList<Job> getJobData() {
        ObservableList<Job> jobList = FXCollections.observableArrayList();
        String query = "SELECT * FROM jobtable";
        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("getJobData skipped: no DB connection available.");
            return jobList;
        }
        try (Connection conn = connection;
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Customer_Name");
                String email = resultSet.getString("Customer_Mail");
                String phone = resultSet.getString("Phone");
                String times = resultSet.getString("Times");
                String vehicalNo = resultSet.getString("VehivalNo");
                // Fetch the date value as a string
                String dateString = resultSet.getString("Date");
                LocalDate date = null;
                if (dateString != null && !dateString.isEmpty()) {
                    date = LocalDate.parse(dateString);
                }
                Job job = new Job(id, name, email, phone, vehicalNo, times, date);
                jobList.add(job);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching data: " + e.getMessage());
        }
        return jobList;
    }
    public void deleteJob(int jobId) {
        String query = "DELETE FROM jobtable WHERE ID = ?";
        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("Deletion skipped: no DB connection available.");
            return;
        }
        try (Connection conn = connection;
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, jobId);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("A job was deleted successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Deletion error: " + e.getMessage());
        }
}
    public void updateJob(int id, String name, String email, String tel, String vehicalNo, String selectedJobs, LocalDate selectedDate) {
        String query = "UPDATE jobtable SET Customer_Name = ?, Customer_Mail = ?, Phone = ?, VehivalNo = ?, Times = ?, Date = ? WHERE ID = ?";

        Connection connection = getConnection();
        if (connection == null) {
            System.err.println("Update skipped: no DB connection available.");
            return;
        }
        try (Connection conn = connection;
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, tel);
                preparedStatement.setString(4, vehicalNo);
                preparedStatement.setString(5, selectedJobs);
                preparedStatement.setDate(6, Date.valueOf(selectedDate));
                preparedStatement.setInt(7, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("A job was updated successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Update error: " + e.getMessage());
        }
}}
