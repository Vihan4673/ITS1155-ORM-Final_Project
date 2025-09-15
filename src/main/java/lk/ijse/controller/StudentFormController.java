package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.tdm.StudentTm;
import lk.ijse.util.Regex;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class StudentFormController {

    @FXML private TextField txtSearch, txtAddress, txtId, txtName, txtTel;
    @FXML private TableColumn<?, ?> colAddress, colId, colName, colRegisterDate, colTel;
    @FXML private DatePicker registerDatePicker;
    @FXML private AnchorPane studentForm;
    @FXML private TableView<StudentTm> tblStudent;

    private final StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);

    public void initialize() {
        setCellValueFactory();
        loadAllStudent();
        generateStudentId();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
    }

    private void loadAllStudent() {
        List<StudentDTO> allStudent = studentBO.getAllStudent();
        ObservableList<StudentTm> studentTms = FXCollections.observableArrayList();

        for (StudentDTO s : allStudent) {
            studentTms.add(new StudentTm(
                    s.getStudentId(),
                    s.getName(),
                    s.getAddress(),
                    s.getTel(),
                    s.getRegistrationDate(),
                    null
            ));
        }
        tblStudent.setItems(studentTms);
    }

    private void clearData() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtTel.clear();
        registerDatePicker.setValue(null);
        generateStudentId();
    }

    private StudentDTO getObject() {
        if (registerDatePicker.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a registration date!").show();
            return null;
        }
        return new StudentDTO(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Long.parseLong(txtTel.getText()),
                Date.valueOf(registerDatePicker.getValue())
        );
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (isValidStudent()) {
            StudentDTO dto = getObject();
            if (dto == null) return;

            studentBO.saveStudent(dto);
            clearData();
            loadAllStudent();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/paymentForm.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Payment Form");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to open Payment Form!").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter all fields!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (isValidStudent()) {
            StudentDTO dto = getObject();
            if (dto == null) return;

            studentBO.updateStudent(dto);
            clearData();
            loadAllStudent();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (isValidStudent()) {
            StudentDTO dto = getObject();
            if (dto == null) return;

            studentBO.deleteStudent(dto);
            loadAllStudent();
            clearData();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearData();
    }

    @FXML
    void tblStudentOnClickAction(MouseEvent event) {
        StudentTm selectedItem = tblStudent.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtId.setText(selectedItem.getStudentId());
            txtName.setText(selectedItem.getName());
            txtAddress.setText(selectedItem.getAddress());
            txtTel.setText(String.valueOf(selectedItem.getTel()));
            registerDatePicker.setValue(selectedItem.getRegistrationDate().toLocalDate());
        }
    }

    public boolean isValidStudent() {
        if (!Regex.setTextColor(lk.ijse.util.TextField.STUDENTID, txtId)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.NAME, txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtAddress)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.TEL, txtTel)) return false;
        return !(txtId.getText().isEmpty() || registerDatePicker.getValue() == null);
    }

    @FXML void txtAddressKeyAction(KeyEvent e) { Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtAddress); }
    @FXML void txtNameKeyAction(KeyEvent e) { Regex.setTextColor(lk.ijse.util.TextField.NAME, txtName); }
    @FXML void txtTelKeyAction(KeyEvent e) { Regex.setTextColor(lk.ijse.util.TextField.TEL, txtTel); }
    @FXML void txtIdKeyAction(KeyEvent e) { Regex.setTextColor(lk.ijse.util.TextField.STUDENTID, txtId); }

    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase();
        List<StudentDTO> allStudent = studentBO.getAllStudent();
        ObservableList<StudentTm> filteredList = FXCollections.observableArrayList();

        for (StudentDTO s : allStudent) {
            if (s.getStudentId().toLowerCase().contains(searchText) ||
                    s.getName().toLowerCase().contains(searchText)) {
                filteredList.add(new StudentTm(
                        s.getStudentId(),
                        s.getName(),
                        s.getAddress(),
                        s.getTel(),
                        s.getRegistrationDate(),
                        null
                ));
            }
        }
        tblStudent.setItems(filteredList);
    }

    private void generateStudentId() {
        String newId = studentBO.generateNewId();
        txtId.setText(newId);
        txtId.setEditable(false);
    }

    public void txtIdOnAction(ActionEvent e) {}
    public void txtNameOnAction(ActionEvent e) {}
    public void txtAddressOnAction(ActionEvent e) {}
}
