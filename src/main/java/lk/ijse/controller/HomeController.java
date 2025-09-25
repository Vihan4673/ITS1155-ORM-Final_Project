package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import lk.ijse.bo.BOFactory;
import lk.ijse.bo.custom.DashboardBO;
import lk.ijse.config.FactoryConfiguration;
import lk.ijse.dto.StudentDTO;
import lk.ijse.tdm.StudyAllStudentTm;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class HomeController {

    @FXML
    private BarChart<String, Number> BarChartStu;

    @FXML
    private Label lblIncom;

    @FXML
    private Label lblTotalInstructor;

    @FXML
    private Label lblTotalPrograms;

    @FXML
    private Label lblTotalStudent;

    // ❌ Not used in FXML - removed lblStudentCount
    @FXML
    private TableView<StudyAllStudentTm> tblStudyAll;

    @FXML
    private TableColumn<StudyAllStudentTm, String> colId;

    @FXML
    private TableColumn<StudyAllStudentTm, String> colName;

    @FXML
    private TableColumn<StudyAllStudentTm, String> colDate;

    @FXML
    private AnchorPane dashboardForm;

    private final DashboardBO dashboardBO = (DashboardBO) BOFactory.getBO(BOFactory.BOType.DASHBOARD);

    @FXML
    public void initialize() {
        setCellValueFactory();
        setTotals();
        loadTableData();
        loadStudentChart();
        loadIncome();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
    }

    private void setTotals() {
        try {
            lblTotalPrograms.setText(String.valueOf(dashboardBO.getCulinaryProgramCount()));
            lblTotalStudent.setText(String.valueOf(dashboardBO.getStudentCount()));
            lblTotalInstructor.setText(String.valueOf(dashboardBO.getInstructorCount()));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load totals!").show();
        }
    }

    private void loadTableData() {
        try {
            tblStudyAll.getItems().clear();
            ObservableList<StudyAllStudentTm> studentTms = FXCollections.observableArrayList();
            List<StudentDTO> allProgramStudents = dashboardBO.getAllProgramStudents();

            for (StudentDTO studentDTO : allProgramStudents) {
                studentTms.add(new StudyAllStudentTm(
                        studentDTO.getStudentId(),
                        studentDTO.getName(),
                        studentDTO.getRegistrationDate()
                ));
            }
            tblStudyAll.setItems(studentTms);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load recent student data!").show();
        }
    }
    private void loadStudentChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Student Registrations");

        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            // JPQL query to count students per month
            List<Object[]> results = session.createQuery(
                            "SELECT FUNCTION('MONTH', s.registrationDate), COUNT(s) " +
                                    "FROM Student s " +
                                    "GROUP BY FUNCTION('MONTH', s.registrationDate) " +
                                    "ORDER BY FUNCTION('MONTH', s.registrationDate)", Object[].class)
                    .list();

            for (Object[] row : results) {
                int month = ((Number) row[0]).intValue();
                long count = ((Number) row[1]).longValue();

                String monthName = LocalDate.of(LocalDate.now().getYear(), month, 1)
                        .getMonth()
                        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

                series.getData().add(new XYChart.Data<>(monthName, count));
            }

            BarChartStu.getData().clear();
            BarChartStu.getData().add(series);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load student chart.").show();
        } finally {
            if (session != null) session.close();
        }
    }


    private void loadIncome() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();

            // DB column name = payment_date
            Double totalIncome = ((Number) session.createNativeQuery(
                            "SELECT SUM(amount) FROM payments " +
                                    "WHERE MONTH(payment_date) = :month AND YEAR(payment_date) = :year")
                    .setParameter("month", currentMonth)
                    .setParameter("year", currentYear)
                    .uniqueResult()).doubleValue();

            if (totalIncome == null) totalIncome = 0.0;

            lblIncom.setText("LKR " + String.format("%.2f", totalIncome));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load current month income").show();
        } finally {
            if (session != null) session.close();
        }
    }

}
