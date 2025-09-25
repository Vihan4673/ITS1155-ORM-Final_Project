package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String paymentId;        // Unique Payment ID
    private String studentId;        // Associated Student ID
    private List<String> programIds; // List of enrolled Course IDs
    private double advanceAmount;    // Amount paid (partial or full)
    private double totalFee;         // Total fee for selected courses
    private LocalDate paymentDate;   // Payment date (yyyy-MM-dd)
    private String status;           // PAID, PENDING, PARTIAL

}
