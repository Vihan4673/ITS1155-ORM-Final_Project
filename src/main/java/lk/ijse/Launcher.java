package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.db.FactoryConfiguration;

import java.io.IOException;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FactoryConfiguration.getInstance();

        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/loginForm.fxml"))));
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}