package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.CourseBO;
import lk.ijse.bo.custom.InstructorBO;
import lk.ijse.bo.custom.LessonBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.InstructorDTO;
import lk.ijse.dto.LessonDTO;
import lk.ijse.dto.StudentDTO;
import lk.ijse.dto.courseDTO;
import lk.ijse.tdm.LessonTm;

import java.time.LocalDate;
import java.util.List;

public class LessonFormController {

    public TextField txtSearch;
    @FXML
    private TableView<LessonTm> tblLesson;
    @FXML
    private TableColumn<LessonTm, String> colLessonId;
    @FXML
    private TableColumn<LessonTm, String> colStudent;
    @FXML
    private TableColumn<LessonTm, String> colCourse;
    @FXML
    private TableColumn<LessonTm, String> colInstructor;
    @FXML
    private TableColumn<LessonTm, LocalDate> colDate;
    @FXML
    private TableColumn<LessonTm, String> colTime;
    @FXML
    private TableColumn<LessonTm, Integer> colDuration;

    @FXML
    private TextField txtLessonId;
    @FXML
    private DatePicker dateLesson;
    @FXML
    private ComboBox<String> cmbStudent;
    @FXML
    private ComboBox<String> cmbCourse;
    @FXML
    private ComboBox<String> cmbInstructor;
    @FXML
    private TextField txtTime;
    @FXML
    private TextField txtDuration;

    LessonBO lessonBO = (LessonBO) BOFactory.getBO(BOFactory.BOType.LESSON);
    StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);
    CourseBO courseBO = (CourseBO) BOFactory.getBO(BOFactory.BOType.COURSE);
    InstructorBO instructorBO = (InstructorBO) BOFactory.getBO(BOFactory.BOType.INSTRUCTOR);

    public void initialize() {
        // Auto-generate Lesson ID
        txtLessonId.setText(lessonBO.generateNextLessonId());

        setCellValueFactory();
        loadAllLesson();
        loadComboBoxes();
    }

    private void setCellValueFactory() {
        colLessonId.setCellValueFactory(new PropertyValueFactory<>("lessonId"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("lessonDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("lessonTime"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    private void loadAllLesson() {
        List<LessonDTO> allLesson = lessonBO.getAllLesson();
        ObservableList<LessonTm> lessonTms = FXCollections.observableArrayList();
        for (LessonDTO dto : allLesson) {
            lessonTms.add(new LessonTm(
                    dto.getLessonId(),
                    dto.getStudentId(),
                    dto.getCourseId(),
                    dto.getInstructorId(),
                    dto.getLessonDate(),
                    dto.getLessonTime(),
                    dto.getDuration()
            ));
        }
        tblLesson.setItems(lessonTms);
    }

    private void loadComboBoxes() {
        List<StudentDTO> students = studentBO.getAllStudent();
        List<courseDTO> courses = courseBO.getAllCulinaryProgram();
        List<InstructorDTO> instructors = instructorBO.getAllInstructors();

        for (StudentDTO s : students) cmbStudent.getItems().add(s.getStudentId());
        for (courseDTO c : courses) cmbCourse.getItems().add(c.getProgramId());
        for (InstructorDTO i : instructors) cmbInstructor.getItems().add(i.getInstructorId());
    }

    private LessonDTO getObject() {
        return new LessonDTO(
                txtLessonId.getText(),
                cmbStudent.getValue(),
                cmbCourse.getValue(),
                cmbInstructor.getValue(),
                dateLesson.getValue(),
                txtTime.getText(),
                Integer.parseInt(txtDuration.getText())
        );
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearData();
    }

    private void clearData() {
        txtLessonId.setText(lessonBO.generateNextLessonId());
        dateLesson.setValue(null);
        cmbStudent.getSelectionModel().clearSelection();
        cmbCourse.getSelectionModel().clearSelection();
        cmbInstructor.getSelectionModel().clearSelection();
        txtTime.clear();
        txtDuration.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        lessonBO.deleteLesson(getObject());
        clearData();
        loadAllLesson();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        lessonBO.saveLesson(getObject());
        clearData();
        loadAllLesson();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        lessonBO.updateLesson(getObject());
        clearData();
        loadAllLesson();
    }

    @FXML
    void tblLessonOnClickAction(MouseEvent event) {
        LessonTm selected = tblLesson.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtLessonId.setText(selected.getLessonId());
            dateLesson.setValue(selected.getLessonDate());
            cmbStudent.setValue(selected.getStudentId());
            cmbCourse.setValue(selected.getCourseId());
            cmbInstructor.setValue(selected.getInstructorId());
            txtTime.setText(selected.getLessonTime());
            txtDuration.setText(String.valueOf(selected.getDuration()));
        }
    }

    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase();


        List<LessonDTO> allLesson = lessonBO.getAllLesson();
        ObservableList<LessonTm> filteredList = FXCollections.observableArrayList();

        for (LessonDTO dto : allLesson) {
            if (dto.getLessonId().toLowerCase().contains(searchText) ||
                    dto.getStudentId().toLowerCase().contains(searchText) ||
                    dto.getCourseId().toLowerCase().contains(searchText) ||
                    dto.getInstructorId().toLowerCase().contains(searchText)) {

                filteredList.add(new LessonTm(
                        dto.getLessonId(),
                        dto.getStudentId(),
                        dto.getCourseId(),
                        dto.getInstructorId(),
                        dto.getLessonDate(),
                        dto.getLessonTime(),
                        dto.getDuration()
                ));
            }
        }

        tblLesson.setItems(filteredList);
    }

}
