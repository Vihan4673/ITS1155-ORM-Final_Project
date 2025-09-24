package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.PaymentBO;
import lk.ijse.bo.custom.StudentBO;
import lk.ijse.dto.CourseDTO;
import lk.ijse.dto.PaymentDTO;
import lk.ijse.dto.StudentDTO;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentFormController {

    @FXML private TextField txtPaymentId;
    @FXML private TextField txtStudent;
    @FXML private ListView<String> lstCourses;
    @FXML private TextField txtTotalFee;
    @FXML private TextField txtAdvance;
    @FXML private TextField txtRemaining;
    @FXML private DatePicker dpPaymentDate;
    @FXML private Button btnMakePayment;
    @FXML private Button btnCancel;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);
    private final StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);

    private double totalFee = 0.0;

    @FXML
    public void initialize() {
        generatePaymentId();
        txtAdvance.textProperty().addListener((obs, oldVal, newVal) -> updateRemaining());
    }

    private void generatePaymentId() {
        try {
            txtPaymentId.setText(paymentBO.generatePaymentId());
        } catch (Exception e) {
            txtPaymentId.setText("P1001"); // fallback ID
        }
    }

    /**
     * Load student and their courses
     */
    public void loadStudentCourses(String studentId) {
        try {
            StudentDTO student = studentBO.getStudent(studentId);
            if (student != null) {
                txtStudent.setText(student.getStudentId() + " - " + student.getName());

                // Load course names
                List<String> courseNames = student.getEnrolledCourseIds().stream().collect(Collectors.toList());
                lstCourses.setItems(FXCollections.observableArrayList(courseNames));

                // Calculate total fee
                totalFee = student.getEnrolledCourseIds().size() * 1000; // Example: each course fee = 1000
                txtTotalFee.setText(String.valueOf(totalFee));

                // Clear previous amounts
                txtAdvance.clear();
                txtRemaining.setText(String.valueOf(totalFee));
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load student data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void updateRemaining() {
        try {
            String advanceText = txtAdvance.getText();
            double advance = advanceText.isEmpty() ? 0.0 : Double.parseDouble(advanceText);
            double remaining = totalFee - advance;
            txtRemaining.setText(String.valueOf(remaining));
        } catch (NumberFormatException e) {
            txtRemaining.setText(String.valueOf(totalFee));
        }
    }

    @FXML
    private void btnMakePaymentOnAction() {
        try {
            // Validations
            if (txtStudent.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a student!").show();
                return;
            }
            if (lstCourses.getItems().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "No courses selected!").show();
                return;
            }
            if (dpPaymentDate.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Please select a payment date!").show();
                return;
            }
            if (txtAdvance.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter the advance amount!").show();
                return;
            }

            double advance = Double.parseDouble(txtAdvance.getText());
            if (advance <= 0) {
                new Alert(Alert.AlertType.WARNING, "Advance must be greater than 0!").show();
                return;
            }

            // Create DTO
            PaymentDTO dto = new PaymentDTO(
                    txtPaymentId.getText(),
                    txtStudent.getText().split(" - ")[0], // extract studentId
                    String.join(",", lstCourses.getItems()),
                    advance,
                    dpPaymentDate.getValue().toString(),
                    txtRemaining.getText()
            );

            // Save
            if (paymentBO.savePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Saved Successfully!").show();
                closeForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save payment!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid advance amount!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
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

    public void setStudentAndCourses(String studentId, List<CourseDTO> list) {
    }
}
