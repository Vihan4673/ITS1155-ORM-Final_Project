package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.InstructorBO;
import lk.ijse.dto.InstructorDTO;

public class InstructorFormController {

    @FXML private TextField txtSearch;
    @FXML private TextField txtInstructorId;
    @FXML private TextField txtName;
    @FXML private TextField txtSpecialization;

    @FXML private TableView<InstructorDTO> tblInstructor;
    @FXML private TableColumn<InstructorDTO, String> colId;
    @FXML private TableColumn<InstructorDTO, String> colName;
    @FXML private TableColumn<InstructorDTO, String> colSpecialization;

    @FXML private JFXButton btnSave, btnUpdate, btnDelete, btnClear;

    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBO(BOFactory.BOType.INSTRUCTOR);

    private String currentUserRole;
    private boolean roleSetBeforeInit = false;

    /** Receive role from DashboardController */
    public void setUserRole(String role) {
        this.currentUserRole = role;

        // If buttons initialized, apply restrictions immediately
        if (btnSave != null) {
            applyRoleRestrictions();
        } else {
            roleSetBeforeInit = true;
        }
    }

    @FXML
    public void initialize() {
        // Table binding
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getInstructorId()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colSpecialization.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpecialization()));

        // Load data & set ID
        loadAllInstructors();
        txtInstructorId.setText(instructorBO.generateNewId());
        txtInstructorId.setEditable(false);

        // Apply role restrictions if role was set before initialize
        if (roleSetBeforeInit && currentUserRole != null) {
            applyRoleRestrictions();
        }
    }

    /** Apply role-based restrictions */
    private void applyRoleRestrictions() {
        if ("RECEPTIONIST".equalsIgnoreCase(currentUserRole)) {
            btnSave.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            btnClear.setDisable(true);

            txtName.setEditable(false);
            txtSpecialization.setEditable(false);

            // Table view read-only
            tblInstructor.setOnMouseClicked(event -> event.consume());
        }
    }

    /** Load all instructors */
    private void loadAllInstructors() {
        ObservableList<InstructorDTO> list = FXCollections.observableArrayList(instructorBO.getAllInstructors());
        tblInstructor.setItems(list);
    }

    /** Save new instructor */
    @FXML
    private void btnSaveOnAction() {
        if (txtName.getText().isEmpty() || txtSpecialization.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Name and Specialization cannot be empty!");
            return;
        }
        InstructorDTO dto = new InstructorDTO(txtInstructorId.getText(), txtName.getText(), txtSpecialization.getText());
        if (instructorBO.saveInstructor(dto)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Instructor saved successfully!");
            reloadAndClear();
        }
    }

    /** Update selected instructor */
    @FXML
    private void btnUpdateOnAction() {
        if (txtInstructorId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an Instructor to update!");
            return;
        }
        InstructorDTO dto = new InstructorDTO(txtInstructorId.getText(), txtName.getText(), txtSpecialization.getText());
        if (instructorBO.updateInstructor(dto)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Instructor updated successfully!");
            reloadAndClear();
        }
    }

    /** Delete selected instructor */
    @FXML
    private void btnDeleteOnAction() {
        if (txtInstructorId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select an Instructor to delete!");
            return;
        }
        if (instructorBO.deleteInstructor(txtInstructorId.getText())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Instructor deleted successfully!");
            reloadAndClear();
        }
    }

    /** Clear input fields */
    @FXML
    private void btnClearOnAction() {
        clearFields();
    }

    private void clearFields() {
        txtName.clear();
        txtSpecialization.clear();
        txtInstructorId.setText(instructorBO.generateNewId());
    }

    /** Populate fields when row clicked */
    @FXML
    private void tblInstructorOnClickAction() {
        InstructorDTO dto = tblInstructor.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtInstructorId.setText(dto.getInstructorId());
            txtName.setText(dto.getName());
            txtSpecialization.setText(dto.getSpecialization());
        }
    }

    /** Filter instructors while typing */
    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase();
        ObservableList<InstructorDTO> allInstructors = FXCollections.observableArrayList(instructorBO.getAllInstructors());
        ObservableList<InstructorDTO> filteredList = FXCollections.observableArrayList();

        for (InstructorDTO dto : allInstructors) {
            if (dto.getInstructorId().toLowerCase().contains(searchText) ||
                    dto.getName().toLowerCase().contains(searchText)) {
                filteredList.add(dto);
            }
        }
        tblInstructor.setItems(filteredList);
    }

    /** Helper methods */
    private void reloadAndClear() {
        loadAllInstructors();
        clearFields();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
