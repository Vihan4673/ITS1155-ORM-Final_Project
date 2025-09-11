package lk.ijse.dto;

import lk.ijse.entity.Payment;

import java.util.List;

public class courseDTO {
    private String programId;
    private String programName;
    private int duration;
    private double fee;
    private List<Payment> payments;


    public courseDTO() {}


    public courseDTO(String programId, String programName, int duration, double fee, List<Payment> payments) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
        this.payments = payments;
    }


    public courseDTO(String programId, String programName, int duration, double fee) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }
    public String getProgramId() {
        return programId;
    }

    public String getProgramName() {
        return programName;
    }

    public int getDuration() {
        return duration;
    }

    public double getFee() {
        return fee;
    }

    public List<Payment> getPayments() {
        return payments;
    }


}
