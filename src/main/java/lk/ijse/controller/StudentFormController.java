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
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.CourseBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.tdm.StudentTm;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StudentFormController {

    @FXML private TextField txtSearch, txtId, txtName, txtAddress, txtTel, txtEmail;
    @FXML private DatePicker registerDatePicker;
    @FXML private ListView<String> listCourses;
    @FXML private TableView<StudentTm> tblStudent;
    @FXML private TableColumn<StudentTm, String> colId;
    @FXML private TableColumn<StudentTm, String> colName;
    @FXML private TableColumn<StudentTm, String> colAddress;
    @FXML private TableColumn<StudentTm, Long> colTel;
    @FXML private TableColumn<StudentTm, String> colEmail;
    @FXML private TableColumn<StudentTm, Date> colRegisterDate;
    @FXML private TableColumn<StudentTm, String> colCourses;

    private final StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);
    private final CourseBO courseBO = (CourseBO) BOFactory.getBO(BOFactory.BOType.COURSE);
    private List<CourseDTO> allCourses;

    public void initialize() {
        setCellValueFactory();
        loadAllCourses();
        loadAllStudent();
        generateStudentId();
        listCourses.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        colCourses.setCellValueFactory(new PropertyValueFactory<>("coursesString"));
    }

    private void generateStudentId() {
        try {
            String newId = studentBO.generateNewId();
            txtId.setText(newId);
            txtId.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate new Student ID!").show();
        }
    }

    private void clearData() {
        generateStudentId();
        txtName.clear();
        txtAddress.clear();
        txtTel.clear();
        txtEmail.clear();
        registerDatePicker.setValue(null);
        listCourses.getSelectionModel().clearSelection();
    }

    private void loadAllCourses() {
        try {
            allCourses = courseBO.getAllCourses();
            ObservableList<String> courseNames = FXCollections.observableArrayList(
                    allCourses.stream().map(CourseDTO::getProgramName).collect(Collectors.toList())
            );
            listCourses.setItems(courseNames);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load courses!").show();
        }
    }

    private boolean validateInput() {
        if (txtName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtTel.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || registerDatePicker.getValue() == null ||
                listCourses.getSelectionModel().getSelectedItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Fill all fields & select at least one course!").show();
            return false;
        }

        if (!Pattern.matches("\\d{10}", txtTel.getText())) {
            new Alert(Alert.AlertType.WARNING, "Telephone must be 10 digits!").show();
            return false;
        }

        if (!Pattern.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$", txtEmail.getText())) {
            new Alert(Alert.AlertType.WARNING, "Invalid email format!").show();
            return false;
        }
        return true;
    }

    private StudentDTO getObject() {
        if (!validateInput()) return null;

        // ⚡ ensure studentId is set
        String studentId = txtId.getText();
        if (studentId == null || studentId.isEmpty()) {
            try {
                studentId = studentBO.generateNewId(); // generate new ID
                txtId.setText(studentId); // update UI
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to generate Student ID!").show();
                return null;
            }
        }

        long tel = Long.parseLong(txtTel.getText());

        List<String> selectedCourseIds = listCourses.getSelectionModel().getSelectedItems().stream()
                .map(name -> allCourses.stream()
                        .filter(c -> c.getProgramName().equals(name))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Course not found: " + name))
                        .getProgramId()
                ).toList();

        return new StudentDTO(
                studentId,
                txtName.getText(),
                txtAddress.getText(),
                tel,
                txtEmail.getText(),
                Date.valueOf(registerDatePicker.getValue()),
                selectedCourseIds
        );
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        StudentDTO dto = getObject();
        if (dto == null) return;

        try {
            // ⚡ Save student
            studentBO.saveStudent(dto);

            new Alert(Alert.AlertType.INFORMATION, "Student Saved Successfully!").show();

            clearData();
            loadAllStudent();

            openPaymentForm(dto);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to save student!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        StudentDTO dto = getObject();
        if (dto == null) return;

        try {
            studentBO.updateStudent(dto);
            new Alert(Alert.AlertType.INFORMATION, "Student Updated Successfully!").show();
            clearData();
            loadAllStudent();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to update student!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select a student to delete!").show();
            return;
        }
        try {
            studentBO.deleteStudent(txtId.getText());
            new Alert(Alert.AlertType.INFORMATION, "Student Deleted Successfully!").show();
            clearData();
            loadAllStudent();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to delete student!").show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearData();
    }

    @FXML
    void tblStudentOnClickAction(MouseEvent event) {
        StudentTm selected = tblStudent.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtId.setText(selected.getStudentId());
            txtName.setText(selected.getName());
            txtAddress.setText(selected.getAddress());
            txtTel.setText(String.valueOf(selected.getTel()));
            txtEmail.setText(selected.getEmail());
            registerDatePicker.setValue(selected.getRegistrationDate().toLocalDate());
            listCourses.getSelectionModel().clearSelection();

            for (String courseId : selected.getEnrolledCourseIds()) {
                allCourses.stream()
                        .filter(c -> c.getProgramId().equals(courseId))
                        .findFirst()
                        .ifPresent(c -> listCourses.getSelectionModel().select(c.getProgramName()));
            }
        }
    }

    @FXML
    void txtSearchKeyReleased(KeyEvent event) {
        String searchText = txtSearch.getText().toLowerCase();
        ObservableList<StudentTm> filteredList = FXCollections.observableArrayList();

        try {
            List<StudentDTO> allStudent = studentBO.getAllStudent();

            for (StudentDTO dto : allStudent) {
                if (dto.getStudentId().toLowerCase().contains(searchText) ||
                        dto.getName().toLowerCase().contains(searchText) ||
                        (dto.getEmail() != null && dto.getEmail().toLowerCase().contains(searchText))) {

                    List<CourseDTO> courseList = dto.getEnrolledCourseIds().stream()
                            .map(id -> allCourses.stream()
                                    .filter(c -> c.getProgramId().equals(id))
                                    .findFirst()
                                    .map(c -> new CourseDTO(c.getProgramId(), c.getProgramName(), c.getDuration(), c.getFee(), ""))
                                    .orElse(new CourseDTO(id, "", 0, 0.0, ""))
                            ).toList();

                    filteredList.add(new StudentTm(
                            dto.getStudentId(),
                            dto.getName(),
                            dto.getAddress(),
                            dto.getTel() != null ? dto.getTel() : 0L,
                            dto.getEmail() != null ? dto.getEmail() : "",
                            dto.getRegistrationDate(),
                            courseList
                    ));
                }
            }
            tblStudent.setItems(filteredList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load students!").show();
        }
    }

    private void loadAllStudent() {
        ObservableList<StudentTm> studentTms = FXCollections.observableArrayList();
        try {
            List<StudentDTO> allStudent = studentBO.getAllStudent();

            for (StudentDTO dto : allStudent) {
                List<CourseDTO> courseList = dto.getEnrolledCourseIds().stream()
                        .map(id -> allCourses.stream()
                                .filter(c -> c.getProgramId().equals(id))
                                .findFirst()
                                .map(c -> new CourseDTO(c.getProgramId(), c.getProgramName(), c.getDuration(), c.getFee(), ""))
                                .orElse(new CourseDTO(id, "", 0, 0.0, ""))
                        ).toList();

                studentTms.add(new StudentTm(
                        dto.getStudentId(),
                        dto.getName(),
                        dto.getAddress(),
                        dto.getTel() != null ? dto.getTel() : 0L,
                        dto.getEmail() != null ? dto.getEmail() : "",
                        dto.getRegistrationDate(),
                        courseList
                ));
            }
            tblStudent.setItems(studentTms);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load students!").show();
        }
    }

    private void openPaymentForm(StudentDTO student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/paymentForm.fxml"));
            Parent root = loader.load();
            PaymentFormController controller = loader.getController();
            controller.setStudentAndCourses(student.getStudentId(),
                    student.getEnrolledCourseIds().stream()
                            .map(id -> allCourses.stream()
                                    .filter(c -> c.getProgramId().equals(id))
                                    .findFirst()
                                    .orElseThrow())
                            .toList());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Payment Form");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open Payment Form!").show();
        }
    }
}
