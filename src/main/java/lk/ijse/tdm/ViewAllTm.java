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
    private LocalDate registerDate;
    private LocalDate installment;
    private int balance;
    private Button payment;
}
