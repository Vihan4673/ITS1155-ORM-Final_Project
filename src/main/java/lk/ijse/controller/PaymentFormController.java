package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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
    @FXML private ComboBox<StudentDTO> cmbStudent;
    @FXML private ListView<String> lstCourses;
    @FXML private TextField txtTotalFee;
    @FXML private TextField txtAmount;
    @FXML private TextField txtRemaining;
    @FXML private Label lblStatus;
    @FXML private DatePicker dpPaymentDate;
    @FXML private Button btnMakePayment;
    @FXML private Button btnCancel;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBO(BOFactory.BOType.PAYMENT);
    private final StudentBO studentBO = (StudentBO) BOFactory.getBO(BOFactory.BOType.STUDENT);

    private List<CourseDTO> selectedCourses;
    private double totalFee = 0.0;

    @FXML
    public void initialize() {
        generatePaymentId();
        lstCourses.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        txtAmount.textProperty().addListener((obs, oldVal, newVal) -> updateRemaining());
    }

    private void generatePaymentId() {
        try {
            txtPaymentId.setText(paymentBO.generatePaymentId());
        } catch (Exception e) {
            txtPaymentId.setText("PAY001"); // fallback
            e.printStackTrace();
        }
    }

    public void loadStudentCourses(String studentId, List<CourseDTO> courses) {
        try {
            StudentDTO student = studentBO.getStudent(studentId);
            if (student != null) {
                cmbStudent.setValue(student);
                selectedCourses = courses;

                ObservableList<String> courseNames = FXCollections.observableArrayList();
                totalFee = 0.0;
                for (CourseDTO course : selectedCourses) {
                    courseNames.add(course.getProgramName() + " - Rs." + course.getFee());
                    totalFee += course.getFee();
                }
                lstCourses.setItems(courseNames);
                txtTotalFee.setText(String.valueOf(totalFee));
                txtAmount.clear();
                txtRemaining.setText(String.valueOf(totalFee));
                lblStatus.setText("PENDING");
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load student data: " + e.getMessage()).show();
        }
    }

    private void updateRemaining() {
        try {
            double advance = txtAmount.getText().isEmpty() ? 0.0 : Double.parseDouble(txtAmount.getText());
            double remaining = totalFee - advance;
            txtRemaining.setText(String.valueOf(remaining));
            lblStatus.setText(advance >= totalFee ? "COMPLETE" : "PENDING");
        } catch (NumberFormatException e) {
            txtRemaining.setText(String.valueOf(totalFee));
            lblStatus.setText("PENDING");
        }
    }

    @FXML
    private void btnMakePaymentOnAction() {
        try {
            if (cmbStudent.getValue() == null) {
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
            if (txtAmount.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter the amount paid!").show();
                return;
            }

            double advance = Double.parseDouble(txtAmount.getText());
            if (advance < 5000) {
                new Alert(Alert.AlertType.WARNING, "Minimum advance is Rs.5000!").show();
                return;
            }
            if (advance > totalFee) {
                new Alert(Alert.AlertType.WARNING, "Amount cannot exceed total fee!").show();
                return;
            }

            String studentId = cmbStudent.getValue().getStudentId();
            List<String> programIds = selectedCourses.stream()
                    .map(CourseDTO::getProgramId)
                    .collect(Collectors.toList());
            String status = advance >= totalFee ? "COMPLETE" : "PENDING";

            PaymentDTO dto = new PaymentDTO(
                    txtPaymentId.getText(),
                    studentId,
                    programIds,
                    advance,
                    totalFee,
                    dpPaymentDate.getValue(),
                    status
            );

            boolean saved = paymentBO.savePayment(dto);
            if (saved) {
                new Alert(Alert.AlertType.INFORMATION, "Payment saved successfully!").show();
                closeForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save payment!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid amount!").show();
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

    public void setStudentAndCourses(String studentId, List<CourseDTO> courses) {
        loadStudentCourses(studentId, courses);
    }

    public void cmbStudentOnAction(ActionEvent actionEvent) {}
    public void txtSearchKeyReleased(KeyEvent keyEvent) {}
}
