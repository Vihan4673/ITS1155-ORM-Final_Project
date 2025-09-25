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

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();


    public Instructor(String instructorId) {
        this.instructorId = instructorId;
        this.name = "";
        this.specialization = "";
        this.email = "";
        this.phone = "";
        this.courses = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }

    public Instructor(String instructorId, String name, String specialization, String email, String phone) {
        this.instructorId = instructorId;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
        this.courses = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
}

