package factone.jobschedulesystem.FrontPage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javafx.application.Platform;
import javafx.print.PrinterJob;
import javafx.stage.Window;
import javafx.scene.web.WebView;

public class FrontController {



    @FXML
    public TextField DiscountTextBox;
    @FXML
    public TextField VehicalNumberTextBox;
    @FXML
    public TextField TotalTextBoxS;
    @FXML
    public TextField DiscountValueBox;
    @FXML
    public TextField resultBox;
    public TextField resultBoxName;
    public TextField resultBoxJob1;
    public TextField resultBoxJob2;
    public TextField resultBoxJob3;
    public TextField resultBoxJob4;
    public TextField resultBoxJob5;
    public TextField resultBoxJob6;
    public TextField resultBoxJob7;
    public TextField resultBoxJob8;

    @FXML
    private CheckBox checkbox1;
    @FXML
    private CheckBox checkbox2;
    @FXML
    private CheckBox checkbox3;
    @FXML
    private CheckBox checkbox4;
    @FXML
    private CheckBox checkbox5;
    @FXML
    private CheckBox checkbox6;
    @FXML
    private CheckBox checkbox7;
    @FXML
    private CheckBox checkbox8;
    @FXML
    private TextField textBox1;
    @FXML
    private TextField textBox2;
    @FXML
    private TextField textBox3;
    @FXML
    private TextField textBox4;
    @FXML
    public TextField AutoSearch;
    @FXML
    private TextArea textArea;
    @FXML
    private DatePicker dateValue;
    @FXML
    private TableView<Job> tableView;
    @FXML
    private TableColumn<Job, Integer> id_Column;
    @FXML
    private TableColumn<Job, String> name_Column;
    @FXML
    private TableColumn<Job, String> email_Column;
    @FXML
    private TableColumn<Job, String> phone_Column;
    @FXML
    private TableColumn<Job, String> job_Column;
    @FXML
    public TableColumn<Job, String> Vehical_Column;


    @FXML
    private TableColumn<Job, String> date_Column;

    @FXML
    protected void jobSaveBtnClick() throws IOException {
        FrontModel model = new FrontModel();
        // Get the text from text fields
        String name = textBox1.getText().trim();
        String vehicalNo = textBox2.getText().trim();
        String tel = textBox3.getText().trim();
        String email = textBox4.getText().trim();
        LocalDate selectedDate = dateValue.getValue();
        List<String> selectedJobs = new ArrayList<>();
        String[] jobValues = {"Vehicle Full Service","Vehicle Normal Service","Vehicle body wash","Vehicle Running Repair","Vehicle Brakes Service","Vehicle Tire Rotation","Vehicle Inspection","Vehicle Scan/Diagnose"};

        // Input validation
        if (name.isEmpty() || vehicalNo.isEmpty() || tel.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Entry", "All fields are required. Please fill in all information.");
            return;
        }

        if (selectedDate == null) {
            showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please select a date.");
            return;
        }

        if (!email.contains("@")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please enter a valid email address.");
            return;
        }

        if (tel.length() < 10) {
            showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please enter a valid phone number (at least 10 digits).");
            return;
        }

        if (checkbox1.isSelected()) selectedJobs.add(jobValues[0]);
        if (checkbox2.isSelected()) selectedJobs.add(jobValues[1]);
        if (checkbox3.isSelected()) selectedJobs.add(jobValues[2]);
        if (checkbox4.isSelected()) selectedJobs.add(jobValues[3]);
        if (checkbox5.isSelected()) selectedJobs.add(jobValues[4]);
        if (checkbox6.isSelected()) selectedJobs.add(jobValues[5]);
        if (checkbox7.isSelected()) selectedJobs.add(jobValues[6]);
        if (checkbox8.isSelected()) selectedJobs.add(jobValues[7]);

        if (selectedJobs.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please select at least one service.");
            return;
        }

        boolean isConnected = model.dataBaseConnect();
        if (isConnected) {
            model.insertData(name, email, tel, vehicalNo, String.join(", ", selectedJobs), selectedDate);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data saved successfully!");
            populateTable();
            ClearTextField(null);
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
        }
    }

