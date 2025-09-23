package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "culinary_programs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @Column(length = 10)
    private String programId;

    @Column(nullable = false)
    private String programName;

    @Column(nullable = false)
    private int duration; // stored in months

    @Column(nullable = false)
    private double fee;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    // Custom constructor for convenience (without relationships)
    public Course(String programId, String programName, int duration, double fee) {
        this.programId = programId;
        this.programName = programName;
        this.duration = duration;
        this.fee = fee;
    }

    // Constructor with only ID, defaults others
    public Course(String programId) {
        this.programId = programId;
        this.programName = "";
        this.duration = 0;
        this.fee = 0.0;
    }
}
