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

    @FXML private JFXButton btnDashboard;
    @FXML private JFXButton btnProgram;
    @FXML private JFXButton btnStudent;
    @FXML private JFXButton btnInstructor;
    @FXML private JFXButton btnPayment;
    @FXML private JFXButton btnLessons;

    @FXML private AnchorPane changeForm;
    @FXML private AnchorPane dashboardFrom;

    @FXML private Label lblUserName;
    @FXML private Label lblUserRole;

    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Home.fxml"));
            AnchorPane pane = loader.load();
            changeForm.getChildren().setAll(pane);

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
        loadForm("/View/Home.fxml");
    }

    @FXML
    void btnProgramOnAction(ActionEvent event) {
        loadForm("/View/CourseForm.fxml");
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) {
        loadForm("/View/studentForm.fxml");
    }

    @FXML
    void btnInstructorOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/instructorForm.fxml"));
            AnchorPane pane = loader.load();

            InstructorFormController controller = loader.getController();
            controller.setUserRole(lblUserRole.getText());

            changeForm.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {
        loadForm("/View/paymentTableForm.fxml");
    }

    @FXML
    void btnLessonsOnAction(ActionEvent event) {
        loadForm("/View/lessonForm.fxml");
    }

    @FXML
    void btSettingsOnAction(ActionEvent event) {
        loadForm("/View/UserForm.fxml");
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
            e.printStackTrace();
        }
    }

    private void loadForm(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane pane = loader.load();
            changeForm.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML: " + fxmlPath);
        }
    }

    public void setUserInfo(String userName, String role) {
        lblUserName.setText(userName);
        lblUserRole.setText(role);
        configureRoleAccess(role);
    }

    public void configureRoleAccess(String role) {
        if ("Admin".equalsIgnoreCase(role)) {
            btnDashboard.setDisable(false);
            btnProgram.setDisable(false);
            btnStudent.setDisable(false);
            btnInstructor.setDisable(false);
            btnPayment.setDisable(false);
            btnLessons.setDisable(false);
        } else if ("Receptionist".equalsIgnoreCase(role) || "Admissions Coordinator".equalsIgnoreCase(role)) {
            btnDashboard.setDisable(false);
            btnProgram.setDisable(true);
            btnStudent.setDisable(false);
            btnInstructor.setDisable(true);
            btnPayment.setDisable(false);
            btnLessons.setDisable(false);
        } else {
            btnDashboard.setDisable(true);
            btnProgram.setDisable(true);
            btnStudent.setDisable(true);
            btnInstructor.setDisable(true);
            btnPayment.setDisable(true);
            btnLessons.setDisable(true);
        }
    }
}
