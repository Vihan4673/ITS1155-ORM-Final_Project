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
import lk.ijse.dto.*;
import lk.ijse.tdm.LessonTm;

import java.time.LocalDate;
import java.util.List;

public class LessonFormController {

    @FXML private TextField txtSearch;
    @FXML private TableView<LessonTm> tblLesson;
    @FXML private TableColumn<LessonTm, String> colLessonId;
    @FXML private TableColumn<LessonTm, String> colStudent;
    @FXML private TableColumn<LessonTm, String> colCourse;
    @FXML private TableColumn<LessonTm, String> colInstructor;
    @FXML private TableColumn<LessonTm, LocalDate> colDate;
    @FXML private TableColumn<LessonTm, String> colTime;
    @FXML private TableColumn<LessonTm, Integer> colDuration;

    @FXML private TextField txtLessonId;
    @FXML private DatePicker dateLesson;
    @FXML private ComboBox<String> cmbStudent;
    @FXML private ComboBox<String> cmbCourse;
    @FXML private ComboBox<String> cmbInstructor;
    @FXML private TextField txtDuration;
    @FXML private Spinner<Integer> spinnerHour;
    @FXML private Spinner<Integer> spinnerMinute;

    private final LessonBO lessonBO = (LessonBO) BOFactory.getBO(BOFactory.BOType.LESSON);
    private final StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);
    private final CourseBO courseBO = (CourseBO) BOFactory.getBO(BOFactory.BOType.COURSE);
    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBO(BOFactory.BOType.INSTRUCTOR);

    public void initialize() {
        txtLessonId.setText(lessonBO.generateNextLessonId());
        setCellValueFactory();
        loadAllLessons();
        loadComboBoxes();
        initializeSpinners();
    }

    private void initializeSpinners() {
        spinnerHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        spinnerHour.setEditable(true);

        spinnerMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        spinnerMinute.setEditable(true);
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

    private void loadAllLessons() {
        List<LessonDTO> allLessons = lessonBO.getAllLesson();
        ObservableList<LessonTm> lessonTms = FXCollections.observableArrayList();
        for (LessonDTO dto : allLessons) {
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
        cmbStudent.getItems().clear();
        cmbCourse.getItems().clear();
        cmbInstructor.getItems().clear();

        try {
            List<StudentDTO> students = studentBO.getAllStudent();
            List<CourseDTO> courses = courseBO.getAllCourses();
            List<InstructorDTO> instructors = instructorBO.getAllInstructors();

            students.forEach(s -> cmbStudent.getItems().add(s.getStudentId()));
            courses.forEach(c -> cmbCourse.getItems().add(c.getProgramId()));
            instructors.forEach(i -> cmbInstructor.getItems().add(i.getInstructorId()));

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load combo boxes!").show();
        }
    }


    private LessonDTO getLessonFromFields() {
        String lessonTime = String.format("%02d:%02d:00", spinnerHour.getValue(), spinnerMinute.getValue());
        return new LessonDTO(
                txtLessonId.getText(),
                cmbStudent.getValue(),
                cmbCourse.getValue(),
                cmbInstructor.getValue(),
                dateLesson.getValue(),
                lessonTime,
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
        spinnerHour.getValueFactory().setValue(9);
        spinnerMinute.getValueFactory().setValue(0);
        txtDuration.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        lessonBO.deleteLesson(getLessonFromFields());
        clearData();
        loadAllLessons();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        lessonBO.saveLesson(getLessonFromFields());
        clearData();
        loadAllLessons();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        lessonBO.updateLesson(getLessonFromFields());
        clearData();
        loadAllLessons();
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

            String[] timeParts = selected.getLessonTime().split(":");
            spinnerHour.getValueFactory().setValue(Integer.parseInt(timeParts[0]));
            spinnerMinute.getValueFactory().setValue(Integer.parseInt(timeParts[1]));

            txtDuration.setText(String.valueOf(selected.getDuration()));
        }
    }

    @FXML
    public void txtSearchKeyReleased(KeyEvent event) {
        String searchText = txtSearch.getText().toLowerCase();
        List<LessonDTO> allLessons = lessonBO.getAllLesson();
        ObservableList<LessonTm> filteredList = FXCollections.observableArrayList();

        for (LessonDTO dto : allLessons) {
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
