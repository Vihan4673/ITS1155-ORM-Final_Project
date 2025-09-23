package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dto.PaymentDTO;

public class PaymentFormController {

    @FXML private TextField txtPaymentId;
    @FXML private ComboBox<String> cmbStudent;
    @FXML private ComboBox<String> cmbProgram;
    @FXML private TextField txtAmount;
    @FXML private DatePicker dpPaymentDate;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Button btnMakePayment;
    @FXML private Button btnCancel;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);

    @FXML
    public void initialize() {
        generatePaymentId();
        loadStatusOptions();
        loadStudents();
        loadPrograms();
    }

    private void generatePaymentId() {
        txtPaymentId.setText(paymentBO.generatePaymentId());
    }

    private void loadStatusOptions() {
        ObservableList<String> statusList = FXCollections.observableArrayList("COMPLETED", "PENDING", "FAILED");
        cmbStatus.setItems(statusList);
    }

    private void loadStudents() {
        ObservableList<String> studentList = FXCollections.observableArrayList();
        paymentBO.getAllStudents().forEach(student -> studentList.add(student.getStudentId()));
        cmbStudent.setItems(studentList);
    }

    private void loadPrograms() {
        ObservableList<String> programList = FXCollections.observableArrayList();
        paymentBO.getAllPrograms().forEach(program -> programList.add(program.getProgramId()));
        cmbProgram.setItems(programList);
    }

    // 🔑 Allow StudentFormController to set StudentID directly
    public void setStudentId(String studentId) {
        cmbStudent.setValue(studentId);
        cmbStudent.setDisable(true); // prevent user from changing
    }

    @FXML
    private void btnMakePaymentOnAction() {
        try {
            // Validate fields
            if (cmbStudent.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a Student!").show();
                return;
            }
            if (cmbProgram.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a Program!").show();
                return;
            }
            if (dpPaymentDate.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a Payment Date!").show();
                return;
            }
            if (cmbStatus.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a Status!").show();
                return;
            }

            // Validate Amount
            String amountText = txtAmount.getText();
            if (amountText == null || amountText.trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter the Amount!").show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    new Alert(Alert.AlertType.WARNING, "Amount must be greater than 0!").show();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Amount must be a valid number!").show();
                return;
            }

            // Create DTO
            PaymentDTO dto = new PaymentDTO(
                    txtPaymentId.getText(),
                    cmbStudent.getValue(),
                    cmbProgram.getValue(),
                    amount,
                    dpPaymentDate.getValue().toString(),
                    cmbStatus.getValue()
            );

            // Save
            if (paymentBO.savePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Saved Successfully!").show();
                closeForm(); // <-- close after success
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Save Payment!").show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCancelOnAction() {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
