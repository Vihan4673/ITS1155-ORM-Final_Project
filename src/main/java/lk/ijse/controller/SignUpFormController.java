package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.SignUpBO;
import lk.ijse.dto.UserDTO;
import lk.ijse.exception.ExceptionHandler;
import lk.ijse.exception.UserAlreadyExistsException;
import lk.ijse.util.PasswordStorage;

import java.io.IOException;

public class SignUpFormController {

    @FXML
    private AnchorPane signUpForm;

    @FXML
    private TextField inputUserName;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private CheckBox adminCheckBox;

    @FXML
    private CheckBox admissionCheckBox;

    private final SignUpBO signUpBO = (SignUpBO) BOFactory.getBO(BOFactory.BOType.SIGNUP);

    @FXML
    void signUpBtnOnAction(ActionEvent event) {
        if (isValid()){
            UserDTO userDTO = new UserDTO();
            userDTO.setUserName(inputUserName.getText().trim());
            userDTO.setPassword(PasswordStorage.hashPassword(inputPassword.getText().trim()));

            if (adminCheckBox.isSelected()) {
                userDTO.setRole("Admin");
            } else {
                userDTO.setRole("Admissions Coordinator");
            }

            try {
                signUpBO.signUp(userDTO);
                new Alert(Alert.AlertType.INFORMATION,"SignUp Successful!").show();
                clearFields();
            } catch (UserAlreadyExistsException e) {
                ExceptionHandler.handleException(e);
            }
        } else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields !!").show();
        }
    }

    @FXML
    void adminCheckBoxOnAction(ActionEvent event) {
        adminCheckBox.setSelected(true);
        admissionCheckBox.setSelected(false);
    }

    @FXML
    void admissionCheckBoxOnAction(ActionEvent event) {
        admissionCheckBox.setSelected(true);
        adminCheckBox.setSelected(false);
    }

    @FXML
    void backToLoginOnAction(javafx.scene.input.MouseEvent event) {
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/loginForm.fxml")));
            Stage stage = (Stage) signUpForm.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load login form!").show();
        }
    }

    @FXML
    void inputUserNameOnAction(ActionEvent event) {
        inputPassword.requestFocus();
    }

    private boolean isValid() {
        return !inputUserName.getText().isEmpty()
                && !inputPassword.getText().isEmpty()
                && (adminCheckBox.isSelected() || admissionCheckBox.isSelected());
    }

    private void clearFields() {
        inputUserName.clear();
        inputPassword.clear();
        adminCheckBox.setSelected(false);
        admissionCheckBox.setSelected(false);
    }
}
