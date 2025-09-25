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
    private String paymentId;
    private String studentId;
    private List<String> programIds;
    private double advanceAmount;
    private double totalFee;
    private LocalDate paymentDate;
    private String status;

}
