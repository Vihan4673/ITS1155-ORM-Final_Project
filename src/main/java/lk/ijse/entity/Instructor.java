package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    private String instructorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();

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
