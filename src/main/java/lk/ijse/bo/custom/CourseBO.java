package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBO;
import lk.ijse.dto.CourseDTO;

import java.util.List;

public interface CourseBO extends SuperBO {

    // CRUD operations
    void saveCourse(CourseDTO courseDTO);
    void deleteCourse(CourseDTO courseDTO);
    void updateCourse(CourseDTO courseDTO);

    // Read operations
    List<CourseDTO> getAllCourses();
    CourseDTO getCourse(String programId);

    // Utility
    String generateCourseId(); // auto-generate new course ID
}
