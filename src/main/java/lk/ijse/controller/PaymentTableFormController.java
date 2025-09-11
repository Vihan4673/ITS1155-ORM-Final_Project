package lk.ijse.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.tdm.PaymentTM;

import java.util.List;

public class PaymentTableFormController {

    public TextField txtStuID;
    public TextField txCouID;

    @FXML
    private TableView<PaymentTM> tblPayments;

    @FXML
    private TableColumn<PaymentTM, String> colPaymentId;

    @FXML
    private TableColumn<PaymentTM, String> colStudent;

    @FXML
    private TableColumn<PaymentTM, String> colProgram;

    @FXML
    private TableColumn<PaymentTM, Double> colAmount;

    @FXML
    private TableColumn<PaymentTM, String> colDate;

    @FXML
    private TableColumn<PaymentTM, String> colStatus;

    private final ObservableList<PaymentTM> paymentList = FXCollections.observableArrayList();
    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);

    @FXML
    public void initialize() {
        // Setup columns
        colPaymentId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentId()));
        colStudent.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStudentId())); // now using ID
        colProgram.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProgramId())); // now using ID
        colAmount.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getAmount()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        // Editable Status column
        colStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        colStatus.setOnEditCommit(event -> {
            PaymentTM payment = event.getRowValue();
            payment.setStatus(event.getNewValue());
            try {
                assert paymentBO != null;
                paymentBO.updateStatus(payment.getPaymentId(), event.getNewValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Load DB data initially
        loadPayments();

        // 🔍 Real-time search listeners
        txtStuID.textProperty().addListener((obs, oldText, newText) -> searchPayments());
        txCouID.textProperty().addListener((obs, oldText, newText) -> searchPayments());
    }

    private void loadPayments() {
        paymentList.clear();
        try {
            List<PaymentDTO> dtos = paymentBO.getAllPayments();
            for (PaymentDTO dto : dtos) {
                paymentList.add(new PaymentTM(
                        dto.getPaymentId(),
                        dto.getStudentId(),   // ✅ Student ID
                        dto.getProgramId(),   // ✅ Program ID
                        dto.getAmount(),
                        dto.getPaymentDate(),
                        dto.getStatus()
                ));
            }
            tblPayments.setItems(paymentList);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load payments!").show();
        }
    }

    // 🔍 Real-time search logic (by StudentID & ProgramID)
    private void searchPayments() {
        String stuId = txtStuID.getText().trim();
        String couId = txCouID.getText().trim();

        if (stuId.isEmpty() && couId.isEmpty()) {
            // Both fields empty → show all
            tblPayments.setItems(paymentList);
            return;
        }

        ObservableList<PaymentTM> filteredList = FXCollections.observableArrayList();

        for (PaymentTM payment : paymentList) {
            boolean match = true;

            if (!stuId.isEmpty() && !payment.getStudentId().toLowerCase().contains(stuId.toLowerCase())) {
                match = false;
            }
            if (!couId.isEmpty() && !payment.getProgramId().toLowerCase().contains(couId.toLowerCase())) {
                match = false;
            }

            if (match) {
                filteredList.add(payment);
            }
        }

        tblPayments.setItems(filteredList);
    }
}
