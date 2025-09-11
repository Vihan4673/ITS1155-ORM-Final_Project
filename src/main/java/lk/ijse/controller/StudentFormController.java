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

    public TextField txtSearch;
    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colRegisterDate;

    @FXML
    private TableColumn<?, ?> colTel;

    @FXML
    private DatePicker registerDatePicker;

    @FXML
    private AnchorPane studentForm;

    @FXML
    private TableView<StudentTm> tblStudent;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTel;

    StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);

    public void initialize() {
        setCellValueFactory();
        loadAllStudent();
        generateStudentId();
    }

    private void loadAllStudent() {
        List<StudentDTO> allStudent = studentBO.getAllStudent();
        ObservableList<StudentTm> studentTms = FXCollections.observableArrayList();

        for (StudentDTO studentDTO : allStudent) {
            studentTms.add(new StudentTm(
                    studentDTO.getStudentId(),
                    studentDTO.getName(),
                    studentDTO.getAddress(),
                    studentDTO.getTel(),
                    studentDTO.getRegistrationDate(),
                    null
            ));
        }
        tblStudent.setItems(studentTms);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearData();
    }

    private void clearData() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtTel.clear();
        registerDatePicker.setValue(null);
    }

    private StudentDTO getObject() {
        return new StudentDTO(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                Long.parseLong(txtTel.getText()),
                Date.valueOf(registerDatePicker.getValue())
        );
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (isValidStudent()) {
            studentBO.deleteStudent(getObject());
            loadAllStudent();
            clearData();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (isValidStudent()) {
            studentBO.saveStudent(getObject());
            clearData();
            loadAllStudent();


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/paymentForm.fxml"));
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
            new Alert(Alert.AlertType.WARNING, "Please Enter All Fields !!").show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (isValidStudent()) {
            studentBO.updateStudent(getObject());
            clearData();
            loadAllStudent();
        }
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
        if (txtId.getText().isEmpty() || registerDatePicker.getValue() == null) return false;
        return true;
    }

    @FXML
    void txtAddressKeyAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.util.TextField.ADDRESS, txtAddress);
    }

    @FXML
    void txtNameKeyAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.util.TextField.NAME, txtName);
    }

    @FXML
    void txtTelKeyAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.util.TextField.TEL, txtTel);
    }

    @FXML
    void txtIdKeyAction(KeyEvent event) {
        Regex.setTextColor(lk.ijse.util.TextField.STUDENTID, txtId);
    }

    public void txtIdOnAction(ActionEvent actionEvent) {
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
    }

    public void txtAddressOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase(); // search text

        // Original list from DB
        List<StudentDTO> allStudent = studentBO.getAllStudent();
        ObservableList<StudentTm> filteredList = FXCollections.observableArrayList();

        for (StudentDTO studentDTO : allStudent) {
            if (studentDTO.getStudentId().toLowerCase().contains(searchText) ||
                    studentDTO.getName().toLowerCase().contains(searchText)) {

                filteredList.add(new StudentTm(
                        studentDTO.getStudentId(),
                        studentDTO.getName(),
                        studentDTO.getAddress(),
                        studentDTO.getTel(),
                        studentDTO.getRegistrationDate(),
                        null
                ));
            }
        }

        tblStudent.setItems(filteredList); // update TableView
    }

    private void generateStudentId() {
        String newId = studentBO.generateNewId();
        txtId.setText(newId);
        txtId.setEditable(false);
    }

}
