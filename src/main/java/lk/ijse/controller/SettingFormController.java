package lk.ijse.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.SettingBO;
import lk.ijse.dto.UserDTO;
import lk.ijse.tdm.UserTm;
import lk.ijse.util.PasswordStorage;

import java.util.List;
import java.util.Optional;

public class SettingFormController {

    @FXML
    private TableColumn<?, ?> colDelete;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private AnchorPane settingForm;

    @FXML
    private TableView<UserTm> tblUser;

    @FXML
    private TableColumn<?, ?> colUserRole;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    @FXML
    private Pane visiblePane;

    SettingBO settingBO = (SettingBO) BOFactory.getBO(BOFactory.BOType.SETTING);

    List<UserDTO> allUsers;

    public void initialize() {
        txtNewPassword.setVisible(false);
        txtConfirmPassword.setVisible(false);
        txtUserName.setText(LoginFormController.userDTO.getUserName());

        if (!LoginFormController.userDTO.getRole().equals("Admin")) {
            visiblePane.setVisible(false);
        }

        setCellValueFactory();
        loadAllUsers();
    }

    private void setCellValueFactory() {
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    private void loadAllUsers() {
        tblUser.getItems().clear();
        ObservableList<UserTm> userTms = tblUser.getItems();
        allUsers = settingBO.getAllUsers();

        for (UserDTO userDTO : allUsers) {
            userTms.add(new UserTm(userDTO.getUserName(), userDTO.getRole(), createButton()));
        }
        tblUser.setItems(userTms);
    }

    private Button createButton() {
        Button button = new Button("Delete");
        button.setStyle("-fx-background-color: red;-fx-text-fill: white;");

        button.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to remove?", yes, no).showAndWait();

            if (type.orElse(no) == yes) {
                int selectedIndex = tblUser.getSelectionModel().getSelectedIndex();
                try {
                    settingBO.deleteUser(allUsers.get(selectedIndex));
                    loadAllUsers();
                } catch (Exception exception) {
                    new Alert(Alert.AlertType.ERROR, "Select a valid row to remove!").show();
                }
                tblUser.refresh();
            }
        });

        return button;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String newUserName = txtUserName.getText().trim();
        String currentPassword = txtPassword.getText().trim();
        String newPassword = txtNewPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        if (newUserName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Username cannot be empty!").show();
            return;
        }

        String hashedPassword = LoginFormController.userDTO.getPassword(); // default: old password

        // If user wants to change password
        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Enter your current password to change password!").show();
                return;
            }

            if (!PasswordStorage.checkPassword(currentPassword, LoginFormController.userDTO.getPassword())) {
                new Alert(Alert.AlertType.ERROR, "Incorrect Current Password!").show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                new Alert(Alert.AlertType.ERROR, "New Password and Confirm Password do not match!").show();
                return;
            }

            hashedPassword = PasswordStorage.hashPassword(newPassword);
        }

        try {
            UserDTO userDTO = new UserDTO(
                    LoginFormController.userDTO.getUserId(),
                    newUserName,
                    hashedPassword,
                    LoginFormController.userDTO.getRole()
            );
            settingBO.updateUser(userDTO);

            // Update current user info in LoginFormController
            LoginFormController.userDTO.setUserName(newUserName);
            LoginFormController.userDTO.setPassword(hashedPassword);

            loadAllUsers();

            txtPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();

            new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
        }
    }

    @FXML
    void txtNewPasswordOnAction(ActionEvent event) {
        txtConfirmPassword.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        // Show new password fields only if current password is correct
        if (PasswordStorage.checkPassword(txtPassword.getText().trim(), LoginFormController.userDTO.getPassword())) {
            txtNewPassword.setVisible(true);
            txtConfirmPassword.setVisible(true);
            txtNewPassword.requestFocus();
        } else {
            new Alert(Alert.AlertType.ERROR, "Incorrect Password!").show();
        }
    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }
}
