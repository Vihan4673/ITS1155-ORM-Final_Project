package lk.ijse.tdm;

import lk.ijse.dto.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentTm {

    private String studentId;
    private String name;
    private String address;
    private long tel;
    private String email;
    private Date registrationDate;
    private List<CourseDTO> courses = new ArrayList<>();  // Enrolled courses

    // Constructor without courses
    public StudentTm(String studentId, String name, String address, long tel, String email, Date registrationDate) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.registrationDate = registrationDate;
        this.courses = new ArrayList<>();
    }

    // Returns course names as comma-separated string for TableView
    public String getCoursesString() {
        if (courses == null || courses.isEmpty()) return "";
        return courses.stream()
                .map(course -> course.getProgramName() != null ? course.getProgramName() : "")
                .collect(Collectors.joining(", "));
    }

    // Returns enrolled course IDs as an immutable list
    public List<String> getEnrolledCourseIds() {
        if (courses == null || courses.isEmpty()) return Collections.emptyList();
        return courses.stream()
                .map(course -> course.getProgramId())
                .collect(Collectors.toList());
    }

    // Convenience methods
    public void addCourse(CourseDTO course) {
        if (course != null) {
            if (courses == null) courses = new ArrayList<>();
            courses.add(course);
        }
    }

    public void removeCourse(CourseDTO course) {
        if (course != null && courses != null) courses.remove(course);
    }

    public void clearCourses() {
        if (courses != null) courses.clear();
    }
}
