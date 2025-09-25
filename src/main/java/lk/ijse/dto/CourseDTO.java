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
    private int duration;
    private double fee;
    private String durationUnit;
    private List<Payment> payments = new ArrayList<>();


    public CourseDTO(String programId, String programName, int duration, double fee, String durationUnit) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
        this.durationUnit = durationUnit;
        this.payments = new ArrayList<>();
    }
}
