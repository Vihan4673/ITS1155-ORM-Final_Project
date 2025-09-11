package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    private String instructorId;  // String ID, manually assign

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();


    public Instructor(String instructorId) {
        this.instructorId = instructorId;
        this.name = "";
        this.specialization = "";
        this.courses = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
}
