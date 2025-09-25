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
import lk.ijse.dto.CourseDTO;
import lk.ijse.tdm.CourseTM;
import lk.ijse.util.Regex;

import java.util.List;

public class CourseFormController {

    @FXML private TextField txtSearch;
    @FXML private TableColumn<CourseTM, String> colId;
    @FXML private TableColumn<CourseTM, String> colProgramName;
    @FXML private TableColumn<CourseTM, String> colDuration;
    @FXML private TableColumn<CourseTM, Double> colFee;
    @FXML private TableView<CourseTM> tblProgram;
    @FXML private TextField txtDuration;
    @FXML private TextField txtFee;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private ComboBox<String> cmbDurationUnit;

    private final CourseBO courseBO = (CourseBO) BOFactory.getBO(BOFactory.BOType.PROGRAM);

    public void initialize() {
        setCellValueFactory();
        txtId.setText(courseBO.generateCourseId());
        loadAllCourses();

        cmbDurationUnit.setItems(FXCollections.observableArrayList("Months", "Weeks"));
        cmbDurationUnit.getSelectionModel().selectFirst(); // default Months
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    private void loadAllCourses() {
        List<CourseDTO> courses = courseBO.getAllCourses();
        ObservableList<CourseTM> tmList = FXCollections.observableArrayList();

        for (CourseDTO c : courses) {
            String duration = convertDurationToString(c.getDuration());
            tmList.add(new CourseTM(c.getProgramId(), c.getProgramName(), duration, c.getFee()));
        }

        tblProgram.setItems(tmList);
    }

    private String convertDurationToString(int duration) {
        if(duration >= 12){
            int years = duration / 12;
            int months = duration % 12;
            return months == 0 ? years + " years" : years + " years " + months + " months";
        }
        return duration + " months";
    }

    private int convertDurationToInt(String duration) {
        int years = 0, months = 0;
        String[] parts = duration.split(" ");
        for(int i=0; i<parts.length; i++){
            if(parts[i].equalsIgnoreCase("year") || parts[i].equalsIgnoreCase("years")) years = Integer.parseInt(parts[i-1]);
            else if(parts[i].equalsIgnoreCase("month") || parts[i].equalsIgnoreCase("months")) months = Integer.parseInt(parts[i-1]);
        }
        return years * 12 + months;
    }

    private int getDurationInMonths() {
        int value = Integer.parseInt(txtDuration.getText());
        String unit = cmbDurationUnit.getValue();
        if(unit.equalsIgnoreCase("Weeks")) {
            return (int) Math.ceil(value / 4.0);
        }
        return value;
    }

    private CourseDTO getDTOFromFields() {
        return new CourseDTO(
                txtId.getText(),
                txtName.getText(),
                getDurationInMonths(),
                Double.parseDouble(txtFee.getText()),
                cmbDurationUnit.getValue()
        );
    }


    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearData();
        txtId.setText(courseBO.generateCourseId());
    }

    private void clearData() {
        txtName.clear();
        txtDuration.clear();
        txtFee.clear();
        cmbDurationUnit.getSelectionModel().selectFirst();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if(isValid()) {
            try {
                courseBO.saveCourse(getDTOFromFields());
                loadAllCourses();
                clearData();
                txtId.setText(courseBO.generateCourseId());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Save Failed: "+e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if(isValid()) {
            try {
                courseBO.updateCourse(getDTOFromFields());
                loadAllCourses();
                clearData();
                txtId.setText(courseBO.generateCourseId());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Update Failed: "+e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if(isValid()) {
            try {
                courseBO.deleteCourse(getDTOFromFields());
                loadAllCourses();
                clearData();
                txtId.setText(courseBO.generateCourseId());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Delete Failed: "+e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields!").show();
        }
    }

    @FXML
    void tblProgramOnClickAction(MouseEvent event) {
        CourseTM selected = tblProgram.getSelectionModel().getSelectedItem();
        if(selected != null){
            txtId.setText(selected.getId());
            txtName.setText(selected.getProgramName());
            txtDuration.setText(String.valueOf(convertDurationToInt(selected.getDuration())));
            cmbDurationUnit.getSelectionModel().select("Months"); // default
            txtFee.setText(String.valueOf(selected.getFee()));
        }
    }

    public boolean isValid() {
        return Regex.setTextColor(lk.ijse.util.TextField.NAME, txtName) &&
                Regex.setTextColor(lk.ijse.util.TextField.MONTH, txtDuration) &&
                Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtFee);
    }

    @FXML void txtNameKeyAction(KeyEvent event) { Regex.setTextColor(lk.ijse.util.TextField.NAME, txtName);}
    @FXML void txtDurationKeyAction(KeyEvent event) { Regex.setTextColor(lk.ijse.util.TextField.MONTH, txtDuration);}
    @FXML void txtFeeKeyAction(KeyEvent event) { Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtFee);}

    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase();
        List<CourseDTO> allCourses = courseBO.getAllCourses();
        ObservableList<CourseTM> filteredList = FXCollections.observableArrayList();

        for(CourseDTO c : allCourses){
            if(c.getProgramId().toLowerCase().contains(searchText) ||
                    c.getProgramName().toLowerCase().contains(searchText)) {
                String duration = convertDurationToString(c.getDuration());
                filteredList.add(new CourseTM(c.getProgramId(), c.getProgramName(), duration, c.getFee()));
            }
        }

        tblProgram.setItems(filteredList);
    }
}
