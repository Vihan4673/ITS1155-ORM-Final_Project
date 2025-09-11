package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.LoginBO;
import lk.ijse.dto.UserDTO;
import lk.ijse.exception.ExceptionHandler;
import lk.ijse.exception.InvalidCredentialsException;
import lk.ijse.util.PasswordStorage;

import java.io.IOException;

public class LoginFormController {

    @FXML
    private AnchorPane fullLoginForm;

    @FXML
    private TextField inputPassword;

    @FXML
    private TextField inputUserName;

    @FXML
    private AnchorPane loginForm;

    LoginBO loginBO = (LoginBO) BOFactory.getBO(BOFactory.BOType.LOGIN);

    public static UserDTO userDTO;

    @FXML
    void loginOnAction(ActionEvent event) {
        if (!inputUserName.getText().isEmpty() && !inputPassword.getText().isEmpty()) {
            try {
                UserDTO loginUser = loginBO.getUser(inputUserName.getText().trim());

                if (PasswordStorage.checkPassword(inputPassword.getText().trim(), loginUser.getPassword())) {
                    userDTO = loginUser;
                    openMainForm(loginUser.getRole());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid User Password !!").show();
                }
            } catch (InvalidCredentialsException e) {
                ExceptionHandler.handleException(e);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please Enter All Fields !!").show();
        }
    }

    private void openMainForm(String role) {
        try {
            String fxmlFile;

            if ("Admin".equalsIgnoreCase(role)) {
                fxmlFile = "/mainForm.fxml";  // Admin → mainForm
            } else if ("Admissions Coordinator".equalsIgnoreCase(role)) {
                fxmlFile = "/mainForm2.fxml"; // Coordinator → mainForm2
            } else {
                new Alert(Alert.AlertType.ERROR, "Unknown role: " + role).show();
                return;
            }

            Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource(fxmlFile)));
            Stage stage = (Stage) fullLoginForm.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void goToSignUpOnAction(MouseEvent event) {
        try {
            loginForm.getChildren().setAll((Node) FXMLLoader.load(this.getClass().getResource("/signUpForm.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void inputPasswordOnAction(ActionEvent event) {
        loginOnAction(event);
    }

    @FXML
    void inputUserNameOnAction(ActionEvent event) {
        inputPassword.requestFocus();
    }

    // FIX: Add missing btnViewOnAction to prevent FXML error
    @FXML
    void btnViewOnAction(ActionEvent event) {
        System.out.println("View button clicked!");
        new Alert(Alert.AlertType.INFORMATION, "View Button Clicked!").show();
    }
}
