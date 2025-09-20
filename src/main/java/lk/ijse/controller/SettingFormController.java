package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private TableColumn<UserTm, String> colUserName;

    @FXML
    private TableColumn<UserTm, String> colUserRole;

    @FXML
    private TableColumn<UserTm, Button> colDelete;

    @FXML
    private AnchorPane settingForm;

    @FXML
    private TableView<UserTm> tblUser;

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

    private final SettingBO settingBO = (SettingBO) BOFactory.getBO(BOFactory.BOType.SETTING);
    private List<UserDTO> allUsers;

    public void initialize() {
        txtNewPassword.setVisible(false);
        txtConfirmPassword.setVisible(false);
        txtNewPassword.setDisable(true);
        txtConfirmPassword.setDisable(true);

        txtUserName.setText(LoginFormController.userDTO.getUserName());

        // Hide user management pane if not admin
        if (!"Admin".equals(LoginFormController.userDTO.getRole())) {
            visiblePane.setVisible(false);
        }

        setCellValueFactory();
        loadAllUsers();
    }

    private void setCellValueFactory() {
        colUserName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("userName"));
        colUserRole.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("role"));
        colDelete.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("delete"));
    }

    private void loadAllUsers() {
        ObservableList<UserTm> userTms = FXCollections.observableArrayList();
        allUsers = settingBO.getAllUsers();

        for (UserDTO userDTO : allUsers) {
            userTms.add(new UserTm(userDTO.getUserName(), userDTO.getRole(), createButton(userDTO)));
        }
        tblUser.setItems(userTms);
    }

    private Button createButton(UserDTO userDTO) {
        Button button = new Button("Delete");
        button.setStyle("-fx-background-color: red;-fx-text-fill: white; -fx-cursor: hand;");

        button.setOnAction(e -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete " + userDTO.getUserName() + "?",
                    yes, no).showAndWait();

            if (result.orElse(no) == yes) {
                try {
                    settingBO.deleteUser(userDTO);
                    loadAllUsers();
                    new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!").show();
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete user!").show();
                }
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

        String hashedPassword = LoginFormController.userDTO.getPassword();

        // If password change is requested
        if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter your current password first!").show();
                return;
            }

            if (!PasswordStorage.checkPassword(currentPassword, hashedPassword)) {
                new Alert(Alert.AlertType.ERROR, "Incorrect current password!").show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                new Alert(Alert.AlertType.ERROR, "New password and confirm password do not match!").show();
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

            // Update static user data
            LoginFormController.userDTO.setUserName(newUserName);
            LoginFormController.userDTO.setPassword(hashedPassword);

            loadAllUsers();

            txtPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();
            txtNewPassword.setVisible(false);
            txtConfirmPassword.setVisible(false);
            txtNewPassword.setDisable(true);
            txtConfirmPassword.setDisable(true);

            new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
        }
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        if (PasswordStorage.checkPassword(txtPassword.getText().trim(), LoginFormController.userDTO.getPassword())) {
            txtNewPassword.setVisible(true);
            txtConfirmPassword.setVisible(true);
            txtNewPassword.setDisable(false);
            txtConfirmPassword.setDisable(false);
            txtNewPassword.requestFocus();
        } else {
            new Alert(Alert.AlertType.ERROR, "Incorrect Current Password!").show();
        }
    }
}
