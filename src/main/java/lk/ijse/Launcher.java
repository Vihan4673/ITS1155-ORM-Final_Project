package lk.ijse;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.config.FactoryConfiguration;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage loadingStage = new Stage();
        try {
            Parent loadingRoot = FXMLLoader.load(getClass().getResource("/View/LoadinScreen.fxml"));
            Scene loadingScene = new Scene(loadingRoot);
            loadingScene.setFill(Color.TRANSPARENT);

            loadingStage.setScene(loadingScene);
            loadingStage.initStyle(StageStyle.TRANSPARENT);
            loadingStage.setResizable(false);
            loadingStage.centerOnScreen();
            loadingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load loading screen!");
        }

        // Task 1: Initialize Database/Hibernate
        Task<Void> initTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                FactoryConfiguration.getInstance();
                Thread.sleep(1000); // optional: simulate loading delay
                return null;
            }
        };


        initTask.setOnSucceeded(event -> {
            // Task 2: Load Login Form
            Task<Parent> loginLoadTask = new Task<>() {
                @Override
                protected Parent call() throws Exception {
                    return FXMLLoader.load(getClass().getResource("/View/loginForm.fxml"));
                }
            };

            loginLoadTask.setOnSucceeded(e -> {
                loadingStage.close(); // close loading screen
                Parent loginRoot = loginLoadTask.getValue();
                Scene loginScene = new Scene(loginRoot);

                Stage loginStage = new Stage();
                loginStage.setScene(loginScene);
                loginStage.setTitle("The Elite Driving School");
                loginStage.setResizable(true);
                loginStage.centerOnScreen();
                loginStage.show();
            });

            loginLoadTask.setOnFailed(e -> {
                loadingStage.close();
                System.err.println("Failed to load Login Form!");
                loginLoadTask.getException().printStackTrace();
            });

            new Thread(loginLoadTask).start();
        });

        initTask.setOnFailed(event -> {
            loadingStage.close();
            System.err.println("Database initialization failed!");
            initTask.getException().printStackTrace();
        });

        new Thread(initTask).start();
    }
}
