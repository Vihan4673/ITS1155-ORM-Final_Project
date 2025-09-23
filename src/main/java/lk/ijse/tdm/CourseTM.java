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
    private String duration;   // formatted string for TableView, e.g., "1 year 3 months"
    private double fee;

    // Optional: store raw duration for internal use
    private int durationValue; // numeric value
    private String durationUnit; // "Months" or "Weeks"

    // Constructor for display only
    public CourseTM(String id, String programName, String duration, double fee) {
        this.id = id;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }
}
