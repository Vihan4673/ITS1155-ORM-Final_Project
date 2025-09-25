package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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
                    openMainForm(loginUser);
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

    private void openMainForm(UserDTO user) {
        try {

            String fxmlFile = "/View/Dashboardpage.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();


            DashboardController controller = loader.getController();
            controller.setUserInfo(user.getName(), user.getRole());
            controller.configureRoleAccess(user.getRole());

            Stage stage = (Stage) fullLoginForm.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Elite Driving School");
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot load dashboard!").show();
        }
    }


    @FXML
    void goToSignUpOnAction(ActionEvent event) {
        try {
            AnchorPane signUpPane = FXMLLoader.load(getClass().getResource("/View/signUpForm.fxml"));
            loginForm.getChildren().setAll(signUpPane);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Cannot load Sign Up form!").show();
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

    @FXML
    void btnViewOnAction(ActionEvent event) {
        System.out.println("View button clicked!");
        new Alert(Alert.AlertType.INFORMATION, "View Button Clicked!").show();
    }
}
