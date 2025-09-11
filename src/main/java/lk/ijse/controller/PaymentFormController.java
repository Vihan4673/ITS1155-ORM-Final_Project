package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        // Generate Payment ID
        txtPaymentId.setText(paymentBO.generatePaymentId());

        // Load status options
        ObservableList<String> statusList = FXCollections.observableArrayList("COMPLETED", "PENDING", "FAILED");
        cmbStatus.setItems(statusList);

        // Load Student IDs
        ObservableList<String> studentList = FXCollections.observableArrayList();
        paymentBO.getAllStudents().forEach(student -> studentList.add(student.getStudentId()));
        cmbStudent.setItems(studentList);

        // Load Program IDs
        ObservableList<String> programList = FXCollections.observableArrayList();
        paymentBO.getAllPrograms().forEach(program -> programList.add(program.getProgramId()));
        cmbProgram.setItems(programList);
    }

    @FXML
    private void btnMakePaymentOnAction() {
        PaymentDTO dto = new PaymentDTO(
                txtPaymentId.getText(),
                cmbStudent.getValue(),
                cmbProgram.getValue(),
                Double.parseDouble(txtAmount.getText()),
                dpPaymentDate.getValue() != null ? dpPaymentDate.getValue().toString() : null, // <-- මෙය
                cmbStatus.getValue()
        );


        if (paymentBO.savePayment(dto)) {
            new Alert(Alert.AlertType.INFORMATION, "Payment Saved Successfully!").show();
            clearForm();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to Save Payment!").show();
        }
    }

    @FXML
    private void btnCancelOnAction() {
        clearForm();
    }

    private void clearForm() {
        txtPaymentId.setText(paymentBO.generatePaymentId());
        cmbStudent.getSelectionModel().clearSelection();
        cmbProgram.getSelectionModel().clearSelection();
        txtAmount.clear();
        dpPaymentDate.setValue(null);
        cmbStatus.getSelectionModel().clearSelection();
    }
}
