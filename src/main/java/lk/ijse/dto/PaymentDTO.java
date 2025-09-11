package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String paymentId;
    private String studentId;
    private String programId;
    private double amount;
    private String paymentDate;
    private String status;
}
