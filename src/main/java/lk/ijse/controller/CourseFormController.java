package lk.ijse.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.CourseBO;
import lk.ijse.dto.courseDTO;
import lk.ijse.tdm.courseTM;
import lk.ijse.util.Regex;


import java.util.ArrayList;
import java.util.List;

public class CourseFormController {

    public TextField txtSearch;
    @FXML
    private TableColumn<?, ?> colDuration;
    @FXML
    private TableColumn<?, ?> colFee;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colProgramName;
    @FXML
    private TableView<courseTM> tblProgram;
    @FXML
    private TextField txtDuration;
    @FXML
    private TextField txtFee;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;

    CourseBO programBO = (CourseBO) BOFactory.getBO(BOFactory.BOType.PROGRAM);

    public void initialize() {
        setCellValueFactory();
        txtId.setText(programBO.generateProgramId()); // auto-generate ID
        loadAllPrograms();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("programName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }

    private void loadAllPrograms() {
        List<courseDTO> programs = programBO.getAllCulinaryProgram();
        tblProgram.getItems().clear();
        ObservableList<courseTM> programTms = tblProgram.getItems();
        for (courseDTO p : programs) {
            String duration = convertDurationToString(p.getDuration());
            programTms.add(new courseTM(p.getProgramId(), p.getProgramName(), duration, p.getFee()));
        }
        tblProgram.setItems(programTms);
    }

    private String convertDurationToString(int duration) {
        if(duration > 11){
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
        return years*12 + months;
    }

    private courseDTO getObject(){
        return new courseDTO(txtId.getText(), txtName.getText(), Integer.parseInt(txtDuration.getText()), Double.parseDouble(txtFee.getText()), new ArrayList<>());
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearData();
        txtId.setText(programBO.generateProgramId());
    }

    private void clearData(){
        txtDuration.clear();
        txtFee.clear();
        txtName.clear();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (isValied()) {
            programBO.deleteCulinaryProgram(getObject());
            loadAllPrograms();
            clearData();
            txtId.setText(programBO.generateProgramId());
        } else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields !!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (isValied()){
            programBO.saveCulinaryProgram(getObject());
            loadAllPrograms();
            clearData();
            txtId.setText(programBO.generateProgramId());
        } else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields !!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (isValied()){
            programBO.updateCulinaryProgram(getObject());
            loadAllPrograms();
            clearData();
            txtId.setText(programBO.generateProgramId());
        } else {
            new Alert(Alert.AlertType.WARNING,"Please Enter All Fields !!").show();
        }
    }

    @FXML
    void tblProgramOnClickAction(MouseEvent event) {
        courseTM selected = tblProgram.getSelectionModel().getSelectedItem();
        if(selected != null){
            txtId.setText(selected.getId());
            txtName.setText(selected.getProgramName());
            txtDuration.setText(String.valueOf(convertDurationToInt(selected.getDuration())));
            txtFee.setText(String.valueOf(selected.getFee()));
        }
    }

    public boolean isValied() {
        // txtId skip (auto-generated)
        if (!Regex.setTextColor(lk.ijse.util.TextField.NAME, txtName)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.MONTH, txtDuration)) return false;
        if (!Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtFee)) return false;
        return true;
    }

    @FXML void txtIdKeyAction(KeyEvent event) {}
    @FXML void txtNameKeyAction(KeyEvent event) { Regex.setTextColor(lk.ijse.util.TextField.NAME, txtName);}
    @FXML void txtDurationKeyAction(KeyEvent event) { Regex.setTextColor(lk.ijse.util.TextField.MONTH, txtDuration);}
    @FXML void txtFeeKeyAction(KeyEvent event) { Regex.setTextColor(lk.ijse.util.TextField.PRICE, txtFee);}

    public void txtDurationOnAction(ActionEvent actionEvent) {
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
    }

    public void txtIdOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase(); // search text


        List<courseDTO> allPrograms = programBO.getAllCulinaryProgram();
        ObservableList<courseTM> filteredList = tblProgram.getItems();
        filteredList.clear();

        for (courseDTO program : allPrograms) {
            if (program.getProgramId().toLowerCase().contains(searchText) ||
                    program.getProgramName().toLowerCase().contains(searchText)) {

                String duration = convertDurationToString(program.getDuration());
                filteredList.add(new courseTM(program.getProgramId(),
                        program.getProgramName(),
                        duration,
                        program.getFee()));
            }
        }

        tblProgram.setItems(filteredList);
    }

}
