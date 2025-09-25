package lk.ijse.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.tdm.PaymentTM;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class PaymentTableFormController {

    @FXML
    private TextField txtStuID;
    @FXML
    private TextField txCouID;

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
    @FXML
    private TableColumn<PaymentTM, HBox> colAction;

    private final ObservableList<PaymentTM> paymentList = FXCollections.observableArrayList();
    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    public void initialize() {
        // Table column bindings
        colPaymentId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPaymentId()));
        colStudent.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStudentId()));
        colProgram.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProgramIdsAsString()));
        colAmount.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getAmount()));
        colDate.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getDate() != null ? cell.getValue().getDate().format(dateFormatter) : ""
        ));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        // Editable status
        colStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        colStatus.setOnEditCommit(event -> {
            PaymentTM payment = event.getRowValue();
            payment.setStatus(event.getNewValue());
            try {
                paymentBO.updateStatus(payment.getPaymentId(), event.getNewValue());
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to update status!").show();
            }
        });

        // Action column: Pay button
        colAction.setCellValueFactory(cell -> {
            Button payBtn = new Button("Pay");
            payBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
            payBtn.setOnAction(event -> openPaymentForm(cell.getValue()));

            HBox hbox = new HBox(payBtn);
            hbox.setSpacing(5);
            return new SimpleObjectProperty<>(hbox);
        });

        loadPayments();

        // Search listeners (null-safe)
        if (txtStuID != null) txtStuID.textProperty().addListener((obs, oldText, newText) -> searchPayments());
        if (txCouID != null) txCouID.textProperty().addListener((obs, oldText, newText) -> searchPayments());
    }

    private void loadPayments() {
        paymentList.clear();
        try {
            List<PaymentDTO> dtos = paymentBO.getAllPayments();
            if (dtos == null) dtos = Collections.emptyList();

            for (PaymentDTO dto : dtos) {
                List<String> programIds = dto.getProgramIds() != null ? dto.getProgramIds() : Collections.emptyList();
                PaymentTM tm = new PaymentTM(
                        dto.getPaymentId(),
                        dto.getStudentId(),
                        programIds,
                        dto.getAdvanceAmount(),
                        dto.getPaymentDate(),
                        dto.getStatus()
                );
                paymentList.add(tm);
            }

            tblPayments.setItems(paymentList);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load payments!").show();
        }
    }


    private void searchPayments() {
        String stuId = txtStuID != null ? txtStuID.getText().trim().toLowerCase() : "";
        String couId = txCouID != null ? txCouID.getText().trim().toLowerCase() : "";

        if (stuId.isEmpty() && couId.isEmpty()) {
            tblPayments.setItems(paymentList);
            return;
        }

        ObservableList<PaymentTM> filteredList = FXCollections.observableArrayList();
        for (PaymentTM payment : paymentList) {
            boolean match = true;
            if (!stuId.isEmpty() && !payment.getStudentId().toLowerCase().contains(stuId)) match = false;
            if (!couId.isEmpty() && (payment.getProgramIds() == null || payment.getProgramIds().stream().noneMatch(pid -> pid.toLowerCase().contains(couId)))) match = false;
            if (match) filteredList.add(payment);
        }
        tblPayments.setItems(filteredList);
    }

    private void openPaymentForm(PaymentTM payment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/pay.fxml"));
            Parent root = loader.load();

            // Correct controller type
            PayController controller = loader.getController();

            // Prepare data to set
            controller.setPayment(payment);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Make Payment");
            stage.show();

            // Refresh table after Pay form is closed
            stage.setOnHidden(e -> loadPayments());

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to open payment form!").show();
        }
    }

    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        // Not needed since we use listeners
    }
}
