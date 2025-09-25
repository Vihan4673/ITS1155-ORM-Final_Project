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
    private List<String> programIds;
    private double amount;
    private LocalDate date;
    private String status;


    public String getProgramIdsAsString() {
        if (programIds == null || programIds.isEmpty()) return "";
        return String.join(", ", programIds);
    }


    public String getDateAsString() {
        if (date == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }
}
