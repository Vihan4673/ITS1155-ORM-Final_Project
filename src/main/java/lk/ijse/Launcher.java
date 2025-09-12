package lk.ijse;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.db.FactoryConfiguration;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize Hibernate/Database connection first
        FactoryConfiguration.getInstance();

        // Show loading screen first
        primaryStage.setScene(new Scene(
                new FXMLLoader(getClass().getResource("/LoadinScreen.fxml")).load()
        ));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // Load Login Form in background (Task)
        Task<Scene> loadingTask = new Task<>() {
            @Override
            protected Scene call() throws Exception {
                // ✅ FIXED path with .fxml
                return new Scene(
                        new FXMLLoader(getClass().getResource("/loginForm.fxml")).load()
                );
            }
        };

        loadingTask.setOnSucceeded(event -> {
            Scene loginScene = loadingTask.getValue();
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login");
            primaryStage.centerOnScreen();
        });

        loadingTask.setOnFailed(event -> {
            System.err.println("❌ Failed to load Login Form!");
            loadingTask.getException().printStackTrace();
        });

        new Thread(loadingTask).start();
    }
}
