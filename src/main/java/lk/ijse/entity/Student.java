package lk.ijse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id", nullable = false, length = 10)
    private String studentId;

    @Column(nullable = false)
    private String name;

    private String address;
    private Long tel;
    private String email;

    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(nullable = false)
    private int someInt = 0;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();




    public Student(String studentId, String name, String address, Long tel, String email, Date registrationDate) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("studentId cannot be null or empty. Generate it before saving.");
        }
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.registrationDate = registrationDate;
    }


    public Student(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("studentId cannot be null or empty. Generate it before saving.");
        }
        this.studentId = studentId;
    }
}
