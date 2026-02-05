module factone.jobschedulesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.mail;
    requires java.sql;
    // When you install MySQL, uncomment the following line:
    // requires mysql.connector.j;


    opens factone.jobschedulesystem to javafx.fxml;
    exports factone.jobschedulesystem;
    opens factone.jobschedulesystem.FrontPage to javafx.fxml;
    exports factone.jobschedulesystem.FrontPage;
}