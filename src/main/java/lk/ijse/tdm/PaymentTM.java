package lk.ijse.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTM {
    private String paymentId;
    private String studentId;
    private String programId;
    private double amount;
    private String date;
    private String status;
}