    @FXML
    public void initialize() {
        id_Column.setCellValueFactory(new PropertyValueFactory<>("ID"));
        name_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
        email_Column.setCellValueFactory(new PropertyValueFactory<>("Customer_Mail"));
        phone_Column.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        job_Column.setCellValueFactory(new PropertyValueFactory<>("Times"));
        Vehical_Column.setCellValueFactory(new PropertyValueFactory<>("VehicalNo"));
        date_Column.setCellValueFactory(new PropertyValueFactory<>("Date"));

        populateTable();

        setupAutoSearch();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFieldsWithSelectedJob(newSelection);
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFieldsWithSelectedJob(newSelection);
                VehicalNumberTextBox.setText(newSelection.getVehicalNo());
            }
        });
    }

    private void populateFieldsWithSelectedJob(Job selectedJob) {
        textBox1.setText(selectedJob.getCustomer_Name());
        textBox2.setText(selectedJob.getVehicalNo());
        textBox3.setText(selectedJob.getPhone());
        textBox4.setText(selectedJob.getCustomer_Mail());
        dateValue.setValue(selectedJob.getDate());

        checkbox1.setSelected(false);
        checkbox2.setSelected(false);
        checkbox3.setSelected(false);
        checkbox4.setSelected(false);
        checkbox5.setSelected(false);
        checkbox6.setSelected(false);
        checkbox7.setSelected(false);
        checkbox8.setSelected(false);

        String[] jobValues = {"Vehicle Full Service","Vehicle Normal Service","Vehicle body wash","Vehicle Running Repair","Vehicle Brakes Service","Vehicle Tire Rotation","Vehicle Inspection","Vehicle Scan/Diagnose"};
        String[] selectedJobs = selectedJob.getTimes().split(", ");

        for (String job : selectedJobs) {
            switch (job) {
                case "Vehicle Full Service":
                    checkbox1.setSelected(true);
                    break;
                case "Vehicle Normal Service":
                    checkbox2.setSelected(true);
                    break;
                case "Vehicle body wash":
                    checkbox3.setSelected(true);
                    break;
                case "Vehicle Running Repair":
                    checkbox4.setSelected(true);
                    break;
                case "Vehicle Brakes Service":
                    checkbox5.setSelected(true);
                    break;
                case "Vehicle Tire Rotation":
                    checkbox6.setSelected(true);
                    break;
                case "Vehicle Inspection":
                    checkbox7.setSelected(true);
                    break;
                case "Vehicle Scan/Diagnose":
                    checkbox8.setSelected(true);
                    break;
            }
        }
    }

    private void setupAutoSearch() {
        AutoSearch.setOnKeyReleased(event -> {
            Timeline timeline = null;
            if (timeline != null) {
                timeline.stop();
            }
            timeline = new Timeline(new KeyFrame(Duration.millis(300), e -> {
                autoSearch();
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    private void autoSearch() {
        String searchText = AutoSearch.getText();
        FrontModel model = new FrontModel();
        ObservableList<Job> filteredList = model.searchJobData(searchText);
        tableView.setItems(filteredList);
    }

    private void populateTable() {
        FrontModel model = new FrontModel();
        tableView.setItems(model.getJobData());
    }

    public void jobSearchBtnClick(ActionEvent actionEvent) {
        FrontModel model = new FrontModel();
        String searchText = textBox1.getText();
        tableView.setItems(model.searchJobData(searchText));
    }

    public void jobDeleteBtnClick(ActionEvent actionEvent) {
        Job selectedJob = tableView.getSelectionModel().getSelectedItem();
        if (selectedJob != null) {
            // Confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete Record");
            confirmAlert.setContentText("Are you sure you want to delete this record?\n" +
                    "Vehicle: " + selectedJob.getVehicalNo() + "\n" +
                    "Customer: " + selectedJob.getCustomer_Name());

            ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                FrontModel model = new FrontModel();
                model.deleteJob(selectedJob.getID());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Record deleted successfully!");
                populateTable();
                ClearTextField(null);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Delete operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a record to delete.");
        }
    }

    public void jobUpdateBtnClick(ActionEvent actionEvent) {
        Job selectedJob = tableView.getSelectionModel().getSelectedItem();
        if (selectedJob != null) {
            String name = textBox1.getText().trim();
            String vehicalNo = textBox2.getText().trim();
            String tel = textBox3.getText().trim();
            String email = textBox4.getText().trim();
            LocalDate selectedDate = dateValue.getValue();

            // Input validation
            if (name.isEmpty() || vehicalNo.isEmpty() || tel.isEmpty() || email.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Entry", "All fields are required. Please fill in all information.");
                return;
            }

            if (selectedDate == null) {
                showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please select a date.");
                return;
            }

            if (!email.contains("@")) {
                showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please enter a valid email address.");
                return;
            }

            if (tel.length() < 10) {
                showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please enter a valid phone number (at least 10 digits).");
                return;
            }

            List<String> selectedJobs = new ArrayList<>();
            String[] jobValues = {"Vehicle Full Service","Vehicle Normal Service","Vehicle body wash","Vehicle Running Repair","Vehicle Brakes Service","Vehicle Tire Rotation","Vehicle Inspection","Vehicle Scan/Diagnose"};

            if (checkbox1.isSelected()) selectedJobs.add(jobValues[0]);
            if (checkbox2.isSelected()) selectedJobs.add(jobValues[1]);
            if (checkbox3.isSelected()) selectedJobs.add(jobValues[2]);
            if (checkbox4.isSelected()) selectedJobs.add(jobValues[3]);
            if (checkbox5.isSelected()) selectedJobs.add(jobValues[4]);
            if (checkbox6.isSelected()) selectedJobs.add(jobValues[5]);
            if (checkbox7.isSelected()) selectedJobs.add(jobValues[6]);
            if (checkbox8.isSelected()) selectedJobs.add(jobValues[7]);

            if (selectedJobs.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Entry", "Please select at least one service.");
                return;
            }

            // Confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Record");
            confirmAlert.setContentText("Are you sure you want to update this record?\n" +
                    "Vehicle: " + vehicalNo + "\n" +
                    "Customer: " + name);

            ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                FrontModel model = new FrontModel();
                model.updateJob(selectedJob.getID(), name, email, tel, vehicalNo, String.join(", ", selectedJobs), selectedDate);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Record updated successfully!");
                populateTable();
                ClearTextField(null);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Update operation cancelled.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a record to update.");
        }
    }

    public void AutoSearchTextBox(ActionEvent actionEvent) {
        autoSearch();
    }

    public void ClearTextField(ActionEvent actionEvent) {
        textBox1.clear();
        textBox2.clear();
        textBox3.clear();
        textBox4.clear();
        AutoSearch.clear();
        textArea.clear();
        TotalTextBoxS.clear();
        DiscountTextBox.clear();
        VehicalNumberTextBox.clear();
        DiscountValueBox.clear();

        dateValue.setValue(null);

        checkbox1.setSelected(false);
        checkbox2.setSelected(false);
        checkbox3.setSelected(false);
        checkbox4.setSelected(false);
        checkbox5.setSelected(false);
        checkbox6.setSelected(false);
        checkbox7.setSelected(false);
        checkbox8.setSelected(false);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void TotleBtnClick(ActionEvent actionEvent) {
        calculateTotal();
    }

    private void calculateTotal() {
        String vehicalNumber = VehicalNumberTextBox.getText();
        if (vehicalNumber == null || vehicalNumber.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Vehicle Number Required", "Please enter a vehicle number to calculate the bill.");
            return;
        }
        FrontModel model = new FrontModel();
        ObservableList<Job> jobList = model.searchJobData(vehicalNumber);

        if (!jobList.isEmpty()) {
            Job matchedJob = jobList.get(0);
            String ownerName = matchedJob.getCustomer_Name();
            String customerEmail = normalizeEmail(matchedJob.getCustomer_Mail(), textBox4.getText());

            Map<String, Double> jobPrices = getJobPricesNormalized();

            double totalPrice = 0.0;
            String[] selectedJobs = matchedJob.getTimes().split(", ");
            for (String job : selectedJobs) {
                Double price = jobPrices.get(normalizeJobKey(job));
                if (price != null) {
                    totalPrice += price;
                }
            }

            double discountPercentage = 0.0;
            if (!DiscountTextBox.getText().isEmpty()) {
                discountPercentage = Double.parseDouble(DiscountTextBox.getText());
            }
            double discountAmount = (discountPercentage / 100) * totalPrice;
            double finalPrice = totalPrice - discountAmount;

            DiscountValueBox.setText(String.format("Discount: Rs %.2f", discountAmount));
            TotalTextBoxS.setText(String.format("Vehicle: %s, Total: Rs %.2f", vehicalNumber, finalPrice));

            resultBoxName.setText(String.format("Customer: %s", ownerName));
            resultBox.setText(String.format("Vehicle: %s", vehicalNumber));

            String[] jobLabels = {resultBoxJob1.getId(), resultBoxJob2.getId(), resultBoxJob3.getId(),
                    resultBoxJob4.getId(), resultBoxJob5.getId(), resultBoxJob6.getId(),
                    resultBoxJob7.getId(), resultBoxJob8.getId()};

            // Populate the visible job result boxes with job name + price
            for (int i = 0; i < jobLabels.length; i++) {
                if (i < selectedJobs.length) {
                    String jobName = selectedJobs[i];
                    String displayName = getJobDisplayName(jobName);
                    Double price = jobPrices.get(normalizeJobKey(jobName));
                    String text = (price != null) ? String.format("%s - Rs %.2f", displayName, price) : displayName;
                    switch (jobLabels[i]) {
                        case "resultBoxJob1":
                            resultBoxJob1.setText(text);
                            break;
                        case "resultBoxJob2":
                            resultBoxJob2.setText(text);
                            break;
                        case "resultBoxJob3":
                            resultBoxJob3.setText(text);
                            break;
                        case "resultBoxJob4":
                            resultBoxJob4.setText(text);
                            break;
                        case "resultBoxJob5":
                            resultBoxJob5.setText(text);
                            break;
                        case "resultBoxJob6":
                            resultBoxJob6.setText(text);
                            break;
                        case "resultBoxJob7":
                            resultBoxJob7.setText(text);
                            break;
                        case "resultBoxJob8":
                            resultBoxJob8.setText(text);
                            break;
                    }
                } else {
                    switch (jobLabels[i]) {
                        case "resultBoxJob1":
                            resultBoxJob1.setText("");
                            break;
                        case "resultBoxJob2":
                            resultBoxJob2.setText("");
                            break;
                        case "resultBoxJob3":
                            resultBoxJob3.setText("");
                            break;
                        case "resultBoxJob4":
                            resultBoxJob4.setText("");
                            break;
                        case "resultBoxJob5":
                            resultBoxJob5.setText("");
                            break;
                        case "resultBoxJob6":
                            resultBoxJob6.setText("");
                            break;
                        case "resultBoxJob7":
                            resultBoxJob7.setText("");
                            break;
                        case "resultBoxJob8":
                            resultBoxJob8.setText("");
                            break;
                    }
                }
            }

            resultBox.setUserData(new EmailData(customerEmail, ownerName, vehicalNumber, finalPrice, discountAmount, selectedJobs));
        } else {
            TotalTextBoxS.setText("Vehicle number not found.");
            showAlert(Alert.AlertType.ERROR, "Vehicle Not Found", "No records found for vehicle number: " + vehicalNumber);
        }
    }

    private Map<String, Double> getJobPricesNormalized() {
        Map<String, Double> jobPrices = new HashMap<>();
        jobPrices.put(normalizeJobKey("Vehicle Full Service"), 4400.0);
        jobPrices.put(normalizeJobKey("Vehicle Normal Service"), 11200.0);
        jobPrices.put(normalizeJobKey("Vehicle body wash"), 5000.0);
        jobPrices.put(normalizeJobKey("Vehicle Running Repair"), 4300.0);
        jobPrices.put(normalizeJobKey("Vehicle Brakes Service"), 8100.0);
        jobPrices.put(normalizeJobKey("Vehicle Tire Rotation"), 7800.0);
        jobPrices.put(normalizeJobKey("Vehicle Inspection"), 2500.0);
        jobPrices.put(normalizeJobKey("Vehicle Scan/Diagnose"), 3800.0);
        return jobPrices;
    }

    private String normalizeJobKey(String jobName) {
        return jobName == null ? "" : jobName.trim().toLowerCase();
    }

    private String getJobDisplayName(String jobName) {
        String key = normalizeJobKey(jobName);
        switch (key) {
            case "vehicle full service":
                return "Vehicle Full Service";
            case "vehicle normal service":
                return "Vehicle Normal Service";
            case "vehicle body wash":
                return "Vehicle body wash";
            case "vehicle running repair":
                return "Vehicle Running Repair";
            case "vehicle brakes service":
                return "Vehicle Brakes Service";
            case "vehicle tire rotation":
                return "Vehicle Tire Rotation";
            case "vehicle inspection":
                return "Vehicle Inspection";
            case "vehicle scan/diagnose":
                return "Vehicle Scan/Diagnose";
            default:
                return jobName == null ? "" : jobName.trim();
        }
    }

    private String normalizeEmail(String dbEmail, String fallbackEmail) {
        String email = (dbEmail != null && !dbEmail.trim().isEmpty()) ? dbEmail.trim() : "";
        if (email.isEmpty() && fallbackEmail != null && !fallbackEmail.trim().isEmpty()) {
            email = fallbackEmail.trim();
        }
        return email;
    }

    private String generateBillContent(String ownerName, String vehicleNumber, double finalPrice, double discountAmount, String[] performedJobs) {
        Map<String, Double> jobPrices = getJobPricesNormalized();
        StringBuilder jobListBuilder = new StringBuilder();
        double subtotal = 0.0;
        for (String job : performedJobs) {
            String displayName = getJobDisplayName(job);
            Double price = jobPrices.get(normalizeJobKey(job));
            if (price != null) {
                subtotal += price;
                jobListBuilder.append(String.format(" - %s : Rs %.2f\n", displayName, price));
            } else {
                jobListBuilder.append(String.format(" - %s\n", displayName));
            }
        }
        return String.format("Dear %s,\n\n" +
                "Thank you for choosing our service.\n\n" +
                "Vehicle Number: %s\n" +
                "Jobs Performed:\n%s\n" +
                "Subtotal: Rs %.2f\n" +
                "Discount Applied: Rs %.2f\n" +
                "Final Price: Rs %.2f\n\n" +
                "Best Regards,\nFact One Auto Care", ownerName, vehicleNumber, jobListBuilder.toString(), subtotal, discountAmount, finalPrice);
    }

    private String generateHTMLBillContent(String ownerName, String vehicleNumber, double finalPrice, double discountAmount, String[] performedJobs) {
        Map<String, Double> jobPrices = getJobPricesNormalized();
        StringBuilder jobListBuilder = new StringBuilder();
        double subtotal = 0.0;

        for (String job : performedJobs) {
            String displayName = getJobDisplayName(job);
            Double price = jobPrices.get(normalizeJobKey(job));
            if (price != null) {
                subtotal += price;
                jobListBuilder.append(String.format(
                    "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>%s</td><td style='padding: 8px; border-bottom: 1px solid #ddd; text-align: right;'>Rs %.2f</td></tr>\n",
                    displayName, price));
            } else {
                jobListBuilder.append(String.format(
                    "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;' colspan='2'>%s</td></tr>\n",
                    displayName));
            }
        }

        return String.format(
            "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
            ".container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
            ".header { text-align: center; margin-bottom: 30px; }" +
            ".logo { max-width: 300px; height: auto; }" +
            ".title { color: #c41e3a; font-size: 24px; margin: 20px 0; }" +
            ".info { margin: 20px 0; }" +
            ".label { font-weight: bold; color: #333; }" +
            "table { width: 100%%; border-collapse: collapse; margin: 20px 0; }" +
            "th { background-color: #c41e3a; color: white; padding: 12px; text-align: left; }" +
            "td { padding: 8px; border-bottom: 1px solid #ddd; }" +
            ".total-section { margin-top: 20px; padding-top: 20px; border-top: 2px solid #c41e3a; }" +
            ".total-row { display: flex; justify-content: space-between; margin: 10px 0; font-size: 16px; }" +
            ".final-total { font-size: 20px; font-weight: bold; color: #c41e3a; }" +
            ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 14px; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<div class='header'>" +
            "<h1 style='color: #c41e3a; font-size: 32px; margin: 10px 0;'>FACT ONE AUTO CARE</h1>" +
            "<svg width=\"44\" height=\"44\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" style=\"display: inline-block;\">" +
            "<path d=\"M9.7002 1.65686C10.5302 1.30116 11.4698 1.30116 12.2998 1.65686L18.0332 4.11389C18.4375 4.28717 18.7 4.68478 18.7002 5.12463V10.903C18.7002 13.0344 17.6709 15.0351 15.9365 16.274L12.2783 18.8864C11.5135 19.4325 10.4865 19.4325 9.72168 18.8864L6.06348 16.274C4.32912 15.0351 3.2998 13.0344 3.2998 10.903V5.12463C3.29998 4.68478 3.56248 4.28717 3.9668 4.11389L9.7002 1.65686ZM14.9248 6.91858C14.4935 6.57375 13.8635 6.64364 13.5186 7.07483L9.81738 11.7018L8.40723 10.2926C8.01679 9.90235 7.38365 9.90243 6.99316 10.2926C6.60272 10.6831 6.60282 11.3161 6.99316 11.7067L8.79785 13.5123C9.42999 14.1441 10.4709 14.0861 11.0293 13.3883L15.0811 8.32483C15.4261 7.89357 15.3561 7.26359 14.9248 6.91858Z\" fill=\"#FF8000\"/>" +
            "</svg>" +
            "<div class='title'>Service Bill</div>" +
            "</div>" +
            "<div class='info'>" +
            "<p>Dear <span class='label'>%s</span>,</p>" +
            "<p>Thank you for choosing Fact One Auto Care for your vehicle service needs.</p>" +
            "</div>" +
            "<div class='info'>" +
            "<p><span class='label'>Vehicle Number:</span> %s</p>" +
            "<p><span class='label'>Date:</span> %s</p>" +
            "</div>" +
            "<table>" +
            "<thead><tr><th>Service Description</th><th style='text-align: right;'>Price</th></tr></thead>" +
            "<tbody>" +
            "%s" +
            "</tbody>" +
            "</table>" +
            "<div class='total-section'>" +
            "<div class='total-row'><span>Subtotal:</span><span>Rs %.2f</span></div>" +
            "<div class='total-row'><span>Discount Applied:</span><span>Rs %.2f</span></div>" +
            "<div class='total-row final-total'><span>Final Amount:</span><span>Rs %.2f</span></div>" +
            "</div>" +
            "<div class='footer'>" +
            "<p>Thank you for your business!</p>" +
            "<p><strong>Fact One Auto Care</strong></p>" +
            "<p>Contact: factoneautocare@gmail.com</p>" +
            "</div>" +
            "</div>" +
            "</body>" +
            "</html>",
            ownerName, vehicleNumber, java.time.LocalDate.now().toString(), jobListBuilder.toString(), subtotal, discountAmount, finalPrice
        );
    }

    private String generateHTMLBillContentForPrint(String ownerName, String vehicleNumber, double finalPrice, double discountAmount, String[] performedJobs) {
        Map<String, Double> jobPrices = getJobPricesNormalized();
        StringBuilder jobListBuilder = new StringBuilder();
        double subtotal = 0.0;

        for (String job : performedJobs) {
            String displayName = getJobDisplayName(job);
            Double price = jobPrices.get(normalizeJobKey(job));
            if (price != null) {
                subtotal += price;
                jobListBuilder.append(String.format(
                    "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;'>%s</td><td style='padding: 8px; border-bottom: 1px solid #ddd; text-align: right;'>Rs %.2f</td></tr>\n",
                    displayName, price));
            } else {
                jobListBuilder.append(String.format(
                    "<tr><td style='padding: 8px; border-bottom: 1px solid #ddd;' colspan='2'>%s</td></tr>\n",
                    displayName));
            }
        }

        // No need to load logo anymore - using company name instead
        String companyHeader = "<h1 style='color: #c41e3a; font-size: 32px; margin: 10px 0;'>FACT ONE AUTO CARE</h1>";

        return String.format(
            "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "<style>" +
            "@media print { body { margin: 0; padding: 10px; background-color: white; } .container { box-shadow: none; } }" +
            "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
            ".container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }" +
            ".header { text-align: center; margin-bottom: 30px; }" +
            ".logo { max-width: 300px; height: auto; }" +
            ".title { color: #c41e3a; font-size: 24px; margin: 20px 0; }" +
            ".info { margin: 20px 0; }" +
            ".label { font-weight: bold; color: #333; }" +
            "table { width: 100%%; border-collapse: collapse; margin: 20px 0; }" +
            "th { background-color: #c41e3a; color: white; padding: 12px; text-align: left; }" +
            "td { padding: 8px; border-bottom: 1px solid #ddd; }" +
            ".total-section { margin-top: 20px; padding-top: 20px; border-top: 2px solid #c41e3a; }" +
            ".total-row { display: flex; justify-content: space-between; margin: 10px 0; font-size: 16px; }" +
            ".final-total { font-size: 20px; font-weight: bold; color: #c41e3a; }" +
            ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 14px; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class='container'>" +
            "<div class='header'>" +
            "%s" +
            "<svg width=\"44\" height=\"44\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\" style=\"display: inline-block;\">" +
            "<path d=\"M9.7002 1.65686C10.5302 1.30116 11.4698 1.30116 12.2998 1.65686L18.0332 4.11389C18.4375 4.28717 18.7 4.68478 18.7002 5.12463V10.903C18.7002 13.0344 17.6709 15.0351 15.9365 16.274L12.2783 18.8864C11.5135 19.4325 10.4865 19.4325 9.72168 18.8864L6.06348 16.274C4.32912 15.0351 3.2998 13.0344 3.2998 10.903V5.12463C3.29998 4.68478 3.56248 4.28717 3.9668 4.11389L9.7002 1.65686ZM14.9248 6.91858C14.4935 6.57375 13.8635 6.64364 13.5186 7.07483L9.81738 11.7018L8.40723 10.2926C8.01679 9.90235 7.38365 9.90243 6.99316 10.2926C6.60272 10.6831 6.60282 11.3161 6.99316 11.7067L8.79785 13.5123C9.42999 14.1441 10.4709 14.0861 11.0293 13.3883L15.0811 8.32483C15.4261 7.89357 15.3561 7.26359 14.9248 6.91858Z\" fill=\"#FF8000\"/>" +
            "</svg>" +
            "<div class='title'>Service Bill</div>" +
            "</div>" +
            "<div class='info'>" +
            "<p>Dear <span class='label'>%s</span>,</p>" +
            "<p>Thank you for choosing Fact One Auto Care for your vehicle service needs.</p>" +
            "</div>" +
            "<div class='info'>" +
            "<p><span class='label'>Vehicle Number:</span> %s</p>" +
            "<p><span class='label'>Date:</span> %s</p>" +
            "</div>" +
            "<table>" +
            "<thead><tr><th>Service Description</th><th style='text-align: right;'>Price</th></tr></thead>" +
            "<tbody>" +
            "%s" +
            "</tbody>" +
            "</table>" +
            "<div class='total-section'>" +
            "<div class='total-row'><span>Subtotal:</span><span>Rs %.2f</span></div>" +
            "<div class='total-row'><span>Discount Applied:</span><span>Rs %.2f</span></div>" +
            "<div class='total-row final-total'><span>Final Amount:</span><span>Rs %.2f</span></div>" +
            "</div>" +
            "<div class='footer'>" +
            "<p>Thank you for your business!</p>" +
            "<p><strong>Fact One Auto Care</strong></p>" +
            "<p>Contact: factoneautocare@gmail.com</p>" +
            "</div>" +
            "</div>" +
            "</body>" +
            "</html>",
            companyHeader, ownerName, vehicleNumber, java.time.LocalDate.now().toString(), jobListBuilder.toString(), subtotal, discountAmount, finalPrice
        );
    }


    private boolean printHTML(Window ownerWindow, String htmlContent) {
        try {
            WebView webView = new WebView();
            webView.getEngine().loadContent(htmlContent);

            // Wait for content to load before printing
            webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    Platform.runLater(() -> {
                        PrinterJob job = PrinterJob.createPrinterJob();
                        if (job == null) {
                            System.err.println("No printers available.");
                            showAlert(Alert.AlertType.ERROR, "Print Error", "No printers available.");
                            return;
                        }

                        boolean proceed = job.showPrintDialog(ownerWindow);
                        if (!proceed) {
                            System.out.println("Print dialog cancelled.");
                            return;
                        }

                        webView.getEngine().print(job);
                        job.endJob();
                        System.out.println("Printed successfully.");
                    });
                }
            });

            return true;
        } catch (Exception e) {
            System.err.println("Error printing HTML: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Print Error", "Failed to print the bill.");
            return false;
        }
    }

    private void sendBillViaEmail(String recipientEmail, String ownerName, String vehicleNumber, double finalPrice, double discountAmount, String[] performedJobs) {
        // Read SMTP credentials from resources/email.properties if available
        Properties emailProps = new Properties();
        String senderEmail = "factoneautocare@gmail.com";
        String senderPassword = "gtfk fpnj axvx pqgh"; // fallback (replace with app password)

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            if (in != null) {
                emailProps.load(in);
                senderEmail = emailProps.getProperty("mail.username", senderEmail);
                senderPassword = emailProps.getProperty("mail.password", senderPassword);
            }
        } catch (IOException e) {
            System.err.println("Could not load email.properties: " + e.getMessage());
        }

        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Email Error", "Recipient email is empty. Please ensure customer has an email address."));
            return;
        }

        final String smtpUser = senderEmail;
        final String smtpPass = senderPassword;

        new Thread(() -> {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUser, smtpPass);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(smtpUser));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject("Your Vehicle Service Bill - Fact One Auto Care");

                // Create multipart message for HTML + image
                MimeMultipart multipart = new MimeMultipart("related");

                // HTML body part
                MimeBodyPart htmlPart = new MimeBodyPart();
                String htmlContent = generateHTMLBillContent(ownerName, vehicleNumber, finalPrice, discountAmount, performedJobs);
                htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
                multipart.addBodyPart(htmlPart);


                // Image part (nic.svg badge)
                try (InputStream badgeStream = getClass().getClassLoader()
                        .getResourceAsStream("factone/jobschedulesystem/nic.svg")) {
                    if (badgeStream == null) {
                        System.err.println("Badge not found at factone/jobschedulesystem/nic.svg");
                    } else {
                        byte[] svgBytes = badgeStream.readAllBytes();
                        if (svgBytes.length == 0) {
                            System.err.println("Badge file is empty; replace nic.svg with a real image.");
                        } else {
                            MimeBodyPart badgePart = new MimeBodyPart();
                            badgePart.setContent(svgBytes, "image/svg+xml");
                            badgePart.setHeader("Content-ID", "<nic>");
                            badgePart.setDisposition(MimeBodyPart.INLINE);
                            badgePart.setFileName("nic.svg");
                            multipart.addBodyPart(badgePart);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Could not attach badge: " + e.getMessage());
                }

                message.setContent(multipart);
                Transport.send(message);

                Platform.runLater(() -> showAlert(Alert.AlertType.INFORMATION, "Email Sent", "Bill sent successfully to " + recipientEmail));
                System.out.println("Bill sent successfully to " + recipientEmail);
            } catch (MessagingException e) {
                e.printStackTrace();
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Email Error", "Failed to send bill: " + e.getMessage()));
            }
        }).start();
    }

    private static class EmailData {
        String recipientEmail;
        String ownerName;
        String vehicleNumber;
        double finalPrice;
        double discountAmount;
        String[] performedJobs;

        EmailData(String recipientEmail, String ownerName, String vehicleNumber, double finalPrice, double discountAmount, String[] performedJobs) {
            this.recipientEmail = recipientEmail;
            this.ownerName = ownerName;
            this.vehicleNumber = vehicleNumber;
            this.finalPrice = finalPrice;
            this.discountAmount = discountAmount;
            this.performedJobs = performedJobs;
        }
    }

    @FXML
    public void PrintMailBtnClick(ActionEvent actionEvent) {
        EmailData emailData = (EmailData) resultBox.getUserData();
        if (emailData != null) {
            // Generate HTML bill content for printing
            String htmlContent = generateHTMLBillContentForPrint(emailData.ownerName, emailData.vehicleNumber, emailData.finalPrice, emailData.discountAmount, emailData.performedJobs);

            // Print the HTML bill
            Window window = ((Node) actionEvent.getSource()).getScene().getWindow();
            boolean printed = printHTML(window, htmlContent);

            if (printed) {
                // After successful print, send email
                sendBillViaEmail(emailData.recipientEmail, emailData.ownerName, emailData.vehicleNumber, emailData.finalPrice, emailData.discountAmount, emailData.performedJobs);
            } else {
                System.out.println("Printing was cancelled or failed; email not sent.");
            }
        } else {
            System.out.println("No data to send.");
        }
    }

    @FXML
    public void CloseBtnClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Any unsaved changes will be lost.");

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private boolean printNode(Window ownerWindow, javafx.scene.Node nodeToPrint) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            System.err.println("No printers available.");
            showAlert(Alert.AlertType.ERROR, "Print Error", "No printers available.");
            return false;
        }
        boolean proceed = job.showPrintDialog(ownerWindow);
        if (!proceed) {
            System.out.println("Print dialog cancelled.");
            return false;
        }
        boolean printed = job.printPage(nodeToPrint);
        if (printed) {
            job.endJob();
            System.out.println("Printed successfully.");
            return true;
        } else {
            System.err.println("Printing failed.");
            showAlert(Alert.AlertType.ERROR, "Print Error", "Failed to print the bill.");
            return false;
        }
    }
}
























