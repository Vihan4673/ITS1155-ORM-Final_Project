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

    public TextField txtSearch;
    @FXML
    private TextField txtInstructorId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSpecialization;
    @FXML
    private TableView<InstructorDTO> tblInstructor;
    @FXML
    private TableColumn<InstructorDTO, String> colId;
    @FXML
    private TableColumn<InstructorDTO, String> colName;
    @FXML
    private TableColumn<InstructorDTO, String> colSpecialization;
    @FXML
    private JFXButton btnSave, btnUpdate, btnDelete, btnClear;

    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBO(BOFactory.BOType.INSTRUCTOR);

    @FXML
    public void initialize() {
        // TableColumn binding
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getInstructorId()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colSpecialization.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpecialization()));

        // Load all instructors
        loadAllInstructors();

        // Auto-generate next ID
        txtInstructorId.setText(instructorBO.generateNewId());
        txtInstructorId.setEditable(false); // ID field not editable
    }

    private void loadAllInstructors() {
        ObservableList<InstructorDTO> list = FXCollections.observableArrayList(instructorBO.getAllInstructors());
        tblInstructor.setItems(list);
    }

    @FXML
    private void btnSaveOnAction() {
        if (txtName.getText().isEmpty() || txtSpecialization.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Name and Specialization cannot be empty!").show();
            return;
        }

        InstructorDTO dto = new InstructorDTO(txtInstructorId.getText(), txtName.getText(), txtSpecialization.getText());
        if (instructorBO.saveInstructor(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Instructor saved successfully!").show();
            loadAllInstructors();
            clearFields();
        }
    }

    @FXML
    private void btnUpdateOnAction() {
        if (txtInstructorId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an Instructor to update!").show();
            return;
        }

        InstructorDTO dto = new InstructorDTO(txtInstructorId.getText(), txtName.getText(), txtSpecialization.getText());
        if (instructorBO.updateInstructor(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Instructor updated successfully!").show();
            loadAllInstructors();
            clearFields();
        }
    }

    @FXML
    private void btnDeleteOnAction() {
        if (txtInstructorId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an Instructor to delete!").show();
            return;
        }

        if (instructorBO.deleteInstructor(txtInstructorId.getText())) {
            new Alert(Alert.AlertType.INFORMATION, "Instructor deleted successfully!").show();
            loadAllInstructors();
            clearFields();
        }
    }

    @FXML
    private void btnClearOnAction() {
        clearFields();
    }

    @FXML
    private void tblInstructorOnClickAction() {
        InstructorDTO dto = tblInstructor.getSelectionModel().getSelectedItem();
        if (dto != null) {
            txtInstructorId.setText(dto.getInstructorId());
            txtName.setText(dto.getName());
            txtSpecialization.setText(dto.getSpecialization());
        }
    }

    private void clearFields() {
        txtName.clear();
        txtSpecialization.clear();
        txtInstructorId.setText(instructorBO.generateNewId());
    }

    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase(); // search text


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

}
