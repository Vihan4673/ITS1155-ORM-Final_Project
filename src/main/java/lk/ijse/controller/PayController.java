package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.tdm.PaymentTM;

public class PayController {

    @FXML private ComboBox<String> cmbPaymentId;
    @FXML private TextField txtStudentId;
    @FXML private TextField txtCourseId;
    @FXML private TextField txtTotalFee;
    @FXML private TextField txtAmount;

    private PaymentTM selectedPayment;
    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);

    /** Initialize controller with selected payment */
    public void setPayment(PaymentTM payment) {
        this.selectedPayment = payment;
        loadPaymentDetails();
    }

    /** Load payment details into the form */
    private void loadPaymentDetails() {
        if (selectedPayment != null) {
            cmbPaymentId.getItems().clear();
            cmbPaymentId.getItems().add(selectedPayment.getPaymentId());
            cmbPaymentId.getSelectionModel().selectFirst();

            txtStudentId.setText(selectedPayment.getStudentId());
            txtCourseId.setText(String.join(", ", selectedPayment.getProgramIds()));
            txtTotalFee.setText(String.valueOf(selectedPayment.getAmount()));
        }
    }

    /** Pay button clicked */
    @FXML
    private void btnPayOnAction() {
        try {
            if (txtAmount.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Enter the amount to pay!").show();
                return;
            }

            double amountPaid = Double.parseDouble(txtAmount.getText());
            double total = Double.parseDouble(txtTotalFee.getText());

            String newStatus = amountPaid >= total ? "COMPLETED" : "PENDING";

            // Update payment DTO
            PaymentDTO dto = new PaymentDTO(
                    selectedPayment.getPaymentId(),
                    selectedPayment.getStudentId(),
                    selectedPayment.getProgramIds(),
                    amountPaid,
                    total,
                    selectedPayment.getDate(),
                    newStatus
            );

            if (paymentBO.updatePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment updated successfully!").show();
                closeForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update payment!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid amount!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    /** Clear button clicked */
    @FXML
    private void btnClearOnAction() {
        txtAmount.clear();
    }

    /** Close the form */
    private void closeForm() {
        Stage stage = (Stage) txtAmount.getScene().getWindow();
        stage.close();
    }

    public void cmbPaymentIdOnAction(ActionEvent actionEvent) {
    }
}
