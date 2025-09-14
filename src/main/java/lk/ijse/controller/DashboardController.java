package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnProgram;

    @FXML
    private JFXButton btnStudent;

    @FXML
    private JFXButton btnInstructor;

    @FXML
    private JFXButton btnPayment;

    @FXML
    private JFXButton btnLessons;

    @FXML
    private AnchorPane changeForm;

    @FXML
    private AnchorPane dashboardFrom;

    public void initialize() {
        try {
            // Load default dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            AnchorPane pane = loader.load();
            changeForm.getChildren().setAll(pane);


            // Add hover effects ONLY to buttons that exist in FXML
            addHoverEffect(btnDashboard);
            addHoverEffect(btnProgram);
            addHoverEffect(btnStudent);
            addHoverEffect(btnInstructor);
            addHoverEffect(btnPayment);
            addHoverEffect(btnLessons);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHoverEffect(JFXButton button) {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setColor(Color.DARKGRAY);

        button.setOnMouseEntered(e -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
            button.setEffect(shadow);
        });
        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setEffect(null);
        });
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        loadForm("/Home.fxml", btnDashboard);
    }

    @FXML
    void btnProgramOnAction(ActionEvent event) {
        loadForm("/programForm.fxml", btnProgram);
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) {
        loadForm("/studentForm.fxml", btnStudent);
    }

    @FXML
    void btnInstructorOnAction(ActionEvent event) {
        loadForm("/instructorForm.fxml", btnInstructor);
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {
        loadForm("/paymentTableForm.fxml", btnPayment);
    }

    @FXML
    void btnLessonsOnAction(ActionEvent event) {
        loadForm("/lessonForm.fxml", btnLessons);
    }

    @FXML
    void logOutAction(MouseEvent event) {
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/loginForm.fxml")));
            Stage stage = (Stage) dashboardFrom.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utility method to load forms & highlight active button
     */
    private void loadForm(String fxmlPath, JFXButton activeButton) {
        try {
            // Use FXMLLoader instance to load the FXML safely
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane pane = loader.load(); // load returns a Node
            changeForm.getChildren().setAll(pane); // set the loaded pane

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML: " + fxmlPath);
        } catch (NullPointerException e) {
            System.out.println("FXML file not found or 'changeForm' is null!");
            e.printStackTrace();
        }
    }


}