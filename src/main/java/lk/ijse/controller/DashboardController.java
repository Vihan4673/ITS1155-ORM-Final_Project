package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
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

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserRole;

    // Initialize method
    public void initialize() {
        try {
            // Load default dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Home.fxml"));
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

    // Hover effect helper
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

    // Load forms
    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        loadForm("/View/Home.fxml", btnDashboard);
    }

    @FXML
    void btnProgramOnAction(ActionEvent event) {
        loadForm("/View/CourseForm.fxml", btnProgram);
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) {
        loadForm("/View/studentForm.fxml", btnStudent);
    }

    @FXML
    void btnInstructorOnAction(ActionEvent event) {
        loadForm("/View/instructorForm.fxml", btnInstructor);
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {
        loadForm("/View/paymentTableForm.fxml", btnPayment);
    }

    @FXML
    void btnLessonsOnAction(ActionEvent event) {
        loadForm("/View/lessonForm.fxml", btnLessons);
    }

    @FXML
    void btSettingsOnAction(ActionEvent event)  {
        loadForm("/View/settingForm.fxml", btnLessons);
    }

    @FXML
    void logOutAction(ActionEvent event) {
        try {
            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/View/loginForm.fxml")));
            Stage stage = (Stage) dashboardFrom.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Utility method to load forms & highlight active button
    private void loadForm(String fxmlPath, JFXButton activeButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane pane = loader.load();
            changeForm.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML: " + fxmlPath);
        }
    }

    // NEW: Method to set user info
    public void setUserInfo(String userName, String role) {
        lblUserName.setText(userName);
        lblUserRole.setText(role);
    }
}
