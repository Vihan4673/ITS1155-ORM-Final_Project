package lk.ijse.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTM {

    private String paymentId;
    private String studentId;
    private List<String> programIds;  // multiple programs
    private double amount;
    private LocalDate date;           // Use LocalDate instead of String
    private String status;

    // Helper to display programs as comma-separated string
    public String getProgramIdsAsString() {
        if (programIds == null || programIds.isEmpty()) return "";
        return String.join(", ", programIds);
    }

    // Helper to display date as string in table view
    public String getDateAsString() {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}
