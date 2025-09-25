package lk.ijse.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
    @FXML private ListView<StudentDTO> listStudents;
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
        listStudents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initializeSpinners() {
        spinnerHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        spinnerHour.setEditable(true);

        spinnerMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        spinnerMinute.setEditable(true);
    }
    private void setCellValueFactory() {
        colLessonId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getLessonId()));
        colStudent.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getStudentId()));
        colCourse.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getCourseId()));
        colInstructor.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getInstructorId()));
        colDate.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLessonDate()));
        colTime.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getLessonTime()));
        colDuration.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDuration()));
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
        cmbCourse.getItems().clear();
        cmbInstructor.getItems().clear();
        listStudents.getItems().clear();

        try {
            List<StudentDTO> students = studentBO.getAllStudent();
            List<CourseDTO> courses = courseBO.getAllCourses();
            List<InstructorDTO> instructors = instructorBO.getAllInstructors();

            listStudents.getItems().addAll(students);
            courses.forEach(c -> cmbCourse.getItems().add(c.getProgramId()));
            instructors.forEach(i -> cmbInstructor.getItems().add(i.getInstructorId()));

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load combo boxes!").show();
        }
    }

    private LessonDTO getLessonFromFields(StudentDTO student) {
        String lessonTime = String.format("%02d:%02d:00", spinnerHour.getValue(), spinnerMinute.getValue());
        return new LessonDTO(
                txtLessonId.getText(),
                student.getStudentId(),
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
        listStudents.getSelectionModel().clearSelection();
        cmbCourse.getSelectionModel().clearSelection();
        cmbInstructor.getSelectionModel().clearSelection();
        spinnerHour.getValueFactory().setValue(9);
        spinnerMinute.getValueFactory().setValue(0);
        txtDuration.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ObservableList<StudentDTO> selectedStudents = listStudents.getSelectionModel().getSelectedItems();
        if (selectedStudents.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select a student to delete lesson").show();
            return;
        }
        for (StudentDTO student : selectedStudents) {
            LessonDTO dto = getLessonFromFields(student);
            lessonBO.deleteLesson(dto);
        }
        clearData();
        loadAllLessons();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        ObservableList<StudentDTO> selectedStudents = listStudents.getSelectionModel().getSelectedItems();
        if (selectedStudents.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select at least one student").show();
            return;
        }

        try {
            for (StudentDTO student : selectedStudents) {
                LessonDTO dto = getLessonFromFields(student);
                lessonBO.saveLesson(dto);
            }
            new Alert(Alert.AlertType.INFORMATION, "Lesson(s) saved successfully!").show();
            clearData();
            loadAllLessons();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        ObservableList<StudentDTO> selectedStudents = listStudents.getSelectionModel().getSelectedItems();
        if (selectedStudents.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select at least one student").show();
            return;
        }

        try {
            for (StudentDTO student : selectedStudents) {
                LessonDTO dto = getLessonFromFields(student);
                lessonBO.updateLesson(dto);
            }
            new Alert(Alert.AlertType.INFORMATION, "Lesson(s) updated successfully!").show();
            clearData();
            loadAllLessons();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void tblLessonOnClickAction(MouseEvent event) {
        LessonTm selected = tblLesson.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtLessonId.setText(selected.getLessonId());
            dateLesson.setValue(selected.getLessonDate());
            cmbCourse.setValue(selected.getCourseId());
            cmbInstructor.setValue(selected.getInstructorId());

            String[] timeParts = selected.getLessonTime().split(":");
            spinnerHour.getValueFactory().setValue(Integer.parseInt(timeParts[0]));
            spinnerMinute.getValueFactory().setValue(Integer.parseInt(timeParts[1]));

            txtDuration.setText(String.valueOf(selected.getDuration()));

            // select the student in ListView
            listStudents.getItems().stream()
                    .filter(s -> s.getStudentId().equals(selected.getStudentId()))
                    .findFirst()
                    .ifPresent(s -> listStudents.getSelectionModel().select(s));
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
