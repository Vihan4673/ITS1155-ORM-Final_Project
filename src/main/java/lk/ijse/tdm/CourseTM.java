package lk.ijse.tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseTM {
    private String id;
    private String programName;
    private String duration;
    private double fee;


    private int durationValue;
    private String durationUnit;


    public CourseTM(String id, String programName, String duration, double fee) {
        this.id = id;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }
}
