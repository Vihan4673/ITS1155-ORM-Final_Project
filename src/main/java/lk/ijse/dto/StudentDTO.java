package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private String studentId;
    private String name;
    private String address;
    private Long tel;
    private String email;
    private Date registrationDate;
    private List<String> enrolledCourseIds = new ArrayList<>();

    public StudentDTO(String studentId, String name, String address, Long tel, String email, Date registrationDate) {
        this.studentId = studentId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.registrationDate = registrationDate;
        this.enrolledCourseIds = new ArrayList<>();
    }

    public String getCoursesString() {
        if (enrolledCourseIds == null || enrolledCourseIds.isEmpty()) return "";
        return String.join(", ", enrolledCourseIds);
    }

    public void ensureId(String generatedId) {
        if (this.studentId == null || this.studentId.isEmpty()) {
            this.studentId = generatedId;
        }
    }
}
