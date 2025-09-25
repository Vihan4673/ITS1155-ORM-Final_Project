package lk.ijse.tdm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewAllTm {
    private String studentId;
    private String studentName;
    private LocalDate registerDate;   // Changed from java.sql.Date to LocalDate
    private LocalDate installment;    // Changed from java.sql.Date to LocalDate
    private int balance;
    private Button payment;           // Keep button for actions
}
