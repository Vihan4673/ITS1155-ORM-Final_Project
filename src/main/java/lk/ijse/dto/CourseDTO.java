package lk.ijse.dto;

import lk.ijse.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private String programId;
    private String programName;
    private int duration; // stored in months
    private double fee;
    private String durationUnit; // මේක add කරන්න
    private List<Payment> payments = new ArrayList<>();

    // Convenience constructor without payments
    public CourseDTO(String programId, String programName, int duration, double fee, String durationUnit) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
        this.durationUnit = durationUnit;
        this.payments = new ArrayList<>();
    }
}
