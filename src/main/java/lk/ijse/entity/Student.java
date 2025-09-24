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

    // Constructor without relations
    public Student(String studentId, String name, String address, Long tel, String email, Date registrationDate) {
        this.studentId = studentId; // ⚡ manual assign, e.g., "S1001"
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.registrationDate = registrationDate;
    }

    // Constructor with only ID
    public Student(String studentId) {
        this.studentId = studentId;
    }
}
