package factone.jobschedulesystem.FrontPage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import factone.jobschedulesystem.LoginMain;

import static javafx.application.Application.launch;

public class FrontMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginMain.class.getResource("FrontView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setTitle("Front Panel");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
