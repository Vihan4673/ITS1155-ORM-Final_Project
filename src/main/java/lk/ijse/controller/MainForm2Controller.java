package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;

public class MainForm2Controller {

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnProgram;

    @FXML
    private JFXButton btnSetting;

    @FXML
    private JFXButton btnStudent;

    @FXML
    private JFXButton btnView;

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
            // Default load dashboard
            changeForm.getChildren().setAll((Node) FXMLLoader.load(this.getClass().getResource("/dashboard.fxml")));
            highlightButton(btnDashboard);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        loadForm("/dashboard.fxml", btnDashboard);
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

    /** Utility method to load forms & highlight active button */
    private void loadForm(String fxmlPath, JFXButton activeButton) {
        try {
            changeForm.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource(fxmlPath)));
            resetButtonStyles();
            highlightButton(activeButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void highlightButton(JFXButton button){
        button.setStyle("-fx-background-color: #468A9A; -fx-text-fill: #FFFFFF; -fx-border-color: #FFFFFF; -fx-border-width: 3; -fx-border-radius: 5; -fx-background-radius: 10;");
    }

    private void resetButtonStyles(){
        String style = "-fx-background-color: #26667F; -fx-text-fill: #000000; -fx-border-color: #FFFFFF; -fx-border-width: 3; -fx-border-radius: 5; -fx-background-radius: 10;";
        btnDashboard.setStyle(style);
        btnProgram.setStyle(style);
        btnStudent.setStyle(style);


        btnInstructor.setStyle(style);
        btnPayment.setStyle(style);
        btnLessons.setStyle(style);
    }


    public void btnViewOnAction(ActionEvent actionEvent) {
    }

    public void btnSettingOnAction(ActionEvent actionEvent) {
        loadForm("/settingForm.fxml", btnSetting);
    }
}
