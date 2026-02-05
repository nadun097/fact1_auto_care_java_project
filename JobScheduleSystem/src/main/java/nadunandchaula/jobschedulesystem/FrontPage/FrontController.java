package nadunandchaula.jobschedulesystem.FrontPage;

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
        String name = textBox1.getText();
        String vehicalNo = textBox2.getText();
        String tel = textBox3.getText();
        String email = textBox4.getText();
        LocalDate selectedDate = dateValue.getValue();
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

        boolean isConnected = model.dataBaseConnect();
        if (isConnected) {
            model.insertData(name, email, tel, vehicalNo, String.join(", ", selectedJobs), selectedDate);
            System.out.println("Data inserted successfully.");
            populateTable();
        } else {
            System.out.println("Failed to connect to the database.");
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
        FrontModel model = new FrontModel();
        Job selectedJob = tableView.getSelectionModel().getSelectedItem();
        if (selectedJob != null) {
            model.deleteJob(selectedJob.getID());
            populateTable();
        } else {
            System.out.println("No job selected.");
        }
    }

    public void jobUpdateBtnClick(ActionEvent actionEvent) {
        FrontModel model = new FrontModel();
        Job selectedJob = tableView.getSelectionModel().getSelectedItem();
        if (selectedJob != null) {
            String name = textBox1.getText();
            String vehicalNo = textBox2.getText();
            String tel = textBox3.getText();
            String email = textBox4.getText();
            LocalDate selectedDate = dateValue.getValue();

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

            model.updateJob(selectedJob.getID(), name, email, tel, vehicalNo, String.join(", ", selectedJobs), selectedDate);
            populateTable();
        } else {
            System.out.println("No job selected.");
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
        FrontModel model = new FrontModel();
        ObservableList<Job> jobList = model.searchJobData(vehicalNumber);

        if (!jobList.isEmpty()) {
            Job matchedJob = jobList.get(0);
            String ownerName = matchedJob.getCustomer_Name();

            Map<String, Double> jobPrices = getJobPrices();

            double totalPrice = 0.0;
            String[] selectedJobs = matchedJob.getTimes().split(", ");
            for (String job : selectedJobs) {
                Double price = jobPrices.get(job);
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

            DiscountValueBox.setText(String.format("Discount: %.2f", discountAmount));
            TotalTextBoxS.setText(String.format("Vehicle: %s, Total: %.2f", vehicalNumber, finalPrice));

            resultBoxName.setText(String.format("Customer: %s", ownerName));
            resultBox.setText(String.format("Vehicle: %s", vehicalNumber));

            String[] jobLabels = {resultBoxJob1.getId(), resultBoxJob2.getId(), resultBoxJob3.getId(),
                    resultBoxJob4.getId(), resultBoxJob5.getId(), resultBoxJob6.getId(),
                    resultBoxJob7.getId(), resultBoxJob8.getId()};

            // Populate the visible job result boxes with job name + price
            for (int i = 0; i < jobLabels.length; i++) {
                if (i < selectedJobs.length) {
                    String jobName = selectedJobs[i];
                    Double price = jobPrices.get(jobName);
                    String text = (price != null) ? String.format("%s - Rs %.2f", jobName, price) : jobName;
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

            resultBox.setUserData(new EmailData(matchedJob.getCustomer_Mail(), ownerName, vehicalNumber, finalPrice, discountAmount, selectedJobs));

        } else {
            TotalTextBoxS.setText("Vehicle number not found.");
        }
    }

    // Helper that returns the official prices for each job
    private Map<String, Double> getJobPrices() {
        Map<String, Double> jobPrices = new HashMap<>();
        jobPrices.put("Vehicle Full Service", 4400.0);
        jobPrices.put("Vehicle Normal Service", 11200.0);
        jobPrices.put("Vehicle body wash", 5000.0);
        jobPrices.put("Vehicle Running Repair", 4300.0);
        jobPrices.put("Vehicle Brakes Service", 8100.0);
        jobPrices.put("Vehicle Tire Rotation", 7800.0);
        jobPrices.put("Vehicle Inspection", 2500.0);
        jobPrices.put("Vehicle Scan/Diagnose", 3800.0);
        return jobPrices;
    }

    private String generateBillContent(String ownerName, String vehicleNumber, double finalPrice, double discountAmount, String[] performedJobs) {
        Map<String, Double> jobPrices = getJobPrices();
        StringBuilder jobListBuilder = new StringBuilder();
        for (String job : performedJobs) {
            Double price = jobPrices.get(job);
            if (price != null) jobListBuilder.append(String.format(" - %s : Rs %.2f\n", job, price));
            else jobListBuilder.append(String.format(" - %s\n", job));
        }
        return String.format("Dear %s,\n\n" +
                "Thank you for choosing our service.\n\n" +
                "Vehicle Number: %s\n" +
                "Jobs Performed:\n%s\n" +
                "Final Price: %.2f\n" +
                "Discount Applied: %.2f\n\n" +
                "Best Regards,\nFact One Auto Care", ownerName, vehicleNumber, jobListBuilder.toString(), finalPrice, discountAmount);
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

        if (recipientEmail == null || recipientEmail.isEmpty()) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Email Error", "Recipient email is empty. Please ensure customer has an email address."));
            return;
        }

        // Capture credentials as final variables for use inside lambdas
        final String smtpUser = senderEmail;
        final String smtpPass = senderPassword;

        // Send email in a background thread to avoid blocking the UI
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
                message.setSubject("Your Vehicle Service Bill");

                StringBuilder jobListBuilder = new StringBuilder();
                for (String job : performedJobs) {
                    jobListBuilder.append(job).append("\n");
                }
                String emailContent = String.format(
                        "Dear %s,\n\n" +
                                "Thank you for choosing our service.\n\n" +
                                "Vehicle Number: %s\n" +
                                "Jobs Performed:\n%s\n" +
                                "Total Price: %.2f\n" +
                                "Discount Applied: %.2f\n" +
                                "Final Price: %.2f\n\n" +
                                "We appreciate your business!\n\n" +
                                "Best Regards,\nYour Service Team",
                        ownerName, vehicleNumber, jobListBuilder.toString(), finalPrice + discountAmount, discountAmount, finalPrice);
                message.setText(emailContent);
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
            // Generate bill content and put it into textArea for printing
            String billContent = generateBillContent(emailData.ownerName, emailData.vehicleNumber, emailData.finalPrice, emailData.discountAmount, emailData.performedJobs);
            textArea.setText(billContent);

            // Print the bill first
            Window window = ((Node) actionEvent.getSource()).getScene().getWindow();
            boolean printed = printNode(window, textArea);
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



















