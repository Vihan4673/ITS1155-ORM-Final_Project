package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    private TableView<UserTm> tblUser;

    @FXML
    private AnchorPane settingForm;
    @FXML
    private TextField txtUserName;
    @FXML
    private PasswordField txtNewPassword;

    private final SettingBO settingBO = (SettingBO) BOFactory.getBO(BOFactory.BOType.SETTING);
    private List<UserDTO> allUsers;

    public void initialize() {
        setCellValueFactory();
        loadAllUsers();

        tblUser.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtUserName.setText(newSelection.getUserName());
                txtNewPassword.clear();
            }
        });
    }

    private void setCellValueFactory() {
        colUserName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("userName"));
        colUserRole.setCellValueFactory(param -> {
            String role = param.getValue().getRole();
            if ("AdmissionCoordinator".equalsIgnoreCase(role)) {
                role = "Receptionist";
            }
            return new javafx.beans.property.SimpleStringProperty(role);
        });
        colDelete.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("delete"));
    }

    private void loadAllUsers() {
        if (!"Admin".equalsIgnoreCase(LoginFormController.userDTO.getRole())) return;

        ObservableList<UserTm> userTms = FXCollections.observableArrayList();
        allUsers = settingBO.getAllUsers();

        for (UserDTO userDTO : allUsers) {
            userTms.add(new UserTm(
                    userDTO.getUserName(),
                    userDTO.getRole(),
                    createDeleteButton(userDTO)
            ));
        }
        tblUser.setItems(userTms);
    }

    private Button createDeleteButton(UserDTO userDTO) {
        Button button = new Button("Delete");
        button.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand;");

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
        String newPassword = txtNewPassword.getText().trim();

        if (newUserName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Username cannot be empty!").show();
            return;
        }

        String hashedPassword = LoginFormController.userDTO.getPassword();

        if (!newPassword.isEmpty()) {
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

            LoginFormController.userDTO.setUserName(newUserName);
            LoginFormController.userDTO.setPassword(hashedPassword);

            loadAllUsers();
            txtNewPassword.clear();

            new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
        }
    }
}
