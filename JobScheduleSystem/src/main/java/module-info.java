module nadunandchaula.jobschedulesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires java.sql;
    // When you install MySQL, uncomment the following line:
    // requires mysql.connector.j;


    opens nadunandchaula.jobschedulesystem to javafx.fxml;
    exports nadunandchaula.jobschedulesystem;
    opens nadunandchaula.jobschedulesystem.FrontPage to javafx.fxml;
    exports nadunandchaula.jobschedulesystem.FrontPage;
}